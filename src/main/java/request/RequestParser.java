package request;

import response.EntityHeaderFields;
import server.HttpClientSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

import static request.HTTPMethod.lookupMethod;
import static request.HTTPRequestURI.*;
import static request.HTTPVersion.*;
import static response.EntityHeaderFields.*;
import static response.EntityHeaderFields.CONTENT_LENGTH;

public class RequestParser {
    private static final String COLON = ":";
    private final String COMMA = ",";
    private final String QUESTION_MARK = "\\?";
    private final String SPACE = " ";
    private final String HTTP_METHOD = "method";
    private final String URI = "uri";
    private final String QUERY_PARAMETERS = "queryParameters";
    private final String HTTP_VERSION_1_1 = "version";

    public HTTPRequest parseRequest(HttpClientSocket clientSocket) {
        Optional<BufferedReader> reader = clientSocket.getInputReader();
        Map<String, String> parsedRequestLine = parseRequestLine(getRequestLine(reader));
        Map<EntityHeaderFields, List<String>> headers = parseRequestHeaders(reader);
        String body = getBody(reader, contentLengthOfBody(headers));
        return buildHTTPRequest(body, parsedRequestLine, headers);
    }

    private HTTPRequest buildHTTPRequest(String body, Map<String, String> requestLine, Map<EntityHeaderFields, List<String>> headers) {
        return new HTTPRequest()
                .addRequestLine(requestLine)
                .addRequestHeader(headers)
                .addBody(body);
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

    private Map<String, String> parseRequestLine(String header) {
        List<String> requestLine = new ArrayList<>(Arrays.asList(header.split(SPACE)));
        Map<String, String> requestLineFields = new HashMap<>();
        requestLineFields.put(HTTP_METHOD, lookupMethod(requestLine.get(0)).method());
        requestLineFields.putAll(parseURIAndQueryParameters(requestLine.get(1)));
        requestLineFields.put(HTTP_VERSION_1_1, lookupVersionName(requestLine.get(2)).version());
        return requestLineFields;
    }

    private Map<String, String> parseURIAndQueryParameters(String uriAndQueryFields) {
        Map<String, String> uriAndParams = new HashMap<>();
        List<String> uriAndParameters = separateURIFromQueryFields(uriAndQueryFields);
        uriAndParams.put(URI, lookupURIName(uriAndParameters.get(0)).uri());
        if (areQueryParameters(uriAndParameters)) {
            uriAndParams.put(QUERY_PARAMETERS, uriAndParameters.get(1));
        }
        return uriAndParams;
    }

    private ArrayList<String> separateURIFromQueryFields(String uriAndQueryParametersField) {
        return new ArrayList<>(Arrays.asList(uriAndQueryParametersField.split(QUESTION_MARK)));
    }

    private boolean areQueryParameters(List<String> uriAndParameters) {
        return uriAndParameters.size() == 2;
    }

    private Map<EntityHeaderFields, List<String>> parseRequestHeaders(Optional<BufferedReader> reader) {
        if (reader.isPresent()) {
            BufferedReader in = reader.get();
            try {
                Map<EntityHeaderFields, List<String>> headers = new HashMap<>();
                String line;
                while ((line = in.readLine()) != null && !line.isEmpty()) {
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
