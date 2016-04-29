import org.junit.Test;

import java.net.Socket;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class HttpRequestTest {
    private final String GET_FOOBAR_REQUEST = "GET /foobar HTTP/1.1";
    @Test
    public void genericRequestReceived() {
        ServerSocketFake serverSocketFake = new ServerSocketFake();
        HTTPRequest request = new HTTPRequest(Optional.of(serverSocketFake.accept()));
        assertEquals(GET_FOOBAR_REQUEST, request.request());
    }

    @Test
    public void generateFourOhFourResponseForRequest() {
        ServerSocketFake serverSocketFake = new ServerSocketFake();
        HTTPRequest request = new HTTPRequest(Optional.of(serverSocketFake.accept()));
        assertEquals("HTTP/1.1 404 Not Found", request.response());
    }

    public class ServerSocketFake {
        public HttpClientSocketFake accept() {
            return new HttpClientSocketFake(new Socket());
        }
    }

    private class HttpClientSocketFake extends HttpClientSocket {
        public HttpClientSocketFake(Socket socket) {
            super(null);
        }

        public String request() {
            return GET_FOOBAR_REQUEST;
        }
    }
}
