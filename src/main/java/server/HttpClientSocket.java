package server;

import response.HTTPResponse;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Optional;

public class HttpClientSocket {
    private Socket socket;

    public HttpClientSocket(Socket socket) {
        this.socket = socket;
    }

    public Optional<BufferedReader> getInputReader() {
        try {
            return Optional.of(new BufferedReader( new InputStreamReader(socket.getInputStream())));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public void sendResponse(HTTPResponse response) {
        try {
            OutputStream os = socket.getOutputStream();
            os.write(response.buildResponse());
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendResponse(String response) {
        try {
            OutputStream os = socket.getOutputStream();
            os.write(formatResponse(response, new byte[0]));
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] formatResponse(String response, byte[] bodyBytes) {
        ByteBuffer entireResponse = ByteBuffer.allocate(response.getBytes().length + bodyBytes.length);
        entireResponse.put(response.getBytes());
        entireResponse.put(bodyBytes);
        return entireResponse.array();
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
