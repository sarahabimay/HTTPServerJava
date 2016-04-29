import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Optional;

public class HttpClientSocket {
    private Socket socket;
    private String lastResponse;

    public HttpClientSocket(Socket socket) {
        this.socket = socket;
        this.lastResponse = "";
    }

    public Optional<BufferedReader> getInputReader() {
        try {
            return Optional.of(new BufferedReader(
                    new InputStreamReader(socket.getInputStream())));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public Optional<PrintWriter> getOutputWriter() {
        try {
            return Optional.of(new PrintWriter(socket.getOutputStream()));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public String request() {
        if (getInputReader().isPresent()) {
            BufferedReader reader = getInputReader().get();
            try {
                return reader.readLine();
            } catch (IOException e) {
                return "";
            }
        }
        return "";
    }

    public boolean isClosed() {
        return socket.isClosed();
    }

    public void sendResponse(String response) {
        this.lastResponse = response;
        PrintWriter writer = getOutputWriter().get();
        writer.println(response);
        writer.close();
    }

    public String lastResponse() {
        return lastResponse;
    }
}
