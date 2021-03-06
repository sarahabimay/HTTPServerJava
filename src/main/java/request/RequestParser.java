package request;

import exceptions.RequestParsingException;
import exceptions.ServerErrorHandler;
import messages.EntityHeaderFields;
import server.HttpClientSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

import static java.util.Arrays.asList;
import static request.HTTPMethod.lookupMethod;
import static request.HTTPResource.*;
import static request.HTTPVersion.*;
import static exceptions.RequestErrors.*;
import static messages.EntityHeaderFields.*;
import static messages.EntityHeaderFields.CONTENT_LENGTH;

public class RequestParser {
    private static final String COLON = ":";
    private final String QUESTION_MARK = "\\?";
    private final String SPACE = " ";
    private final String HTTP_METHOD = "method";
    private final String URI = "uri";
    private final String QUERY_PARAMETERS = "queryParameters";
    private final String HTTP_VERSION_1_1 = "version";
    private final ServerErrorHandler errorHandler;

    public RequestParser(ServerErrorHandler errorHandler) {
       this.errorHandler = errorHandler;
    }

    public HTTPRequest parseRequest(HttpClientSocket clientSocket) {
        try {
            Optional<BufferedReader> reader = clientSocket.getInputReader();
            if (reader.isPresent()) {
                Map<String, String> parsedRequestLine = parseRequestLine(getRequestLine(reader));
                Map<EntityHeaderFields, String> headers = parseRequestHeaders(reader);
                String body = getBody(reader, contentLengthOfBody(headers));
                return buildHTTPRequest(body, parsedRequestLine, headers);
            } else {
                return errorHandler.buildInvalidRequest(UNAVAILABLE_INPUT_READER.errorMessage());
            }

        } catch (RequestParsingException e) {
            return errorHandler.buildInvalidRequest(e.getMessage());
        }
    }

    private HTTPRequest buildHTTPRequest(String body, Map<String, String> requestLine, Map<EntityHeaderFields, String> headers) {
        return new HTTPRequest()
                .addRequestLine(requestLine)
                .addRequestHeader(headers)
                .addBody(body);
    }

    private String getRequestLine(Optional<BufferedReader> reader) {
        BufferedReader in = reader.get();
        try {
            return in.readLine();
        } catch (IOException e) {
            throw new RequestParsingException(e.getMessage());
        }
    }

    private Map<String, String> parseRequestLine(String header) {
        List<String> requestLine = asList(header.split(SPACE));
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
        return new ArrayList<>(asList(uriAndQueryParametersField.split(QUESTION_MARK)));
    }

    private boolean areQueryParameters(List<String> uriAndParameters) {
        return uriAndParameters.size() == 2;
    }

    private Map<EntityHeaderFields, String> parseRequestHeaders(Optional<BufferedReader> reader) {
        if (reader.isPresent()) {
            BufferedReader in = reader.get();
            try {
                Map<EntityHeaderFields, String> headers = new HashMap<>();
                String line;
                while ((line = in.readLine()) != null && !line.isEmpty()) {
                    parseRequestHeader(headers, line);
                }
                return headers;
            } catch (IOException e) {
                throw new RequestParsingException(e.getMessage());
            }
        }
        return new HashMap<>();
    }

    private void parseRequestHeader(Map<EntityHeaderFields, String> headers, String line) {
        String[] header = parseFieldAndValue(line);
        EntityHeaderFields fieldName = lookupHeaderField(header[0]);
        if (!fieldName.equals(INVALID_HEADER)) {
            headers.put(fieldName, header[1].trim());
        }
    }

    private String[] parseFieldAndValue(String field) {
        return field.split(COLON);
    }

    private int contentLengthOfBody(Map<EntityHeaderFields, String> headers) {
        if (headers != null && !headers.isEmpty()) {
            String contentLengthField = headers.get(CONTENT_LENGTH);
            if (contentLengthField != null) {
                return Integer.parseInt(contentLengthField);
            }
        }
        return 0;
    }

    private String getBody(Optional<BufferedReader> reader, int lengthOfBodyContent) {
        if (reader.isPresent()) {
            char[] body = new char[lengthOfBodyContent];
            try {
                reader.get().read(body, 0, lengthOfBodyContent);
                return String.valueOf(body);
            } catch (IOException e) {
                throw new RequestParsingException(e.getMessage());
            }
        }
        return "";
    }
}
