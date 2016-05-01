import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class RequestParser {
    public HTTPRequest parseRequest(HttpClientSocket clientSocket) {
        Optional<BufferedReader> reader = clientSocket.getInputReader();
        return generateParsedRequest(reader);
    }

    private HTTPRequest generateParsedRequest(Optional<BufferedReader> reader) {
        HTTPRequest HTTPRequest = new HTTPRequest();
        HTTPRequest.addRequestLine(parseRequestLine(getHeader(reader)));
        return HTTPRequest;
    }

    private List<String> parseRequestLine(String header) {
        return new ArrayList<>(Arrays.asList(header.split(" ")));
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
}
