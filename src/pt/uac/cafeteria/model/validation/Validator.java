
package pt.uac.cafeteria.model.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import pt.uac.cafeteria.model.domain.DomainObject;

/**
 * Common validation algorithms for domain object validators.
 * <p>
 * Static methods are available for common types of validation checks,
 * which can be used outside the context of a domain object validator.
 * <p>
 * Assertions are made that record an error message if a validation doesn't
 * pass. The list of errors found can be retrieved, in order to give
 * feedback to the user on what needs to be corrected.
 *
 * @param <T> the domain object type to validate.
 */
public abstract class Validator<T extends DomainObject> {

    /** List of error messages for assertions that didn't pass. */
    protected List<String> errors = new ArrayList<String>();

    /**
     * Helper method that matches any regular expression pattern against a string.
     *
     * @param regex the regular expression to use.
     * @param subject the string to match <code>regex</code> to.
     * @return true if the expression matches the string, or false if it doesn't.
     */
    public static boolean matchPattern(String regex, String subject) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(subject);
        return matcher.matches();
    }

    /**
     * Helper method that checks if a value is within the
     * boundaries of a range of values.
     *
     * @param low the lower bound, or lower value.
     * @param high the higher bound, or higher value.
     * @param value the value to test.
     * @return true if <code>value</code> is within <code>low</code> and
     *         <code>high</code>, or false if it isn't.
     */
    public static boolean inRange(int low, int high, int value) {
        return value >= low && value <= high;
    }

    /**
     * Helper method that checks if an <code>int</code> from the java language
     * has a given number of digits.
     *
     * @param digits number of digits to check.
     * @param value the value to test.
     * @return true if <code>value</code> has exactly how many digits from
     *         <code>digits</code>, or false if it doesn't.
     */
    public static boolean testDigits(int digits, int value) {
        return String.valueOf(value).length() == digits;
    }

    /**
     * Helper method that checks if a String is empty.
     * <p>
     * This is different from a String's <code>isEmpty()</code> because it
     * also checks if the value is null, to prevent a NullPointerException.
     *
     * @param subject the string to test.
     * @return true if the string is null or empty; false otherwise.
     */
    public static boolean isEmpty(String subject) {
        return subject == null || subject.isEmpty();
    }

    /**
     * Helper method that checks if no string is empty in a group of strings.
     *
     * @param strings the group of strings to test.
     * @return true if none of the strings are empty, or false otherwise.
     */
    public static boolean isNoneEmpty(String... strings) {
        for (String string : strings) {
            if (isEmpty(string)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Helper method that checks if at least one string is empty in a group
     * of strings.
     *
     * @param strings the group of strings to test.
     * @return true if at least on string is empty, or false otherwise.
     */
    public static boolean isAnyEmpty(String... strings) {
        for (String string : strings) {
            if (isEmpty(string)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks weather the domain object being validated is valid or not.
     * <p>
     * Any errors found are recorded.
     *
     * @param subject the domain object being tested.
     * @return true if no errors found or false otherwise.
     */
    public boolean isValid(T subject) {
        doAssertions(subject);
        return errors.isEmpty();
    }

    /**
     * Makes all necessary assertions on the domain object.
     *
     * @param subject the domain object being tested.
     */
    protected abstract void doAssertions(T subject);

    /**
     * Asserts an aggregate domain object and adds any found error messages to
     * this one.
     * <p>
     * Since objects can have various associations, this allows separation
     * of concerns by validating an aggregated object with its own validator,
     * but now requiring it to be validated outside the parent object it is
     * associated to.
     *
     * @param <S> the type of the aggregated domain object.
     * @param subject the aggregated domain object.
     * @param validator the aggregated domain object's validator.
     */
    protected <S extends DomainObject> void assertAggregate(S subject, Validator<S> validator) {
        if (!validator.isValid(subject)) {
            errors.addAll(validator.getErrors());
        }
    }

    /**
     * Checks if a given required field is not empty.
     * <p>
     * For any object, if it's null, it is empty. For a string,
     * it's content also needs to not be empty.
     *
     * @param fieldName the field name to be used in an error message.
     * @param field the value of the field.
     * @return true if the required field is present, or false if it's empty.
     */
    protected boolean assertRequired(String fieldName, Object field) {
        boolean required;

        if (field instanceof String) {
            required = !isEmpty((String) field);
        }
        else {
            required = ( field != null );
        }

        check(required, requiredFieldError(fieldName));
        return required;
    }

    /**
     * Produces an error message for a required field.
     * <p>
     * Override method to give an alternate error message.
     *
     * @param fieldName the name of the required field to be mentioned in the error message.
     * @return An error message as feedback to the user on which field needs to be specified.
     */
    protected String requiredFieldError(String fieldName) {
        return "Campo obrigatório: " + fieldName + ".";
    }

    /**
     * Checks in an error message on a false assertion.
     *
     * @param valid result of an assertion.
     * @param errorMessage the error message to check into the list of errors found.
     */
    protected void check(boolean valid, String errorMessage) {
        if (!valid) {
            errors.add(errorMessage);
        }
    }

    /** Gets a collection of all errors found during validation. */
    public Collection<String> getErrors() {
        return Collections.unmodifiableCollection(errors);
    }
}