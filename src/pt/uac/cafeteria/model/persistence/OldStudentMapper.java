
package pt.uac.cafeteria.model.persistence;

import pt.uac.cafeteria.model.domain.Account;

/**
 * Data Mapper for old Student objects.
 * <p>
 * When a student finishes studies, his information is moved to an
 * historic of old students. This mapper manages this list.
 */
public class OldStudentMapper extends StudentMapper {

    /**
     * Creates a new OldStudentMapper instance.
     *
     * @param con a database connection object.
     */
    public OldStudentMapper(java.sql.Connection con) {
        super(con);
    }

    @Override
    protected String table() {
        return "histAlunos";
    }

    @Override
    protected Account createNewAccount(Integer id) {
        Account account = super.createNewAccount(id);
        account.close();
        return account;
    }
}
