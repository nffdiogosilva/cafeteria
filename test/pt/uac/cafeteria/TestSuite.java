
package pt.uac.cafeteria;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import pt.uac.cafeteria.model.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    ApplicationTest.class,
    AdministratorTest.class,
    AccountTest.class,
    StudentTest.class,
    AddressTest.class
})
public class TestSuite {}
