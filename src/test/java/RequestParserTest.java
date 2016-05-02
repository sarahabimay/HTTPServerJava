import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertEquals;

public class RequestParserTest {
    private RequestParser requestParser;
    private ByteArrayInputStream inputStream;

    @Before
    public void setUp() {
        inputStream = new ByteArrayInputStream("GET / HTTP/1.1".getBytes());
        requestParser = new RequestParser();
    }

    @Test
    public void receiveRequestFromClientSocketInstream() {
        ClientSocketSpy socketSpy = new ClientSocketSpy(inputStream);
        requestParser.parseRequest(socketSpy);
        assertEquals(true, socketSpy.hasReadRequestFromInputStream());
    }

    @Test
    public void createParsedRequestObject() {
        ClientSocketSpy socketSpy = new ClientSocketSpy(inputStream);
        HTTPRequest HTTPRequest = requestParser.parseRequest(socketSpy);
        HTTPMethod expectedMethod = HTTPMethod.GET;
        assertEquals(expectedMethod, HTTPRequest.method());
    }
}
