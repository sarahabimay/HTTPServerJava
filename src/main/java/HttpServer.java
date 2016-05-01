import java.util.Optional;
import java.util.concurrent.ExecutorService;

public class HttpServer {
    private Optional<HttpServerSocket> serverSocket = Optional.empty();
    private ExecutorServiceCreator executorServiceCreator;
    private Router router;

    public HttpServer(HttpServerSocket serverSocket, ExecutorServiceCreator executorServiceCreator, Router router) {
        this.serverSocket = Optional.of(serverSocket);
        this.executorServiceCreator = executorServiceCreator;
        this.router = router;
    }

    public void serverUp() {
        if (serverSocket.isPresent()) {
            submitRequestToThreadpool(serverSocket.get().accept());
        }
    }

    private void submitRequestToThreadpool(Optional<HttpClientSocket> clientSocket) {
        ExecutorService executorService;
        if ((executorService = executorServiceCreator.create()) != null) {
            executorService.submit(() -> {
                HTTPRequest httpRequest = new HTTPRequest(router, clientSocket);
                clientSocket.get().sendResponse(httpRequest.response());
                clientSocket.get().close();
            });
            executorService.shutdown();
        }
    }
}
