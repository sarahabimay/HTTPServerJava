package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Optional;

public class HttpServerSocket {
    private ServerSocket serverSocket;

    public HttpServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public Optional<HttpClientSocket> accept() {
        try {
            return Optional.of(new HttpClientSocket(serverSocket.accept()));
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
