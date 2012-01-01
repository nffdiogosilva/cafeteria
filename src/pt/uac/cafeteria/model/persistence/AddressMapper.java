
package pt.uac.cafeteria.model.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import pt.uac.cafeteria.model.domain.Address;
import pt.uac.cafeteria.model.persistence.abstracts.DatabaseMapper;

/**
 * Data Mapper for Address domain objects.
 */
public class AddressMapper extends DatabaseMapper<Address> {

    /**
     * Creates a new AddressMapper instance.
     *
     * @param con a database connection object.
     */
    public AddressMapper(Connection con) {
        super(con);
    }

    @Override
    protected String table() {
        return "Moradas";
    }

    @Override
    protected String findStatement() {
        return "SELECT id, rua, nr, cod_postal, localidade"
                + " FROM " + table() + " WHERE id = ?";
    }

    @Override
    protected Address doLoad(Integer id, ResultSet rs) throws SQLException {
        String streetAddress = rs.getString("rua");
        String number = rs.getString("nr");
        String postalCode = rs.getString("cod_postal");
        String city = rs.getString("localidade");
        Address result = Address.build(streetAddress, number, postalCode, city);
        result.setId(id);
        return result;
    }

    @Override
    protected String insertStatement() {
        return "INSERT INTO " + table()
                + " (rua, nr, cod_postal, localidade) VALUES (?, ?, ?, ?)";
    }

    @Override
    protected void doInsert(Address address, PreparedStatement stmt) throws SQLException {
        stmt.setString(1, address.getStreetAddress());
        stmt.setString(2, address.getNumber());
        stmt.setString(3, address.getPostalCode());
        stmt.setString(4, address.getCity());
    }

    @Override
    protected String updateStatement() {
        return "UPDATE " + table()
                + " SET rua = ?, nr = ?, cod_postal = ?, localidade = ?"
                + " WHERE id = ?";
    }

    @Override
    protected void doUpdate(Address address, PreparedStatement stmt) throws SQLException {
        doInsert(address, stmt);
        stmt.setInt(5, address.getId().intValue());
    }
 }
