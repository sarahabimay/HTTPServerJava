import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CommandLineArgumentsTest {
    private CommandLineArguments arguments;
    private String[] no_arguments;
    private String[] one_argument;

    @Before
    public void setUp() {
        arguments = new CommandLineArguments();
        no_arguments = new String[0];
        one_argument = new String[2];
    }

    @Test
    public void startServerOnDefaultPort(){
        arguments.process_arguments(no_arguments);
        int expected_port_number = 5000;
        assertEquals(expected_port_number, arguments.portNumber());
    }

    @Test
    public void startServerWithDefaultPublicDir(){
        arguments.process_arguments(no_arguments);
        String expected_public_dir = "/Users/sarahjohnston/Sarah/CobSpec/public/";
        assertEquals(expected_public_dir, arguments.publicClassPath());
    }

    @Test
    public void startServerOnSpecifiedPort(){
        one_argument[0] = "-p";
        one_argument[1] = "3050";
        arguments.process_arguments(one_argument);
        int expected_port_number = 3050;
        assertEquals(expected_port_number, arguments.portNumber());
    }

    @Test
    public void startServerWithSpecifiedPublicDir(){
        one_argument[0] = "-d";
        one_argument[1] = "/Users/sarahjohnston/Sarah/Server/public/";
        arguments.process_arguments(one_argument);
        String expected_public_dir = "/Users/sarahjohnston/Sarah/Server/public/";
        assertEquals(expected_public_dir, arguments.publicClassPath());
    }

    @Test
    public void startServerWithSpecifiedPortAndPublicDir() {
        String[] both_arguments = new String[4];
        both_arguments[0] = "-p";
        both_arguments[1] = "4050";
        both_arguments[2] = "-d";
        both_arguments[3] = "/Users/sarahjohnston/Sarah/Server/public/";
        arguments.process_arguments(both_arguments);
        int expected_port_number = 4050;
        String expected_public_dir = "/Users/sarahjohnston/Sarah/Server/public/";
        assertEquals(arguments.portNumber(), expected_port_number);
        assertEquals(expected_public_dir, arguments.publicClassPath());
    }
}
