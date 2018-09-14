package software.testing.testdoubles;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NetworkConnectionFactoryTest {

    @Test
    void shouldInitialiseUsingPropertiesFile() {
        NetworkConnectionFactory factory = new NetworkConnectionFactory();
        factory.initialise("/TestConnectionSettings.properties");
        assertEquals("production", factory.getConnectionMode());
        assertEquals("USB", factory.getConnectionPort());
    }

    @Test
    void shouldInitialiseUsingSettings() {
        NetworkConnectionFactory factory = new NetworkConnectionFactory();
        factory.initialise("COM1","testResponder");
        assertEquals("testResponder", factory.getConnectionMode());
        assertEquals("COM1", factory.getConnectionPort());
    }

    @Test
    void shouldCreateUSBConnection() {
        NetworkConnectionFactory factory = new NetworkConnectionFactory();
        factory.initialise("USB","production");
        assertTrue(factory.getConnection() instanceof USBNetworkConnection);
    }

    @Test
    void shouldCreateResponderConnection() {
        NetworkConnectionFactory factory = new NetworkConnectionFactory();
        factory.initialise("USB","testResponse");
        assertTrue(factory.getConnection() instanceof ResponderStubNetworkConnection);
    }

    @Test
    void shouldCreateSaboteurConnection() {
        NetworkConnectionFactory factory = new NetworkConnectionFactory();
        factory.initialise("USB","testFailure");
        assertTrue(factory.getConnection() instanceof SaboteurStubNetworkConnection);
    }

    @Test
    void shouldFailToCreateConnectionWhenModeIsIncorrect() {
        NetworkConnectionFactory factory = new NetworkConnectionFactory();
        factory.initialise("USB","invalidValue");
        assertNull(factory.getConnection());
    }

}