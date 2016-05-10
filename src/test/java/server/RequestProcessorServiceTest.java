package server;

import exceptions.ServerErrorHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import request.*;
import router.RouteProcessorSpy;
import router.RouterStub;
import router.URIProcessorStub;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertEquals;

public class RequestProcessorServiceTest {
    private ClientSocketSpy clientSocketSpy;
    private RequestParserSpy requestParserSpy;
    private RequestProcessorService requestProcessorService;
    private ByteArrayInputStream inputStream;
    private RouteProcessorSpy routeProcessorSpy;

    @Before
    public void setUp() {
        inputStream = new ByteArrayInputStream(buildGETRequestLine().getBytes());
        clientSocketSpy = new ClientSocketSpy(inputStream);
        requestParserSpy = new RequestParserSpy(new HTTPRequestFake());
        routeProcessorSpy = new RouteProcessorSpy(
                new RouterStub(), new URIProcessorStub(), new ServerErrorHandler());
        requestProcessorService = new RequestProcessorService(
                clientSocketSpy,
                requestParserSpy,
                routeProcessorSpy);
    }

    @Test
    public void requestHasBeenReceivedFromClient() {
        requestProcessorService.process();
        assertEquals(true, clientSocketSpy.hasReadRequestFromInputStream());
    }

    @Test
    public void requestHasBeenParsed() {
        requestProcessorService.process();
        Assert.assertEquals(true, requestParserSpy.hasRequestBeenParsed());
    }

    @Test
    public void responseHasBeenCreated() {
        requestProcessorService.process();
        assertEquals(true, routeProcessorSpy.hasRequestBeenParsed());
    }

    @Test
    public void responseSentToClient() {
        requestProcessorService.process();
        assertEquals(true, clientSocketSpy.hasSentResponseToClient());
    }

    private String buildGETRequestLine() {
        return new StringBuilder()
                .append(HTTPMethod.GET.method())
                .append(" ")
                .append(HTTPResource.INDEX.uri())
                .append(" ")
                .append(HTTPVersion.HTTP_1_1.version())
                .toString();
    }
}
