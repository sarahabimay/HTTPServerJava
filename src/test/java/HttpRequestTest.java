import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;

import java.net.Socket;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class HttpRequestTest {
    private final String GET_FOOBAR_REQUEST = "GET /foobar HTTP/1.1";
    private final String GET_INDEX_REQUEST = "GET / HTTP/1.1";
    private final String PUT_REQUEST = "PUT /form HTTP/1.1";
    private final String POST_REQUEST = "POST /form HTTP/1.1";
    private final String FOUR_OH_FOUR_RESPONSE = "HTTP/1.1 404 Not Found";
    private final String STATUS_OK_RESPONSE = "HTTP/1.1 200 OK";
    private ServerSocketFake serverSocketFake;

    @Before
    public void setUp() {
        serverSocketFake = new ServerSocketFake();
    }

    @Test
    public void genericRequestReceived() {
        serverSocketFake.dummyRequests(new ArrayList<>(Arrays.asList(GET_FOOBAR_REQUEST)));
        HTTPRequest request = new HTTPRequest(new Router(routes()), Optional.of(serverSocketFake.accept()));
        Map<String, String> parsedRequest = ImmutableMap.of("method", "GET", "path", "/foobar", "version", "HTTP/1.1");
        assertEquals(parsedRequest, request.request());
    }

    @Test
    public void generateFourOhFourResponseForRequest() {
        serverSocketFake.dummyRequests(new ArrayList<>(Arrays.asList(GET_FOOBAR_REQUEST)));
        HTTPRequest request = new HTTPRequest(new Router(routes()), Optional.of(serverSocketFake.accept()));
        assertEquals(FOUR_OH_FOUR_RESPONSE, request.response());
    }

    @Test
    public void simpleGETReceivesTwoHundred() {
        serverSocketFake.dummyRequests(new ArrayList<>(Arrays.asList(GET_INDEX_REQUEST)));
        HTTPRequest request = new HTTPRequest(new Router(routes()), Optional.of(serverSocketFake.accept()));
        assertEquals(STATUS_OK_RESPONSE, request.response());
    }

    @Test
    public void simplePUTReceivesTwoHundred() {
        serverSocketFake.dummyRequests(new ArrayList<>(Arrays.asList(PUT_REQUEST)));
        HTTPRequest request = new HTTPRequest(new Router(routes()), Optional.of(serverSocketFake.accept()));
        assertEquals(STATUS_OK_RESPONSE, request.response());
    }

    @Test
    public void simplePOSTReceivesTwoHundred() {
        serverSocketFake.dummyRequests(new ArrayList<>(Arrays.asList(POST_REQUEST)));
        HTTPRequest request = new HTTPRequest(new Router(routes()), Optional.of(serverSocketFake.accept()));
        assertEquals(STATUS_OK_RESPONSE, request.response());
    }

    private List<Route> routes() {
        return new RoutesFactory().routes();
    }

    public class ServerSocketFake {
        private List<String> dummyRequests = new ArrayList<>();

        public void dummyRequests(List<String> requests){
            this.dummyRequests = requests;
        }

        public HttpClientSocketFake accept() {
            HttpClientSocketFake clientSocket = new HttpClientSocketFake(new Socket());
            clientSocket.dummyRequests(dummyRequests);
            return clientSocket;
        }
    }

    private class HttpClientSocketFake extends HttpClientSocket {
        private List<String> dummyRequests = new ArrayList<>();

        public HttpClientSocketFake(Socket socket) {
            super(null);
        }

        public String request() {
            return dummyRequests.remove(0);
        }

        public void dummyRequests(List<String> dummyRequests) {
            this.dummyRequests = dummyRequests;
        }
    }
}
