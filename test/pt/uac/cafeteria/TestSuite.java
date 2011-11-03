
package pt.uac.cafeteria;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import pt.uac.cafeteria.model.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({ 
    AcceptanceTests.class,
    AdministratorTest.class,
    StudentTest.class
})
public class TestSuite {}
