package server;

import org.junit.Before;
import org.junit.Test;
import request.HTTPMethod;
import request.HTTPRequestURI;
import request.HTTPVersion;
import response.HTTPStatusCode;
import response.HttpResponseSpy;

import java.io.*;
import java.net.Socket;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class HttpClientSocketTest {
    private JavaSocketFake socketFake;
    private HttpClientSocket clientSocket;
    private String requestLine;

    @Before
    public void setUp() {
        socketFake = new JavaSocketFake();
        clientSocket = new HttpClientSocket(socketFake);
        requestLine = HTTPMethod.GET + " " + HTTPRequestURI.INDEX + " " + HTTPVersion.HTTP_1_1;
    }

    @Test
    public void socketInputReaderExists() {
        assertEquals(BufferedReader.class, clientSocket.getInputReader().get().getClass());
    }

    @Test
    public void receiveRequestMessage() {
        assertEquals(requestLine, requestLineFromClient());
    }

    @Test
    public void sendResponseToClientSocket() {
        clientSocket.sendResponse(statusNotFoundResponse());
        assertEquals(true, socketFake.hasBeenAskedForOutputStream());
    }

    @Test
    public void sendHttpResponseToClientSocket() {
        clientSocket.sendResponse(new HttpResponseSpy());
        assertEquals(true, socketFake.hasBeenAskedForOutputStream());
    }

    private String statusNotFoundResponse() {
        return new StringBuilder()
                .append(HTTPVersion.HTTP_1_1.version())
                .append(HTTPStatusCode.NOT_FOUND.statusCode())
                .append(" ")
                .append(HTTPStatusCode.NOT_FOUND.reason())
                .toString();
    }

    private String requestLineFromClient() {
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

    private class JavaSocketFake extends Socket {
        private boolean hasBeenAskedForOutputStream = false;

        @Override
        public InputStream getInputStream() {
            return new ByteArrayInputStream(requestLine.getBytes());
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
