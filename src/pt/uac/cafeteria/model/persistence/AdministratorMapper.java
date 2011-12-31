

package pt.uac.cafeteria.model.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import pt.uac.cafeteria.model.domain.Administrator;

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
     * Finds an Administrator domain object from a username.
     *
     * @param username the administrator's username.
     * @return An Administrator domain object.
     */
    public Administrator findByUsername(String username) {
        String query = "SELECT id, nome, username, password FROM " + table() + " WHERE username = ?";
        List<Administrator> admins = findMany(query, new String[]{username});
        return !admins.isEmpty() ? admins.get(0) : null;
    }

    @Override
    protected String findStatement() {
        return "SELECT id, nome, username, password"
                + " FROM " + table() + " WHERE id = ?";
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
        stmt.setString(1, admin.getName());
        stmt.setString(2, admin.getUsername());
        stmt.setString(3, admin.getPassword());
        stmt.setInt(4, admin.getId().intValue());
    }
}
