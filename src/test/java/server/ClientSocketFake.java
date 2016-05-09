package server;

import response.HTTPResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;

public class ClientSocketFake extends HttpClientSocket {
    private final InputStream inputStream;

    public ClientSocketFake(InputStream inputStream) {
        super(null);
        this.inputStream = inputStream;
    }

    @Override
    public Optional<BufferedReader> getInputReader() {
        if (inputStream == null) {
            return Optional.empty();
        }
        return Optional.of(new BufferedReaderFake(new InputStreamReader(inputStream)));
    }

    @Override
    public void sendResponse(HTTPResponse response) {
        super.sendResponse(response);
    }

    @Override
    public void sendResponse(String response) {
        super.sendResponse(response);
    }

    @Override
    public byte[] formatResponse(String response, byte[] bodyBytes) {
        return super.formatResponse(response, bodyBytes);
    }

    private class BufferedReaderFake extends BufferedReader {
        public BufferedReaderFake(InputStreamReader inputStreamReader) {
            super(inputStreamReader);
        }

        @Override
        public String readLine() throws IOException {
            String line;
            if ((line = super.readLine()).equals("ERRONEOUS INPUT")) {
                throw new IOException();
            }
            return line;
        }

        @Override
        public int read(char[] cbuf, int off, int len) throws IOException {
            throw new IOException();
        }
    }
}
