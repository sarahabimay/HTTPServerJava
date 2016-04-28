import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MainTest {

    private HttpServerMain server;
    private String[] no_options;
    private String[] port_command;

    @Before
    public void setUp() {
        server = new HttpServerMain();
        no_options = new String[0];
        port_command = new String[2];
    }

    @Test
    public void startServerOnDefaultPort(){
        server.main(no_options);
        int expected_port_number = 5000;
        assertEquals(server.portNumber(), expected_port_number);
    }

    @Test
    public void startServerWithDefaultPublicDir(){
        server.main(no_options);
        String expected_public_dir = "/Users/sarahjohnston/Sarah/CobSpec/public/";
        assertEquals(server.publicClassPath(), expected_public_dir);
    }

    @Test
    public void startServerOnSpecifiedPort(){
        port_command[0] = "-p";
        port_command[1] = "5050";
        server.main(port_command);
        int expected_port_number = 5050;
        assertEquals(server.portNumber(), expected_port_number);
    }

    @Test
    public void startServerWithSpecifiedPublicDir(){
        port_command[0] = "-d";
        port_command[1] = "/Users/sarahjohnston/Sarah/Server/public/";
        server.main(port_command);
        String expected_public_dir = "/Users/sarahjohnston/Sarah/Server/public/";
        assertEquals(server.publicClassPath(), expected_public_dir);
    }
}
