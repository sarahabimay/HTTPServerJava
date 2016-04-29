import java.util.Optional;

public class HTTPRequest {
    private String request;

    public HTTPRequest(Optional<HttpClientSocket> clientSocket) {
        this.request = clientSocket.get().request();
    }

    public String request() {
        return request;
    }

    public String response() {
        return "HTTP/1.1 404 Not Found";
    }
}
