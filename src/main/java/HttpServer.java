import java.util.Optional;

public class HttpServer {
    private Router router;
    private Optional<HttpServerSocket> serverSocket = Optional.empty();

    public HttpServer(HttpServerSocket serverSocket, Router router) {
        this.serverSocket = Optional.of(serverSocket);
        this.router = router;
    }

    public void serverUp() {
        if (serverSocket.isPresent()) {
            Optional<HttpClientSocket> clientSocket = serverSocket.get().accept();
            if (clientSocket.isPresent()) {
                while (!clientSocket.get().isClosed()) {
                    HTTPRequest httpRequest = new HTTPRequest(router, clientSocket);
                    clientSocket.get().sendResponse(httpRequest.response());
                }
            }
        }
    }

    public Optional<Integer> getLocalPort() {
        return serverSocket.isPresent() ? Optional.of(serverSocket.get().getLocalPort()) : Optional.empty();
    }
}
