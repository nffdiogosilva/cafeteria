
package pt.uac.cafeteria.model.validation;

import pt.uac.cafeteria.model.domain.Student;

/**
 * Validates Student domain objects.
 * <p>
 * It's possible to override the email regular expression.
 */
public class StudentValidator extends Validator<Student> {

    /** Regular expression for email validation. */
    protected String regexEmail = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /**
     * Sets a different regular expression for email validation.
     *
     * @param regex new regular expression.
     */
    public void setEmailPattern(String regex) {
        regexEmail = regex;
    }

    @Override
    public void doAssertions(Student student) {
        if (student.getId() != null &&
                assertRequired("número de processo", student.getId())) {
            assertId(student.getId());
        }

        assertRequired("nome", student.getName());

        if (assertRequired("endereço", student.getAddress())) {
            assertAggregate(student.getAddress(), new AddressValidator());
        }

        if (assertRequired("contacto telefónico", student.getPhone())) {
            assertPhone(student.getPhone());
        }

        if (assertRequired("email", student.getEmail())) {
            assertEmail(student.getEmail());
        }

        if (assertRequired("curso", student.getCourse())) {
            assertAggregate(student.getCourse(), new CourseValidator());
        }
    }

    /**
     * Asserts the student id number.
     * <p>
     * It must be an 8 digit long value, composed of a year concatenated by
     * a seed number ranging between 1000 and 9999.
     *
     * @param id the student id number.
     */
    protected void assertId(Integer id) {
        boolean length = testDigits(8, id.intValue());
        check(length, "Número de processo inválido.");

        if (length) {
            int seed = Integer.parseInt(id.toString().substring(4));
            boolean range = inRange(1000, 9999, seed);
            check(range, "Número de matrícula inválido. Fora dos limites de 1000 a 9999.");
        }
    }

    /**
     * Asserts a phone number.
     * <p>
     * It must be a 9 digit number.
     *
     * @param phone a phone number.
     */
    protected void assertPhone(int phone) {
        boolean length = testDigits(9, phone);
        check(length, "Número de telefone inválido. Deve ter 9 dígitos.");
    }

    /**
     * Asserts an email address.
     * <p>
     * Value is matched against a regular express that can be overriden by
     * <code>setEmailPattern(newRegex)</code>.
     *
     * @param email an email address.
     */
    protected void assertEmail(String email) {
        boolean format = matchPattern(regexEmail, email);
        check(format, "Email com formato inválido.");
    }
}
