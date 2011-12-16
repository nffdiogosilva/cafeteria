
package pt.uac.cafeteria.model.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pt.uac.cafeteria.model.ApplicationException;
import pt.uac.cafeteria.model.domain.DomainObject;

public abstract class DatabaseMapper<T extends DomainObject<Integer>> implements DataMapper<T, Integer> {

    protected Map<Integer, T> loadedMap = new HashMap<Integer, T>();
    abstract protected String findStatement();

    private static Connection con;

    private Connection getDB() {
        if (con == null) {
            try {
                con = DriverManager.getConnection(
                        "jdbc:mysql://localhost/cafeteria", "root", "root");
            } catch (SQLException e) {
                throw new ApplicationException(e.getMessage());
            }
        }
        return con;
    }

    @Override
    public T find(Integer id) {
        T result = loadedMap.get(id);
        if (result != null) {
            return result;
        }
        PreparedStatement findStatement = null;
        try {
            findStatement = getDB().prepareStatement(findStatement());
            findStatement.setInt(1, id.intValue());
            ResultSet rs = findStatement.executeQuery();
            rs.next();
            result = load(rs);
            return result;
        } catch (SQLException e) {
            throw new ApplicationException(e.getMessage());
        } finally {
            if (findStatement != null) {
                try {
                    findStatement.close();
                } catch (SQLException e) {
                    throw new ApplicationException(e.getMessage());
                }
            }
        }
    }

    protected T load(ResultSet rs) throws SQLException {
        Integer id = new Integer(rs.getInt(1));
        if (loadedMap.containsKey(id)) {
            return loadedMap.get(id);
        }
        T result = doLoad(id, rs);
        loadedMap.put(id, result);
        return result;
    }

    abstract protected T doLoad(Integer id, ResultSet rs) throws SQLException;

    protected List<T> loadAll(ResultSet rs) throws SQLException {
        List<T> result = new ArrayList<T>();
        while (rs.next()) {
            result.add(load(rs));
        }
        return result;
    }

    protected List<T> findMany(StatementSource source) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = getDB().prepareStatement(source.sql());
            for (int i = 0; i < source.parameters().length; i++) {
                stmt.setObject(i+1, source.parameters()[i]);
            }
            rs = stmt.executeQuery();
            return loadAll(rs);
        } catch (SQLException e) {
            throw new ApplicationException(e.getMessage());
        }
    }

    @Override
    public Integer insert(T subject) {
        PreparedStatement insertStatement = null;
        try {
            insertStatement = getDB().prepareStatement(insertStatement());
            doInsert(subject, insertStatement);
            insertStatement.executeUpdate();
            loadedMap.put(subject.getId(), subject);
            return subject.getId();
        } catch (SQLException e) {
            throw new ApplicationException(e.getMessage());
        }
    }

    abstract protected String insertStatement();
    abstract protected void doInsert(T subject, PreparedStatement insertStatement) throws SQLException;
}
