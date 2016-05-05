package server;

import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.Assert.assertEquals;

public class HttpServerSocketTest {
    @Test
    public void serverSocketReceivesClientConnection() {
        HttpServerSocket serverSocket;
        try {
            JavaServerSocketSpy javaServerSocket = new JavaServerSocketSpy();
            serverSocket = new HttpServerSocket(javaServerSocket);
            serverSocket.accept();
            assertEquals(true, javaServerSocket.hasServerSocketReceivedConnection());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class JavaServerSocketSpy extends ServerSocket {
        private boolean hasServerSocketReceivedConnection = true;

        public JavaServerSocketSpy() throws IOException {
        }

        @Override
        public Socket accept(){
            hasServerSocketReceivedConnection = true;
            return new Socket();
        }

        public boolean hasServerSocketReceivedConnection() {
            return hasServerSocketReceivedConnection;
        }
    }
}
