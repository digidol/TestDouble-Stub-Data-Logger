package software.testing.testdoubles;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This class represents a simplified data logger that would read values
 * from a network connection. This implementation is simplified so that
 * it reads values and outputs them to the console. In a real application,
 * we would want to think about how the values are used by the user
 * interface and also stored to file.
 *
 * @author Neil Taylor (nst@aber.ac.uk)
 * @version 1.0
 */
public class DataLogger {

    /** The network connection object used to read data. */
    private NetworkConnection connection;

    /**
     * A local store of any data that is received. In the real application,
     * this would be stored to a file. For this example, we will
     * keep this arraylist of values in memory. That will mean we can
     * write some unit tests to check the returned data values.
     */
    private ArrayList<String> dataReceived = new ArrayList<String>();

    /**
     * Logs values from the connection.
     * <p>
     * For this implementation, the code will read 10 values and
     * then exit the method. In a real implementation, the values
     * would be passed to user interface
     *
     * @param numberOfValues The number of values to read. This
     *                       should be a value of 1 or more.
     *
     * @throws IllegalArgumentException if the number of values
     * is less than 1.
     */
    private void logValues(int numberOfValues) {

        if(connection == null) {
            return;
        }

        if(numberOfValues < 1) {
            throw new IllegalArgumentException("Value must be 1 or more.");
        }

        for(int i = 0; i < numberOfValues; i++) {
            try {
                String data = connection.readData();

                if(data != null) {
                    dataReceived.add(data);
                    System.out.println("Accessed value: " + data);
                }
                else {
                    System.out.println("Error reading the value");
                }

            }
            catch(IOException e) {
                System.err.println("Error accessing the data.");
                return;
            }
        }
    }

    /**
     * Close the connection. This will change the connection
     * so that it cannot read any further values.
     */
    private void close() {
        if(connection != null) {
            connection.close();
            connection = null;
        }
    }

    /**
     * Initialises the class with a link to the network connection.
     * <p>
     * Any existing data in the dataReceived buffer will be cleared.
     *
     * @param factory A factory that can access the network connection.
     *
     * @return True if the initialisation was successful. Otherwise, false.
     */
    public boolean initialise(NetworkConnectionFactory factory) {

        dataReceived = new ArrayList<>();

        connection = factory.getConnection();
        if(connection == null) {
            System.out.println("There was an error getting the connection.");
            System.out.println("The connection mode is: " + factory.getConnectionMode());
            return false;
        }

        try {
            connection.open(factory.getConnectionPort());
            return true;
        } catch (IOException e) {
            System.err.println("Unable to open connection.");
            connection = null; // reset the connection
            return false;
        }
    }

    /**
     * Retrieves an array with a copy of the data
     * received from the network connection.
     *
     * @return An array that contains all of the items received
     * whilst the logValues() method has run.
     */
    public String[] getDataReceived() {
        String[] result = new String[dataReceived.size()];
        return dataReceived.toArray(result);
    }

    /**
     * Starts this example program.
     *
     * @param args The command line arguments. These are not used.
     */
    public static void main(String[] args) {
        DataLogger logger = new DataLogger();
        NetworkConnectionFactory factory = new NetworkConnectionFactory();
        factory.initialise("/NetworkConnectionSettings.properties");

        if(logger.initialise(factory)) {
            logger.logValues(20);
            System.out.println("Closing the connection.");
            logger.close();
            System.out.println("Exiting.");
        }
    }

}
