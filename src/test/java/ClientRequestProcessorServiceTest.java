import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertEquals;

public class ClientRequestProcessorServiceTest {
    private ClientSocketSpy clientSocketSpy;
    private RequestParserSpy requestParserSpy;
    private HTTPRequestSpy fakeHTTPRequestSpy;
    private RouterSpy routerSpy;
    private ClientRequestProcessorService clientRequestProcessorService;
    private ByteArrayInputStream inputStream;

    @Before
    public void setUp() {
        inputStream = new ByteArrayInputStream("GET / HTTP/1.1".getBytes());
        clientSocketSpy = new ClientSocketSpy(inputStream);
        fakeHTTPRequestSpy = new HTTPRequestSpy();
        requestParserSpy = new RequestParserSpy(fakeHTTPRequestSpy);
        routerSpy = new RouterSpy(new Route("GET", "/", "HTTP/1.1"));
        clientRequestProcessorService =
                new ClientRequestProcessorService(
                        clientSocketSpy,
                        requestParserSpy,
                        routerSpy);
    }

    @Test
    public void requestHasBeenReceivedFromClient() {
        clientRequestProcessorService.process();
        assertEquals(true, clientSocketSpy.hasReadRequestFromInputStream());
    }

    @Test
    public void requestHasBeenParsed() {
        clientRequestProcessorService.process();
        assertEquals(true, requestParserSpy.hasRequestBeenParsed());
    }

    @Test
    public void responseSentToClient() {
        clientRequestProcessorService.process();
        assertEquals(true, clientSocketSpy.hasSentResponseToClient());
    }
}
