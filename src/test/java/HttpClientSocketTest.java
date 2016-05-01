import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.Socket;

import static org.junit.Assert.assertEquals;

public class HttpClientSocketTest {
    private JavaSocketFake socketFake;
    private HttpClientSocket clientSocket;

    @Before
    public void setUp() {
        socketFake = new JavaSocketFake();
        clientSocket = new HttpClientSocket(socketFake);
    }

    @Test
    public void socketInputReaderExists() {
        assertEquals(BufferedReader.class, clientSocket.getInputReader().get().getClass());
    }

    @Test
    public void receiveRequestMessage() {
        assertEquals("GET / HTTP/1.1", clientSocket.request());
    }

    @Test
    public void sendResponseToClientSocket() {
        clientSocket.sendResponse("HTTP/1.1 404 Not Found");
        assertEquals(true, socketFake.hasBeenAskedForOutputStream());
    }

    private class JavaSocketFake extends Socket {
        private boolean hasBeenAskedForOutputStream = false;

        @Override
        public InputStream getInputStream() {
            return new ByteArrayInputStream("GET / HTTP/1.1".getBytes());
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            hasBeenAskedForOutputStream = true;
            return new ByteArrayOutputStream();
        }

        public boolean hasBeenAskedForOutputStream() {
            return hasBeenAskedForOutputStream;
        }
    }
}
