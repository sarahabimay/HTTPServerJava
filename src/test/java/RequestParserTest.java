import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertEquals;

public class RequestParserTest {
    private RequestParser requestParser;
    private ByteArrayInputStream inputStream;
    private ClientSocketSpy socketSpy;

    @Before
    public void setUp() {
        String requestLine = HTTPMethod.GET.method() + " / HTTP/1.1";
        inputStream = new ByteArrayInputStream(requestLine.getBytes());
        requestParser = new RequestParser();
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
}
