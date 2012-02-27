
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

import java.sql.*;
import java.util.List;
import pt.uac.cafeteria.model.domain.Course;
import pt.uac.cafeteria.model.persistence.abstracts.DatabaseMapper;

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
