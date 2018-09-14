package software.testing.testdoubles;

import java.io.IOException;
import java.util.Properties;

/**
 * This factory is used to create a NetworkConnection object, based on the
 * value in the properties file.
 *
 * @author Neil Taylor (nst@aber.ac.uk)
 * @version 1.0
 */
public class NetworkConnectionFactory {

    /**
     * The properties that contains settings to control the network connection.
     */
    private Properties properties;

    /**
     * Creates a new instance.
     */
    public NetworkConnectionFactory() {

    }

    /**
     * Initialises the instance and sets the properties to be used.
     *
     * @param port The value of the port, e.g. production, testResponder, testFailure.
     * @param mode The value of the mode, e.g. COM1 or USB.
     */
    public void initialise(String port, String mode) {
        properties = new Properties();
        properties.put("port", port);
        properties.put("mode", mode);
    }

    /**
     * Initialises the instance and attempts to load the properties from the specified
     * @param propertyFile
     */
    public void initialise(String propertyFile) {

        properties = new Properties();
        try {
            properties.load(NetworkConnectionFactory.class.getResourceAsStream(propertyFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the string value for the given property name. This has been
     * read from the property file.
     *
     * @return A string loaded from the property. <code>null</code> is returned
     * if the properties were not loaded or do not contain the mode.
     */
    private String getStringProperty(String property) {
        if(properties == null) {
            return null;
        }
        return properties.getProperty(property);
    }

    /**
     * Gets the string that represents the connection type. This has been
     * read from the property file.
     *
     * @return A string loaded from the mode property. <code>null</code> is returned
     * if the properties were not loaded or do not contain the mode.
     */
    public String getConnectionMode() {
        return getStringProperty("mode");
    }

    /**
     * Gets the string that represents the connection port. This has been
     * read from the property file.
     *
     * @return A string loaded from the port property. <code>null</code> is returned
     * if the properties were not loaded or do not contain the mode.
     */
    public String getConnectionPort() {
        return getStringProperty("port");
    }

    /**
     * Creates a network connection, based on the value in the property file.
     * Changing the value in the property file before running the program
     * will change which of the three types of network connection are
     * returned.
     *
     * @return A network connection, or <code>null</code> if the properties
     * were not loaded or if the property does not contain one of the three
     * values: production, testResponse, testFailure.
     */
    public NetworkConnection getConnection() {

        System.out.println("mode value is: " + getConnectionMode());

        switch(getConnectionMode()) {
            case "production":
                return new USBNetworkConnection();
            case "testResponse":
                return new ResponderStubNetworkConnection();
            case "testFailure":
                return new SaboteurStubNetworkConnection();
        }

        return null;
    }

}
