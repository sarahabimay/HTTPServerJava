import java.util.Optional;

public class HttpServer {
    private Optional<HttpServerSocket> serverSocket = Optional.empty();

    public HttpServer(HttpServerSocket serverSocket) {
        this.serverSocket = Optional.of(serverSocket);
    }

    public void serverUp() {
        if(serverSocket.isPresent()) {
            System.out.println("Start waiting for Requests");
            HTTPRequest httpRequest = new HTTPRequest(serverSocket.get().accept());
        }
    }

    public Optional<Integer> getLocalPort() {
        return serverSocket.isPresent()? Optional.of(serverSocket.get().getLocalPort()) : Optional.empty();
    }
}
