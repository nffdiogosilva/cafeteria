
package pt.uac.cafeteria.model;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a student.
 *
 * The process number is an auto-incremented number
 * prefixed with the current year.
 */
public class Student {

    /** Auto-incremented seed, from 1000 to 9999. */
    protected static int seed = 1000;

    /** The current year */
    private static int year = Calendar.getInstance().get(Calendar.YEAR);

    /** Student identification. It's a combination of the year and seed. */
    private final int id;

    /** Student's name. */
    private String name;

    /** Student address. */
    private Address address;

    /** Telephone number. */
    private int phone;

    /** Email address. */
    private String email;

    /** Student has scholarship? */
    private boolean scholarship;

    /** Student's course. */
    private String course;

    /**
     * Protected constructor.
     * 
     * Used when a given pre-generated id number is necessary (e.g. mock tests).
     */
    protected Student(int id) {
        this.id = id;
    }

    /**
     * Constructor.
     *
     * Generates a student id number automatically based on a seed number and
     * the current year.
     *
     * Format: YEAR + seed (concatenation).
     *
     * E.g. 20102144.
     */
    protected Student() {
        if (seed < 1000 || seed > 9999) {
            throw new RuntimeException("Impossivel gerar numero de processo. Nao ha nenhum disponivel.");
        }
        id = year * 10000 + seed;
    }

    /**
     * Factory method.
     *
     * Seed is incremented only on successful object creation and validation.
     *
     * @param name         full name
     * @param address      Address object
     * @param phone        phone number
     * @param email        email address
     * @param scholarship  scholarship
     * @param course       course
     * @return             Student object.
     */
    public static Student build(String name, Address address, int phone, String email, boolean scholarship, String course) {

        Student student = new Student();

        student.setName(name);
        student.setAddress(address);
        student.setPhone(phone);
        student.setEmail(email);
        student.setScholarship(scholarship);
        student.setCourse(course);

        seed++;

        return student;
    }

    /** Returns the identification of a student */
    public int getId() {
        return id;
    }

    /** Returns the name of a student */
    public String getName() {
        return name;
    }

    /**
     * Changes the name.
     *
     * @param name the name that will be defined
     */
    public void setName(String name) {
        this.name = name;
    }

    /** Returns the address of a student */
    public Address getAddress() {
        return address;
    }

    /** Sets the address. */
    public void setAddress(Address address) {
        this.address = address;
    }

    /** Returns the phone number. */
    public int getPhone() {
        return phone;
    }

    /** Sets phone to a new number */
    public void setPhone(int phone) {
        if (String.valueOf(phone).length() != 9) {
            throw new IllegalArgumentException("Numero de telefone invalido. Deve ter 9 digitos.");
        }
        this.phone = phone;
    }

    /** Returns the email address. */
    public String getEmail() {
        return email;
    }

    /** Sets the email to a new valid address. */
    public void setEmail(String email) {
        if (!emailIsValid(email)) {
            throw new IllegalArgumentException("Email com formato invalido.");
        }
        this.email = email;
    }

    /** Checks if the student has a scholarship. */
    public boolean hasScholarship() {
        return scholarship;
    }

    /** Changes the scholarship status. */
    public void setScholarship(boolean scholarship) {
        this.scholarship = scholarship;
    }

    /** Returns the course. */
    public String getCourse() {
        return course;
    }

    /** Sets the course. */
    public void setCourse(String course) {
        this.course = course;
    }

    /**
     * Checks if an email is a valid address.
     *
     * @param email  email to be checked.
     * @return       true if email is in the right format; false otherwise.
     */
    public boolean emailIsValid(String email){
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Student other = (Student) obj;
        if (this.id != other.id) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if (this.address != other.address && (this.address == null || !this.address.equals(other.address))) {
            return false;
        }
        if (this.phone != other.phone) {
            return false;
        }
        if ((this.email == null) ? (other.email != null) : !this.email.equals(other.email)) {
            return false;
        }
        if (this.scholarship != other.scholarship) {
            return false;
        }
        if ((this.course == null) ? (other.course != null) : !this.course.equals(other.course)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this.id;
        hash = 97 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 97 * hash + (this.address != null ? this.address.hashCode() : 0);
        hash = 97 * hash + this.phone;
        hash = 97 * hash + (this.email != null ? this.email.hashCode() : 0);
        hash = 97 * hash + (this.scholarship ? 1 : 0);
        hash = 97 * hash + (this.course != null ? this.course.hashCode() : 0);
        return hash;
    }
}
