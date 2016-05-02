import org.junit.Before;
import org.junit.Test;
import request.HTTPMethod;
import request.HTTPRequestURI;
import request.HTTPVersion;
import server.ClientRequestProcessorService;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertEquals;

public class ClientRequestProcessorServiceTest {
    private ClientSocketSpy clientSocketSpy;
    private RequestParserSpy requestParserSpy;
    private ClientRequestProcessorService clientRequestProcessorService;
    private ByteArrayInputStream inputStream;
    private RouteProcessorSpy routeProcessorSpy;

    @Before
    public void setUp() {
        inputStream = new ByteArrayInputStream(buildGETRequestLine().getBytes());
        clientSocketSpy = new ClientSocketSpy(inputStream);
        requestParserSpy = new RequestParserSpy(new HTTPRequestSpy());
        routeProcessorSpy = new RouteProcessorSpy(new RouterStub());
        clientRequestProcessorService = new ClientRequestProcessorService(
                clientSocketSpy,
                requestParserSpy,
                routeProcessorSpy);
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
    public void responseHasBeenCreated() {
        clientRequestProcessorService.process();
        assertEquals(true, routeProcessorSpy.hasRequestBeenParsed());
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
