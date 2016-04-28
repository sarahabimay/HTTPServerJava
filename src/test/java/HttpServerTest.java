import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HttpServerTest {

    @Test
    public void verifyServerCreatedASocket() {
        int portNumber = 5050;
        HttpServer server = new HttpServer(portNumber);
        assertEquals(server.getLocalPort().get(), (Integer) portNumber);
    }
}
