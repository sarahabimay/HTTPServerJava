import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertEquals;

public class ClientRequestProcessorServiceTest {
    private ClientSocketSpy clientSocketSpy;
    private RequestParserSpy requestParserSpy;
    private ClientRequestProcessorService clientRequestProcessorService;
    private ByteArrayInputStream inputStream;

    @Before
    public void setUp() {
        inputStream = new ByteArrayInputStream(buildGETRequestLine().getBytes());
        clientSocketSpy = new ClientSocketSpy(inputStream);
        requestParserSpy = new RequestParserSpy(new HTTPRequestSpy());
        clientRequestProcessorService = new ClientRequestProcessorService(
                clientSocketSpy,
                requestParserSpy,
                new RouteFake());
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

    private String buildGETRequestLine() {
        return new StringBuilder()
                .append(HTTPMethod.GET.method())
                .append(" ")
                .append(HTTPRequestURI.INDEX.uri())
                .append(" ")
                .append(HTTPVersion.HTTP_1_1.version())
                .toString();
    }
}
