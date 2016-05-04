package request;

import response.EntityHeaderFields;
import server.HttpClientSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

import static response.EntityHeaderFields.*;
import static response.EntityHeaderFields.CONTENT_LENGTH;

public class RequestParser {
    private static final String COLON = ":";
    private final String COMMA = ",";

    public HTTPRequest parseRequest(HttpClientSocket clientSocket) {
        Optional<BufferedReader> reader = clientSocket.getInputReader();
        String requestLine = getRequestLine(reader);
        Map<EntityHeaderFields, List<String>> headers = parseRequestHeaders(reader);
        return buildHTTPRequest(reader, requestLine, headers);
    }

    private HTTPRequest buildHTTPRequest(Optional<BufferedReader> reader, String requestLine, Map<EntityHeaderFields, List<String>> headers) {
        return new HTTPRequest()
                    .addRequestLine(parseRequestLine(requestLine))
                    .addRequestHeader(headers)
                    .addBody(getBody(reader, contentLengthOfBody(headers)));
    }

    private String getRequestLine(Optional<BufferedReader> reader) {
        if (reader.isPresent()) {
            BufferedReader in = reader.get();
            try {
                return in.readLine();
            } catch (IOException e) {
            }
        }
        return "";
    }

    private List<String> parseRequestLine(String header) {
        return new ArrayList<>(Arrays.asList(header.split(" ")));
    }

    private Map<EntityHeaderFields, List<String>> parseRequestHeaders(Optional<BufferedReader> reader) {
        if (reader.isPresent()) {
            BufferedReader in = reader.get();
            try {
                Map<EntityHeaderFields, List<String>> headers = new HashMap<>();
                String line;
                while((line = in.readLine()) != null && !line.isEmpty() ) {
                    parseRequestHeader(headers, line);
                }
                return headers;
            } catch (IOException e) {
            }
        }
        return new HashMap<>();
    }

    private void parseRequestHeader(Map<EntityHeaderFields, List<String>> headers, String line) {
        String[] header = parseFieldAndValue(line);
        EntityHeaderFields fieldName = lookupHeaderField(header[0]);
        if (!fieldName.equals(EntityHeaderFields.INVALID_HEADER)) {
            headers.put(fieldName, parseHeaderValues(header[1]));
        }
    }

    private List<String> parseHeaderValues(String headerValues) {
        String[] array = headerValues.split(COMMA);
        array = Arrays.stream(array).map(String::trim).toArray(String[]::new);
        return new ArrayList<>(Arrays.asList(array));
    }

    private String[] parseFieldAndValue(String field) {
        return field.split(COLON);
    }

    private int contentLengthOfBody(Map<EntityHeaderFields, List<String>> headers) {
        if (headers != null && !headers.isEmpty()) {
            List<String> contentLengthField = findHeaderField(headers, CONTENT_LENGTH);
            if (contentLengthField != null) {
                return Integer.parseInt(contentLengthField.get(0));
            }
        }
        return 0;
    }

    private List<String> findHeaderField(Map<EntityHeaderFields, List<String>> headers, EntityHeaderFields headerField) {
        return headers.get(headerField);
    }

    private String getBody(Optional<BufferedReader> reader, int lengthOfBodyContent) {
        if (reader.isPresent()) {
            char[] body = new char[lengthOfBodyContent];
            try {
                reader.get().read(body, 0, lengthOfBodyContent);
                return String.valueOf(body);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return "";
    }
}
