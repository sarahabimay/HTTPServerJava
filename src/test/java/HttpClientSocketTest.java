import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.Socket;
import java.util.Optional;

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
        assertEquals("GET / HTTP/1.1", requestHeader());
    }

    private String requestHeader() {
        Optional<BufferedReader> inputReader = clientSocket.getInputReader();
        return getHeader(inputReader);
    }

    private String getHeader(Optional<BufferedReader> reader) {
        if (reader.isPresent()) {
            BufferedReader in = reader.get();
            try {
                return in.readLine();
            } catch (IOException e) {
            }
        }
        return "";
    }
    @Test
    public void sendResponseToClientSocket() {
        clientSocket.sendResponse("HTTP/1.1 404 Not Found");
        assertEquals(true, socketFake.hasBeenAskedForOutputStream());
    }

    @Test
    public void sendHttpResponseToClientSocket() {
        clientSocket.sendResponse(new HttpResponseSpy());
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
