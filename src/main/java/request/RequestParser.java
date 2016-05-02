package request;

import server.HttpClientSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class RequestParser {
    public HTTPRequest parseRequest(HttpClientSocket clientSocket) {
        return generateParsedRequest(clientSocket.getInputReader());
    }

    private HTTPRequest generateParsedRequest(Optional<BufferedReader> reader) {
        HTTPRequest request = new HTTPRequest();
        request.addRequestLine(parseRequestLine(getHeader(reader)));
        return request;
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
