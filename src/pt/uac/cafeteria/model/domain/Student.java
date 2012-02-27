
/**
 * Cafeteria management application
 * Copyright (c) 2011, 2012 Helder Correia, Nuno Silva
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

package pt.uac.cafeteria.model.domain;

/**
 * Represents a student.
 * <p>
 * The id number should be an auto-incremented number
 * prefixed with the current year.
 */
public class Student implements DomainObject<Integer> {

    /** Student identification. */
    private Integer id;

    /** Student's name. */
    private String name;

    /** Student's account. */
    private Account account;

    /** Student address. */
    private Address address;

    /** Telephone number. */
    private Integer phone;

    /** Email address. */
    private String email;

    /** Student has scholarship? */
    private boolean scholarship;

    /** Student's course. */
    private Course course;

    /**
     * Creates a new Student instance.
     *
     * @param id the student id number.
     * @param name the student's name.
     * @param address the student's address.
     * @param phone a phone number.
     * @param email an email address.
     * @param scholarship scholarship status.
     * @param course the student's course.
     */
    public Student(Integer id, String name, Address address, int phone,
            String email, boolean scholarship, Course course) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.scholarship = scholarship;
        this.course = course;
    }

    /** Returns the identification of a student */
    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    /** Returns the student account */
    public Account getAccount() {
        return this.account;
    }

    /** Defines the student account */
    public void setAccount(Account account) {
        this.account = account;
    }

    /** Returns the name of a student */
    public String getName() {
        return this.name;
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
        return this.address;
    }

    /** Sets the address. */
    public void setAddress(Address address) {
        this.address = address;
    }

    /** Returns the phone number. */
    public Integer getPhone() {
        return new Integer(this.phone);
    }

    /** Sets phone to a new number */
    public void setPhone(int phone) {
        this.phone = phone;
    }

    /** Returns the email address. */
    public String getEmail() {
        return email;
    }

    /** Sets the email to a new valid address. */
    public void setEmail(String email) {
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
    public Course getCourse() {
        return course;
    }

    /** Sets the course. */
    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return id + "       " + name;
    }
}
