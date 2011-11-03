
package pt.uac.cafeteria.model;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a student.
 */
public class Student {

    /** Generate number starting at 1000 */
    private static int generateNumber = 1000;

    /** The actual year */
    private static int year;

    /** Identification of a student */
    private int id;

    /** Name of a student */
    private String name;

    /** Address of a student */
    private Address address;

    /** Telephone of a student */
    private int phone;

    /** Email of a student */
    private String email;

    /** Tells if a student has scholarship or not */
    private boolean scholarship;

    /** Course of a student */
    private String course;
    
    /** Protected constructor. Used in tests (mocks). */
    protected Student(int id) {
        this.id = id;
    }

    /** Student builder */
    public static Student build(String name, Address address, int phone, String email, boolean scholarship, String course) throws Exception {
        
        Student student;
        
        year = Calendar.getInstance().get(Calendar.YEAR);
       
        if (generateNumber >= 1000 && generateNumber <= 9000) {
            student = new Student(year * 10000 + generateNumber);
        }
        else {
            throw new Exception("Unable to generate new number");
        }

        student.setName(name);
        student.setAddress(address);
        student.setPhone(phone);
        student.setEmail(email);
        student.setScholarship(scholarship);
        student.setCourse(course);

        generateNumber++;

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

    /**
     * Changes the address.
     *
     * @param address the address that will be defined
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /** Returns the telephone of a student */
    public long getPhone() {
        return phone;
    }

    /**
     * Changes the telephone.
     * 
     * @param phone the telephone that will be defined
     * @throws Exception exception that leads with invalid number format
     */
    public void setPhone(int phone) throws Exception {
        if (phone >= 100000000) {
            this.phone = phone;
        }
        else {
            throw new Exception("Invalid number format");
        }
    }

    /** Returns the email of a student */
    public String getEmail() {
        return email;
    }

    /**
     * Changes the email.
     *
     * @param email the email that will be defined
     * @throws Exception exception that leads with invalid email format
     */
    public void setEmail(String email) throws Exception {
        if (this.emailIsValid(email)) {
            this.email = email;
        }
        else {
            throw new Exception("Invalid email format");
        }
    }

    /** Returns the status of scholarship of a student */
    public boolean hasScholarship() {
        return scholarship;
    }

    /**
     * Changes the status of the scholarship
     *
     * @param scholarship state of the scholarship that will be defined
     */
    public void setScholarship(boolean scholarship) {
        this.scholarship = scholarship;
    }

    /** Returns the course of a student */
    public String getCourse() {
        return course;
    }

    /**
     * Changes the course.
     *
     * @param course the course that will be defined
     */
    public void setCourse(String course) {
        this.course = course;
    }

    /** Returns a string that describes a student */
    @Override
    public String toString() {
        return "\nStudent number: " + this.id +
                "\nName: " + this.name +
                "\nAddress: " + this.address +
                "\nPhone: " + this.phone +
                "\nEmail: " + this.email +
                "\nScholarship Student: " + this.scholarship +
                "\nCourse: " + this.course;
    }

    /**
     * Method that checks if the email has the right format
     *
     * @param email the email that will be checked
     * @return Returns a boolean. True if the email has the right format. False if the email has the wrong format
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
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + this.id;
        hash = 83 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 83 * hash + (this.address != null ? this.address.hashCode() : 0);
        hash = 83 * hash + this.phone;
        hash = 83 * hash + (this.email != null ? this.email.hashCode() : 0);
        hash = 83 * hash + (this.scholarship ? 1 : 0);
        hash = 83 * hash + (this.course != null ? this.course.hashCode() : 0);
        return hash;
    }
}
