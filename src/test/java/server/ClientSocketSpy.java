package server;

import response.HTTPResponse;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;

public class ClientSocketSpy extends HttpClientSocket {
    private InputStream inputStream;
    private boolean hasReadRequestFromInputStream = false;
    private boolean hasSentResponseToClient = false;

    public ClientSocketSpy(InputStream inputStream) {
        super(null);
        this.inputStream = inputStream;
    }

    @Override
    public Optional<BufferedReader> getInputReader() {
        hasReadRequestFromInputStream = true;
        return Optional.of(new BufferedReader(new InputStreamReader(inputStream)));
    }

    @Override
    public void sendResponse(HTTPResponse response) {
        hasSentResponseToClient = true;
    }

    @Override
    public void close() {
    }

    public boolean hasReadRequestFromInputStream() {
        return hasReadRequestFromInputStream;
    }

    public boolean hasSentResponseToClient() {
        return hasSentResponseToClient;
    }
}
