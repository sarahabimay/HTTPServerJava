import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
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

    @Test
    public void responseSentToClient() {
        int portNumber = 5062;
        try {
            HttpServerSocketFake serverSocket = new HttpServerSocketFake(new ServerSocket(portNumber));
            HttpServer server = new HttpServer(serverSocket);
            server.serverUp();
            HttpClientSocketSpy clientSocket = (HttpClientSocketSpy)serverSocket.getClientSocket();
            assertEquals(true, clientSocket.hasResponseBeenReceived());
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
        private boolean hasResponseBeenReceived = false;
        private List<String> dummyRequests = new ArrayList<>();
        private List<Boolean> closeConnection = new ArrayList<>();

        public HttpClientSocketSpy(Socket socket) {
            super(null);
            dummyRequests.add("");
            closeConnection.add(false);
            closeConnection.add(true);
        }

        public String request() {
            hasRequestBeenCalled = true;
            return dummyRequests.size() == 0 ? null : dummyRequests.remove(0);
        }

        public void sendResponse(String response){
            hasResponseBeenReceived = true;
        }

        public boolean isClosed(){
            return closeConnection.remove(0);
        }

        public boolean hasRequestBeenCalled() {
            return hasRequestBeenCalled;
        }

        public boolean hasResponseBeenReceived() {
            return hasResponseBeenReceived;
        }
    }
}
