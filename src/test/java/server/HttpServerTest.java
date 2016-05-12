package server;

import configuration.Configuration;
import exceptions.ServerErrorHandler;
import org.junit.Before;
import org.junit.Test;
import request.RequestParser;
import router.RouteProcessor;
import router.RoutesFactory;
import router.URIProcessorStub;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class HttpServerTest {
    private ExecutorServiceCreatorSpy executorServiceCreatorSpy;
    private HttpServer server;
    private HttpClientSocketStub clientSocketSpy;
    private HttpServerSocketFake serverSocketFake;
    private RouteProcessor routeProcessor;
    private RequestParser requestParser;
    private Configuration configuration;

    @Before
    public void setUp() {
        clientSocketSpy = new HttpClientSocketStub();
        serverSocketFake = new HttpServerSocketFake(clientSocketSpy);
        executorServiceCreatorSpy = new ExecutorServiceCreatorSpy(1);
        ServerErrorHandler errorHandler = new ServerErrorHandler();
        requestParser = new RequestParser(errorHandler);
        configuration = new Configuration();
        routeProcessor = new RouteProcessor(
                new RoutesFactory(new URIProcessorStub(), configuration),
                configuration,
                errorHandler);
        server = new HttpServer(serverSocketFake, executorServiceCreatorSpy, requestParser, routeProcessor);
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

    private class HttpClientSocketStub extends HttpClientSocket {
        public HttpClientSocketStub() {
            super(null);
        }
    }
}
