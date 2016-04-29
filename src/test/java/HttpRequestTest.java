import org.junit.Test;

import java.net.Socket;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class HttpRequestTest {
    @Test
    public void genericRequestReceived() {
        ServerSocketFake serverSocketFake = new ServerSocketFake();
        HTTPRequest request = new HTTPRequest(Optional.of(serverSocketFake.accept()));
        assertEquals("GET / HTTP/1.1", request.request());
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
            return "GET / HTTP/1.1";
        }
    }
}
