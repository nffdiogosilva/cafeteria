
package pt.uac.cafeteria.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class StudentTest {
    private static final class Sample {
        static String name = "Jon Doe";
        static String streetAddress = "Av. de Franca, 1423";
        static String postalCode = "4000-302";
        static String city = "Porto";
        static String email = "email@example.com";
        static int phone = 912345678;
        static boolean scholarship = true;
        static String course = "IRM";
    }
    

    public Student createStudent() throws Exception {
        Address address = new Address(Sample.streetAddress,
                                      Sample.postalCode,
                                      Sample.city);
        
        Student student = Student.build(Sample.name, address,
                                        Sample.phone,
                                        Sample.email,
                                        Sample.scholarship,
                                        Sample.course);
        
        return student;
    }
    
    @Test
    public void canCreateStudent() throws Exception {
        Student student = createStudent();

        assertEquals(Sample.name, student.getName());
        assertEquals(Sample.streetAddress, student.getAddress().getStreetAddress());
        assertEquals(Sample.postalCode, student.getAddress().getPostalCode());
        assertEquals(Sample.city, student.getAddress().getCity());
        assertEquals(Sample.email, student.getEmail());
        assertEquals(Sample.phone, student.getPhone());
        assertTrue(student.hasScholarship());
        assertEquals(Sample.course, student.getCourse());
    }
}
