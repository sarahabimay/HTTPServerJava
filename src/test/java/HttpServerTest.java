import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class HttpServerTest {
    private ExecutorServiceCreatorSpy executorServiceCreatorSpy;
    private HttpServer server;
    private HttpClientSocketSpy clientSocketSpy;
    private HttpServerSocketFake serverSocketFake;
    private Router router;

    @Before
    public void setUp() {
        router = new Router(routes());
        clientSocketSpy = new HttpClientSocketSpy();
        serverSocketFake = new HttpServerSocketFake(clientSocketSpy);
        executorServiceCreatorSpy = new ExecutorServiceCreatorSpy(1);
        server = new HttpServer(serverSocketFake, executorServiceCreatorSpy, router);
    }

    @Test
    public void serverCreatedAThreadPool() {
        server.serverUp();
        assertEquals(true, executorServiceCreatorSpy.hasThreadPoolBeenCreated());
    }

    @Test
    public void sentATaskToExecutorService() {
        ExecutorServiceSpy executorServiceSpy = new ExecutorServiceSpy();
        executorServiceCreatorSpy.setExecutorService(executorServiceSpy);
        server.serverUp();
        assertEquals(true, executorServiceSpy.hasSubmittedATask());
    }

    private List<Route> routes() {
        return new RoutesFactory().routes();
    }

    private class HttpServerSocketFake extends HttpServerSocket {
        private HttpClientSocket clientSocket;

        public HttpServerSocketFake(HttpClientSocket clientSocket) {
            super(null);
            this.clientSocket = clientSocket;
        }

        public Optional<HttpClientSocket> accept() {
            return Optional.of(clientSocket);
        }
    }

    private class HttpClientSocketSpy extends HttpClientSocket {
        public HttpClientSocketSpy() {
            super(null);
        }
    }
}
