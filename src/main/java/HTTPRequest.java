import java.util.Optional;

public class HTTPRequest {
    private String request;
    private Optional<HttpClientSocket> clientSocket;

    public HTTPRequest(Optional<HttpClientSocket> clientSocket) {
        this.clientSocket = clientSocket;
        this.request = clientSocket.get().request();
    }

    public String request() {
        return clientSocket.get().request();
    }
}
