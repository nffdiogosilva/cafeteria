
package pt.uac.cafeteria.model;

import java.util.Calendar;
import org.junit.Test;
import static org.junit.Assert.*;

class MockLastStudent extends Student {
    static int seed = 9999+1;
    MockLastStudent() {
        super();
    }
}

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

    private Student createStudent() {
        Student student = Student.build(Sample.name,
                                        Address.build(
                                            Sample.streetAddress,
                                            Sample.postalCode,
                                            Sample.city),
                                        Sample.phone,
                                        Sample.email,
                                        Sample.scholarship,
                                        Sample.course);

        return student;
    }

    @Test
    public void canCreateStudent() throws Exception {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        Student student = createStudent();

        assertEquals(Sample.name, student.getName());
        assertEquals(Sample.streetAddress, student.getAddress().getStreetAddress());
        assertEquals(Sample.postalCode, student.getAddress().getPostalCode());
        assertEquals(Sample.city, student.getAddress().getCity());
        assertEquals(Sample.email, student.getEmail());
        assertEquals(Sample.phone, student.getPhone());
        assertTrue(student.hasScholarship());
        assertEquals(Sample.course, student.getCourse());

        // First id should be YEAR+1000 (concatenation)
        String expected = String.format("%d1000", year);
        String actual = String.valueOf(student.getId());
        assertEquals(expected, actual);
    }

    @Test(expected=RuntimeException.class)
    public void lastNumber() {
        new MockLastStudent();
    }

    @Test(expected=IllegalArgumentException.class)
    public void cantUseAnInvalidEmailAddress() {
        Student student = createStudent();
        student.setEmail("wrong@email");
    }

    @Test(expected=IllegalArgumentException.class)
    public void cantUseSmallPhone() {
        Student student = createStudent();
        student.setPhone(12345678);
    }

    @Test(expected=IllegalArgumentException.class)
    public void cantUseBigPhone() {
        Student student = createStudent();
        student.setPhone(1234567891);
    }
}
