import org.junit.Before;
import org.junit.Test;
import request.*;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertEquals;

public class RequestParserTest {
    private RequestParser requestParser;
    private ByteArrayInputStream inputStream;
    private ClientSocketSpy socketSpy;

    @Before
    public void setUp() {
        requestParser = new RequestParser();
        inputStream = new ByteArrayInputStream(buildGETRequestLine().getBytes());
        socketSpy = new ClientSocketSpy(inputStream);
    }

    @Test
    public void receiveRequestFromClientSocketInstream() {
        requestParser.parseRequest(socketSpy);
        assertEquals(true, socketSpy.hasReadRequestFromInputStream());
    }

    @Test
    public void createParsedRequestObject() {
        HTTPRequest HTTPRequest = requestParser.parseRequest(socketSpy);
        HTTPMethod expectedMethod = HTTPMethod.GET;
        assertEquals(expectedMethod, HTTPRequest.method());
    }

    private String buildGETRequestLine() {
        return new StringBuilder()
                .append(HTTPMethod.GET)
                .append(" ")
                .append(HTTPRequestURI.INDEX)
                .append(" ")
                .append(HTTPVersion.HTTP_1_1)
                .toString();
    }
}
