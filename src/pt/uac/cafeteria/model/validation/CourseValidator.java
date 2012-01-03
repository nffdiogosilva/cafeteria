
package pt.uac.cafeteria.model.validation;

import pt.uac.cafeteria.model.domain.Course;

/**
 * Validates Course domain objects.
 */
public class CourseValidator extends Validator<Course> {

    @Override
    public void doAssertions(Course course) {
        assertRequired("nome", course.getName());
    }
}
