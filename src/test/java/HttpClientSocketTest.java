import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.Socket;

import static org.junit.Assert.assertEquals;

public class HttpClientSocketTest {
    private JavaSocketFake socket;
    private HttpClientSocket clientSocket;

    @Before
    public void setUp() {
        socket = new JavaSocketFake();
        clientSocket = new HttpClientSocket(socket);
    }

    @Test
    public void socketInputReaderExists() {
        assertEquals(BufferedReader.class, clientSocket.getInputReader().get().getClass());
    }

    @Test
    public void socketOutputWriterExists() {
        assertEquals(PrintWriter.class, clientSocket.getOutputWriter().get().getClass());
    }

    @Test
    public void receiveRequestMessage() {
        assertEquals("GET / HTTP/1.1", clientSocket.request());
    }

    @Test
    public void sendResponseToClientSocket() {
        clientSocket.sendResponse("HTTP/1.1 404 Not Found");
        assertEquals("HTTP/1.1 404 Not Found", clientSocket.lastResponse());
    }

    private class JavaSocketFake extends Socket {
        @Override
        public InputStream getInputStream() {
            return new ByteArrayInputStream("GET / HTTP/1.1".getBytes());
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            return new ByteArrayOutputStream();
        }
    }
}
