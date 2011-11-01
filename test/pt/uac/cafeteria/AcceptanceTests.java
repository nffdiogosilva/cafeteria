
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
        Backend backend = app.getBackend("administrador", "12345678");
        assertNotNull(backend);
    }

    @Test
    public void cantAccessWithInvalidAdminCredentials() {
        Backend backend = app.getBackend("invalidadmin", "password");
        assertNull(backend);
    }
}
