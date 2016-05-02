package server;

import request.RequestParser;
import router.RouteProcessor;

import java.util.Optional;
import java.util.concurrent.ExecutorService;

public class HttpServer {
    private Optional<HttpServerSocket> serverSocket = Optional.empty();
    private ExecutorServiceCreator executorServiceCreator;
    private RouteProcessor routeProcessor;

    public HttpServer(HttpServerSocket serverSocket, ExecutorServiceCreator executorServiceCreator, RouteProcessor routeProcessor) {
        this.serverSocket = Optional.of(serverSocket);
        this.executorServiceCreator = executorServiceCreator;
        this.routeProcessor = routeProcessor;
    }

    public void serverUp() {
        if (serverSocket.isPresent()) {
            submitRequestToBeProcessed(serverSocket.get().accept());
        }
    }

    private void submitRequestToBeProcessed(Optional<HttpClientSocket> clientSocket) {
        ExecutorService executorService;
        if ((executorService = executorServiceCreator.create()) != null) {
            executorService.submit(() -> {
                ClientRequestProcessorService requestProcessorService =
                        new ClientRequestProcessorService(clientSocket.get(), new RequestParser(), routeProcessor);
                requestProcessorService.process();
            });
            executorService.shutdown();
        }
    }
}
