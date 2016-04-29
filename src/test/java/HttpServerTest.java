import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class HttpServerTest {

    @Test
    public void verifyServerCreatedASocket() {
        int portNumber = 5050;
        try {
            HttpServerSocket serverSocket = new HttpServerSocket(new ServerSocket(portNumber));
            HttpServer server = new HttpServer(serverSocket);
            assertEquals((Integer) portNumber, server.getLocalPort().get());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void clientSendsARequest() {
        int portNumber = 5061;
        try {
            HttpServerSocketFake serverSocket = new HttpServerSocketFake(new ServerSocket(portNumber));
            HttpServer server = new HttpServer(serverSocket);
            server.serverUp();
            HttpClientSocketSpy clientSocket = (HttpClientSocketSpy)serverSocket.getClientSocket();
            assertEquals(true, clientSocket.hasRequestBeenCalled());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class HttpServerSocketFake extends HttpServerSocket{
        private Optional<HttpClientSocket> clientSocket;

        public HttpServerSocketFake(ServerSocket serverSocket) {
            super(serverSocket);
        }

        public Optional<HttpClientSocket> accept() {
            this.clientSocket = Optional.of(new HttpClientSocketSpy(new Socket()));
            return clientSocket;
        }

        public HttpClientSocket getClientSocket(){
            return clientSocket.get();
        }
    }

    private class HttpClientSocketSpy extends HttpClientSocket {
        private boolean hasRequestBeenCalled = false;

        public HttpClientSocketSpy(Socket socket) {
            super(null);
        }

        public String request() {
            hasRequestBeenCalled = true;
            return "";
        }

        public boolean hasRequestBeenCalled() {
            return hasRequestBeenCalled;
        }
    }
}
