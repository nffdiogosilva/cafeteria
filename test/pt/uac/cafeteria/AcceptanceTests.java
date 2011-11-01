
package pt.uac.cafeteria;

import pt.uac.cafeteria.model.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AcceptanceTests {

    private Application app;

    @Before
    public void setUp() throws Exception {
        app = new Application();
    }

    @Test
    public void canAccessWithDefaultAdminCredentials() {
        Administrator admin = app.getAdministrator("administrador", "12345678");
        assertNotNull(admin);
    }

    @Test
    public void cantAccessWithInvalidAdminCredentials() {
        Administrator admin = app.getAdministrator("invalidadmin", "password");
        assertNull(admin);
    }
}
