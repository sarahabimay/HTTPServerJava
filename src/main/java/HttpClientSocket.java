import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Optional;

public class HttpClientSocket {
    private Socket socket;

    public HttpClientSocket(Socket socket) {
        this.socket = socket;
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
}
