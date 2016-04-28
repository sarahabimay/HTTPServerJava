import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MainTest {
    @Test
    public void startServerOnDefaultPort(){
        HttpServerMain server = new HttpServerMain();
        String[] commands = new String[0];
        server.main(commands);
        assertEquals(server.portNumber(), 5000);
    }

    @Test
    public void startServerWithDefaultPublicDir(){
        HttpServerMain server = new HttpServerMain();
        String[] commands = new String[0];
        server.main(commands);
        assertEquals(server.publicClassPath(), "/Users/sarahjohnston/Sarah/CobSpec/public/");
    }

    @Test
    public void startServerOnSpecifiedPort(){
        HttpServerMain server = new HttpServerMain();
        String[] commands = new String[2];
        commands[0] = "-p";
        commands[1] = "5050";
        server.main(commands);
        assertEquals(server.portNumber(), 5050);
    }

    @Test
    public void startServerWithSpecifiedPublicDir(){
        HttpServerMain server = new HttpServerMain();
        String[] commands = new String[2];
        commands[0] = "-d";
        commands[1] = "/Users/sarahjohnston/Sarah/Server/public/";
        server.main(commands);
        assertEquals(server.publicClassPath(), "/Users/sarahjohnston/Sarah/Server/public/");
    }
}
