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
            submitRequestToBeProcessed(serverSocket.get().accept());
        }
    }

    private void submitRequestToBeProcessed(Optional<HttpClientSocket> clientSocket) {
        ExecutorService executorService;
        if ((executorService = executorServiceCreator.create()) != null) {
            executorService.submit(() -> {
                ClientRequestProcessorService requestProcessorService =
                        new ClientRequestProcessorService(clientSocket.get(), new RequestParser(), router);
                requestProcessorService.process();
            });
            executorService.shutdown();
        }
    }
}
