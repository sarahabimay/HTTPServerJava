package server;

import request.RequestParser;
import router.RouteProcessor;

import java.util.Optional;
import java.util.concurrent.ExecutorService;

public class HttpServer {
    private Optional<HttpServerSocket> serverSocket = Optional.empty();
    private ExecutorServiceCreator executorServiceCreator;
    private RouteProcessor routeProcessor;
    private final RequestParser requestParser;

    public HttpServer(HttpServerSocket serverSocket, ExecutorServiceCreator executorServiceCreator, RequestParser requestParser, RouteProcessor routeProcessor) {
        this.serverSocket = Optional.of(serverSocket);
        this.executorServiceCreator = executorServiceCreator;
        this.requestParser = requestParser;
        this.routeProcessor = routeProcessor;
    }

    public void serverUp() {
        if (serverSocket.isPresent()) {
            submitRequestToBeProcessed(serverSocket.get().accept());
        }
    }

    private void submitRequestToBeProcessed(Optional<HttpClientSocket> clientSocket) {
        if (clientSocket.isPresent()) {
            ExecutorService executorService;
            if ((executorService = executorServiceCreator.create()) != null) {
                executorService.submit(() -> {
                    RequestProcessorService requestProcessorService = requestProcessorService(clientSocket.get());
                    requestProcessorService.process();
                });
                executorService.shutdown();
            }
        }
    }

    private RequestProcessorService requestProcessorService(HttpClientSocket clientSocket) {
        return new RequestProcessorService( clientSocket, requestParser, routeProcessor);
    }
}
