
package pt.uac.cafeteria.model.persistence;

import java.sql.*;
import java.util.List;
import pt.uac.cafeteria.model.domain.Course;

/**
 * DataMapper for the Course domain object.
 */
public class CourseMapper extends DatabaseMapper<Course> {

    /** Database table name. */
    private final String TABLE = "Cursos";

    /**
     * Creates a new instance of the mapper.
     *
     * @param con a database connection object.
     */
    public CourseMapper(Connection con) {
        super(con);
    }

    /** Gets a list, ordered by name, of all courses. */
    public List<Course> findAll() {
        return findMany("SELECT id, nome FROM " + TABLE + " ORDER BY nome");
    }

    @Override
    protected String findStatement() {
        return "SELECT id, nome FROM " + TABLE + " WHERE id = ?";
    }

    @Override
    protected Course doLoad(Integer id, ResultSet rs) throws SQLException {
        String name = rs.getString("nome");
        return new Course(id, name);
    }

    @Override
    protected String insertStatement() {
        return "INSERT INTO " + TABLE + " (nome) VALUES (?)";
    }

    @Override
    protected void doInsert(Course course, PreparedStatement insertStatement) throws SQLException {
        insertStatement.setString(1, course.getName());
    }

    @Override
    protected String updateStatement() {
        return "UPDATE " + TABLE + " SET nome = ? WHERE id = ?";
    }

    @Override
    protected void doUpdate(Course course, PreparedStatement updateStatement) throws SQLException {
        updateStatement.setString(1, course.getName());
        updateStatement.setInt(2, course.getId().intValue());
    }

    @Override
    protected String deleteStatement() {
        return "DELETE FROM " + TABLE + " WHERE id = ?";
    }

    /**
     * Saves a new course to the data storage.
     *
     * @param course name of the new course.
     * @return Number of the auto generated id for the course.
     */
    public Integer insert(String course) {
        return insert(new Course(null, course));
    }
}
