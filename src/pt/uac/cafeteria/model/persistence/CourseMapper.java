
package pt.uac.cafeteria.model.persistence;

import java.sql.*;
import java.util.List;
import pt.uac.cafeteria.model.domain.Course;

/**
 * DataMapper for the Course domain object.
 */
public class CourseMapper extends DatabaseMapper<Course> {

    /**
     * Creates a new instance of the mapper.
     *
     * @param con a database connection object.
     */
    public CourseMapper(Connection con) {
        super(con);
    }

    @Override
    protected String table() {
        return "Cursos";
    }

    /** Gets a list, ordered by name, of all courses. */
    public List<Course> findAll() {
        return findMany("SELECT id, nome FROM " + table() + " ORDER BY nome");
    }

    @Override
    protected String findStatement() {
        return "SELECT id, nome FROM " + table() + " WHERE id = ?";
    }

    @Override
    protected Course doLoad(Integer id, ResultSet rs) throws SQLException {
        String name = rs.getString("nome");
        return new Course(id, name);
    }

    @Override
    protected String insertStatement() {
        return "INSERT INTO " + table() + " (nome) VALUES (?)";
    }

    @Override
    protected void doInsert(Course course, PreparedStatement stmt) throws SQLException {
        stmt.setString(1, course.getName());
    }

    @Override
    protected String updateStatement() {
        return "UPDATE " + table() + " SET nome = ? WHERE id = ?";
    }

    @Override
    protected void doUpdate(Course course, PreparedStatement stmt) throws SQLException {
        doInsert(course, stmt);
        stmt.setInt(2, course.getId().intValue());
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
