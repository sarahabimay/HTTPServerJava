package request;

import org.junit.Before;
import org.junit.Test;
import response.EntityHeaderFields;
import server.ClientSocketSpy;

import java.io.ByteArrayInputStream;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static request.HTTPMethod.GET;
import static request.HTTPMethod.POST;
import static request.HTTPResource.FORM;
import static request.HTTPResource.INDEX;
import static request.HTTPResource.PARAMETERS;
import static request.HTTPVersion.HTTP_1_1;

public class RequestParserTest {
    private RequestParser requestParser;
    private ByteArrayInputStream inputStream;
    private ClientSocketSpy socketSpy;

    @Before
    public void setUp() {
        requestParser = new RequestParser();
        inputStream = new ByteArrayInputStream(buildGETRequestLine().getBytes());
        socketSpy = new ClientSocketSpy(inputStream);
    }

    @Test
    public void receiveRequestFromClient() {
        requestParser.parseRequest(socketSpy);
        assertEquals(true, socketSpy.hasReadRequestFromInputStream());
    }

    @Test
    public void createRequestWithoutHeaders() {
        HTTPRequest HTTPRequest = requestParser.parseRequest(socketSpy);
        HTTPMethod expectedMethod = GET;
        assertEquals(expectedMethod, HTTPRequest.method());
    }

    @Test
    public void createRequestWithHeaderAndBody() {
        inputStream = new ByteArrayInputStream(buildPOSTRequest().getBytes());
        socketSpy = new ClientSocketSpy(inputStream);
        HTTPRequest httpRequest = requestParser.parseRequest(socketSpy);
        Map<EntityHeaderFields, List<String>> expectedHeaders = emptyRequestHeaders();
        expectedHeaders.put(EntityHeaderFields.CONTENT_LENGTH, new ArrayList<>(Arrays.asList("11")));
        assertEquals("name=myname", httpRequest.body());
        assertEquals(expectedHeaders, httpRequest.headers());
    }

    @Test
    public void ignoreUnrecognizedHeaderFields() {
        inputStream = new ByteArrayInputStream(buildRequestWithUnrecognizedHeaders().getBytes());
        socketSpy = new ClientSocketSpy(inputStream);
        HTTPRequest httpRequest = requestParser.parseRequest(socketSpy);
        assertEquals(emptyRequestHeaders(), httpRequest.headers());
    }

    @Test
    public void parseRequestWithParameters() {
        inputStream = new ByteArrayInputStream(buildRequestWithParameters().getBytes());
        socketSpy = new ClientSocketSpy(inputStream);
        HTTPRequest httpRequest = requestParser.parseRequest(socketSpy);
        assertEquals(queryParameters(), httpRequest.queryParameters());
    }

    private String buildRequestWithParameters() {
        return new StringBuilder()
                .append(GET)
                .append(" ")
                .append(PARAMETERS)
                .append("?")
                .append(queryParameters())
                .append(" ")
                .append(HTTP_1_1)
                .toString();
    }

    private String queryParameters() {
        return "variable_1=Operators%20%3C%2C%20%3E%2C%20%3D%2C%20!%3D%3B%20%2B%2C%20-%2C%20*%2C%20%26%2C%20%40%2C%20%23%2C%20%24%2C%20%5B%2C%20%5D%3A%20%22is%20that%20all%22%3F&variable_2=stuff";
    }

    private Map<EntityHeaderFields, List<String>> emptyRequestHeaders() {
        return new HashMap<>();
    }

    private String buildRequestWithUnrecognizedHeaders() {
        return new StringBuilder()
                .append(optionRequestLine())
                .append("\n")
                .append(lotsOfHeaders())
                .append("\r\n\r\n")
                .append(postBody())
                .toString();
    }

    private String lotsOfHeaders() {
        return new StringBuilder()
                .append("Host: localhost:5000")
                .append("Connection: Keep-Alive")
                .append("User-Agent: Apache-HttpClient/4.3.5 (java 1.5)")
                .append("Accept-Encoding: gzip,deflate")
                .toString();
    }

    private String optionRequestLine() {
        return new StringBuilder()
                .append(HTTPMethod.OPTIONS)
                .append(" ")
                .append(HTTPResource.OPTIONS_ONE)
                .append(" ")
                .append(HTTP_1_1)
                .toString();
    }

    private String buildPOSTRequest() {
        return new StringBuilder()
                .append(postRequestLine())
                .append("\n")
                .append(postHeaders())
                .append("\r\n\r\n")
                .append(postBody())
                .toString();
    }

    private String postRequestLine() {
        return new StringBuilder()
                .append(POST)
                .append(" ")
                .append(FORM)
                .append(" ")
                .append(HTTP_1_1)
                .toString();
    }

    private String postHeaders() {
        return new StringBuilder()
                .append("Content-Length: 11")
                .toString();
    }

    private String postBody() {
        return new StringBuilder()
                .append("name=myname")
                .toString();
    }

    private String buildGETRequestLine() {
        return new StringBuilder()
                .append(GET)
                .append(" ")
                .append(INDEX)
                .append(" ")
                .append(HTTP_1_1)
                .toString();
    }
}
