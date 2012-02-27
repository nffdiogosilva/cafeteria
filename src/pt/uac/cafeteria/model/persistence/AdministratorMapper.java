
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

package pt.uac.cafeteria.model.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import pt.uac.cafeteria.model.domain.Administrator;
import pt.uac.cafeteria.model.persistence.abstracts.DatabaseMapper;

/**
 * DataMapper for the Administrator domain object.
 */
public class AdministratorMapper extends DatabaseMapper<Administrator> {

    /**
     * Creates a new AdministratorMapper instance.
     *
     * @param con a database connection object.
     */
    public AdministratorMapper(Connection con) {
        super(con);
    }

    @Override
    protected String table() {
        return "Admins";
    }

    /**
     * Finds a list of Administrator domain objects from a name.
     * <p>
     * The name can be a partial match, and is not case sensitive.
     *
     * @param name a full or partial match for an administrator name.
     * @return A list of matching administrators.
     */
    public List<Administrator> findByName(String name) {
        String query = findStatement("nome RLIKE ?");
        return findMany(query, new String[]{name});
    }

    /**
     * Finds an Administrator domain object from a username.
     *
     * @param username the administrator's username.
     * @return An Administrator domain object.
     */
    public Administrator findByUsername(String username) {
        String query = findStatement("username = ?");
        List<Administrator> admins = findMany(query, new String[]{username});
        return !admins.isEmpty() ? admins.get(0) : null;
    }

    /**
     * SQL SELECT statement missing only the criteria to append to the
     * WHERE clause.
     *
     * @param criteria contents of "WHERE" part of the SQL statement.
     * @return A complete SQL statement string.
     */
    protected String findStatement(String criteria) {
        return "SELECT id, nome, username, password"
                + " FROM " + table() + " WHERE " + criteria;
    }

    @Override
    protected String findStatement() {
        return findStatement("id = ?");
    }

    @Override
    protected Administrator doLoad(Integer id, ResultSet rs) throws SQLException {
        String name = rs.getString("nome");
        String username = rs.getString("username");
        String password = rs.getString("password");
        Administrator result = new Administrator(name, username, password);
        result.setId(id);
        return result;
    }

    @Override
    protected String insertStatement() {
        return "INSERT INTO " + table()
                + " (nome, username, password) VALUES (?, ?, ?)";
    }

    @Override
    protected void doInsert(Administrator admin, PreparedStatement stmt) throws SQLException {
        stmt.setString(1, admin.getName());
        stmt.setString(2, admin.getUsername());
        stmt.setString(3, admin.getPassword());
    }

    @Override
    protected String updateStatement() {
        return "UPDATE " + table()
                + " SET nome = ?, username = ?, password = ?"
                + " WHERE id = ?";
    }

    @Override
    protected void doUpdate(Administrator admin, PreparedStatement stmt) throws SQLException {
        doInsert(admin, stmt);
        stmt.setInt(4, admin.getId().intValue());
    }
}
