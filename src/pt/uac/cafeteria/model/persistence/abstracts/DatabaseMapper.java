
/**
 * Cafeteria management application
 * Copyright (c) 2011, 2012 Helder Correia
 * 
 * This file is part of Cafeteria.
 * 
 * Cafeteria is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * Cafeteria is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Cafeteria.  If not, see <http://www.gnu.org/licenses/>.
 */

package pt.uac.cafeteria.model.persistence.abstracts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pt.uac.cafeteria.model.ApplicationException;
import pt.uac.cafeteria.model.domain.DomainObject;

/**
 * Abstraction for database based data mappers.
 * <p>
 * Most of the database access logic is provided here. Abstract methods need
 * to be implemented to add domain object specific details.
 * <p>
 * <code>Loader</code> interface allows external management of a mapper's
 * loaded objects. Most useful when trying to save up calls to the DB server,
 * because it allows one mapper to load objects associated to it but managed
 * by other mappers, in only one call. It may also be useful to force the
 * mapper to refresh a load from the database in some cases.
 * 
 * @param <T> the domain object type.
 */
public abstract class DatabaseMapper<T extends DomainObject<Integer>>
                implements DataMapper<T, Integer>, Loader<T, Integer> {

    /** Map with already loaded instances of the domain object. */
    protected Map<Integer, T> loadedMap = new HashMap<Integer, T>();

    /** Database connection object. */
    protected final Connection DB;

    /**
     * Creates a new instance of the mapper.
     *
     * @param con a database connection object.
     */
    public DatabaseMapper(Connection con) {
        this.DB = con;
    }

    @Override
    public T register(Integer id, T subject) {
        return loadedMap.put(id, subject);
    }

    @Override
    public boolean isLoaded(Integer id) {
        return loadedMap.containsKey(id);
    }

    @Override
    public T retrieve(Integer id) {
        return loadedMap.get(id);
    }

    @Override
    public T remove(Integer id) {
        return loadedMap.remove(id);
    }

    /** Gets the database table name to map. */
    abstract protected String table();

    /**
     * Convenience method that finds a domain object based on its id as
     * a primitive int.
     *
     * @param id domain object's id.
     * @return The domain object, or null if not found.
     */
    public T find(int id) {
        return find(new Integer(id));
    }

    @Override
    public T find(Integer id) {
        T result = retrieve(id);
        if (result != null) {
            return result;
        }
        PreparedStatement findStatement = null;
        try {
            findStatement = DB.prepareStatement(findStatement());
            findStatement.setInt(1, id.intValue());

            ResultSet rs = findStatement.executeQuery();
            if (rs.next()) {
                result = load(rs);
                return result;
            }
            return null;

        } catch (SQLException e) {
            throw new ApplicationException(e.getMessage());
        } finally {
            cleanUp(findStatement);
        }
    }

    /** Attempts to close a Statement resource. */
    protected void cleanUp(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                throw new ApplicationException(e.getMessage());
            }
        }
    }

    /**
     * Returns a SQL SELECT statement for one object.
     * <p>
     * Id field needed as input.
     * <p>
     * Typical usage:
     * <code>SELECT field1, field2 FROM table WHERE id = ?</code>
     */
    abstract protected String findStatement();

    /**
     * Makes the mapping between a database result set to the corresponding
     * domain object.
     * <p>
     * The first field int the ResultSet must be id, to check if it is already
     * loaded.
     * <p>
     * Actual field mapping is done in domain specific method doLoad().
     *
     * @param rs database result set.
     * @return The domain object mapped from the ResultSet.
     * @throws SQLException in case of SQL error. 
     */
    protected T load(ResultSet rs) throws SQLException {
        Integer id = new Integer(rs.getInt(1));
        if (isLoaded(id)) {
            return retrieve(id);
        }
        T result = doLoad(id, rs);
        register(id, result);
        return result;
    }

    /**
     * Actual domain specific mapping from a database result set to
     * the corresponding domain object.
     *
     * @param id the domain object's id.
     * @param rs the database result set.
     * @return The domain object mapped from the ResultSet.
     * @throws SQLException in case of SQL error. 
     */
    abstract protected T doLoad(Integer id, ResultSet rs) throws SQLException;

    /**
     * Loads all records from a result set.
     *
     * @param rs the database result set.
     * @return A list of domain objects loaded from the result set.
     * @throws SQLException in case of SQL error. 
     */
    protected List<T> loadAll(ResultSet rs) throws SQLException {
        List<T> result = new ArrayList<T>();
        while (rs.next()) {
            result.add(load(rs));
        }
        return result;
    }

    /**
     * Finds more than one object for any SELECT statement with no
     * input fields or parameters.
     *
     * @param sql SQL select statement without parameters.
     * @return A list of domain objects found.
     */
    protected List<T> findMany(String sql) {
        return findMany(sql, new Object[]{});
    }

    /**
     * Finds more than one object for any criteria.
     *
     * @param sql SQL select statement with input fields.
     * @param parameters array of parameters to bind to the sql input fields.
     * @return A list of domain objects found.
     */
    protected List<T> findMany(String sql, Object[] parameters) {
        PreparedStatement stmt = null;
        try {
            stmt = DB.prepareStatement(sql);
            for (int i = 0; i < parameters.length; i++) {
                stmt.setObject(i+1, parameters[i]);
            }
            ResultSet rs = stmt.executeQuery();
            return loadAll(rs);
        } catch (SQLException e) {
            throw new ApplicationException(e.getMessage());
        } finally {
            cleanUp(stmt);
        }
    }

    @Override
    public Integer insert(T subject) {
        PreparedStatement insertStatement = null;
        try {
            insertStatement = DB.prepareStatement(insertStatement(), Statement.RETURN_GENERATED_KEYS);
            doInsert(subject, insertStatement);

            int affectedRows = insertStatement.executeUpdate();
            if (affectedRows == 0) {
                return null;
            }

            ResultSet generatedKeys = insertStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                subject.setId(generatedKeys.getInt(1));
            }

            register(subject.getId(), subject);
            return subject.getId();

        } catch (SQLException e) {
            throw new ApplicationException(e.getMessage());
        } finally {
            cleanUp(insertStatement);
        }
    }

    /**
     * Returns a SQL INSERT statement for one object.
     * <p>
     * Typical usage:
     * <code>INSERT INTO table (field1, field2) VALUES (?, ?)</code>
     */
    abstract protected String insertStatement();

    /**
     * Actual domain specific mapping from a domain object to
     * database fields set in insertStatement().
     *
     * @param subject domain object to insert.
     * @param insertStatement prepared statement to set fields.
     * @throws SQLException in case of SQL error. 
     */
    abstract protected void doInsert(T subject, PreparedStatement insertStatement) throws SQLException;

    @Override
    public boolean update(T subject) {
        PreparedStatement updateStatement = null;
        try {
            updateStatement = DB.prepareStatement(updateStatement());
            doUpdate(subject, updateStatement);

            int affectedRows = updateStatement.executeUpdate();
            if (affectedRows > 0) {
                register(subject.getId(), subject);
                return true;
            }
            return false;

        } catch (SQLException e) {
            throw new ApplicationException(e.getMessage());
        } finally {
            cleanUp(updateStatement);
        }
    }

    /**
     * Returns a SQL UPDATE statement for one object.
     * <p>
     * Typical usage:
     * <code>UPDATE table SET field1 = ?, field2 = ? WHERE id = ?</code>
     */
    abstract protected String updateStatement();

    /**
     * Actual domain specific mapping from a domain object to
     * database fields set in updateStatement().
     *
     * @param subject domain object to update.
     * @param updateStatement prepared statement to set fields.
     * @throws SQLException in case of SQL error. 
     */
    abstract protected void doUpdate(T subject, PreparedStatement updateStatement) throws SQLException;

    @Override
    public boolean delete(T subject) {
        PreparedStatement deleteStatement = null;
        try {
            deleteStatement = DB.prepareStatement(deleteStatement());
            deleteStatement.setInt(1, subject.getId().intValue());

            int affectedRows = deleteStatement.executeUpdate();
            if (affectedRows != 0) {
                remove(subject.getId());
                return true;
            }
            return false;

        } catch (SQLException e) {
            throw new ApplicationException(e.getMessage());
        } finally {
            cleanUp(deleteStatement);
        }
    }

    /**
     * Returns a SQL DELETE statement for one object.
     * <p>
     * Id field needed as input.
     * <p>
     * Typical usage:
     * <code>DELETE FROM table WHERE id = ?</code>
     */
    protected String deleteStatement() {
        return "DELETE FROM " + table() + " WHERE id = ?";
    }
}
