package request;

import exceptions.ServerErrorHandler;
import exceptions.ServerErrorHandlerSpy;
import org.junit.Before;
import org.junit.Test;
import response.EntityHeaderFields;
import server.ClientSocketFake;
import server.ClientSocketSpy;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static request.HTTPMethod.*;
import static request.HTTPResource.*;
import static request.HTTPVersion.HTTP_1_1;
import static response.EntityHeaderFields.CONTENT_LENGTH;
import static response.EntityHeaderFields.RANGE;

public class RequestParserTest {
    private RequestParser requestParser;

    @Before
    public void setUp() {
        requestParser = new RequestParser(new ServerErrorHandler());
    }

    @Test
    public void receiveRequestFromClient() {
        ClientSocketSpy socketSpy = new ClientSocketSpy(getInputStream(buildGETRequestLine()));
        requestParser.parseRequest(socketSpy);
        assertEquals(true, socketSpy.hasReadRequestFromInputStream());
    }

    @Test
    public void noInputStreamError() {
        ClientSocketFake socketFake = new ClientSocketFake(null);
        ServerErrorHandlerSpy errorHandlerSpy = new ServerErrorHandlerSpy();
        RequestParser requestParser = new RequestParser(errorHandlerSpy);
        HTTPRequest request = requestParser.parseRequest(socketFake);
        assertEquals(true, errorHandlerSpy.hasInvalidRequestBeenBuilt());
        assertEquals(UNDEFINED, request.method());
    }

    @Test
    public void readErroneousRequestLineFromInputStream() {
        ClientSocketFake socketFake = new ClientSocketFake(getInputStream(buildRequestWithErroneousRequestLine()));
        ServerErrorHandlerSpy errorHandlerSpy = new ServerErrorHandlerSpy();
        RequestParser requestParser = new RequestParser(errorHandlerSpy);
        HTTPRequest request = requestParser.parseRequest(socketFake);
        assertEquals(true, errorHandlerSpy.hasInvalidRequestBeenBuilt());
        assertEquals(UNDEFINED, request.method());
    }

    @Test
    public void readErroneousRequestHeaderFromInputStream() {
        ClientSocketFake socketFake = new ClientSocketFake(getInputStream(buildRequestWithErroneousHeader()));
        ServerErrorHandlerSpy errorHandlerSpy = new ServerErrorHandlerSpy();
        RequestParser requestParser = new RequestParser(errorHandlerSpy);
        HTTPRequest request = requestParser.parseRequest(socketFake);
        assertEquals(true, errorHandlerSpy.hasInvalidRequestBeenBuilt());
        assertEquals(UNDEFINED, request.method());
    }

    @Test
    public void readErroneousBodyFromInputStream() {
        ClientSocketFake socketFake = new ClientSocketFake(getInputStream(buildRequestWithErroneousBody()));
        ServerErrorHandlerSpy errorHandlerSpy = new ServerErrorHandlerSpy();
        RequestParser requestParser = new RequestParser(errorHandlerSpy);
        HTTPRequest request = requestParser.parseRequest(socketFake);
        assertEquals(true, errorHandlerSpy.hasInvalidRequestBeenBuilt());
        assertEquals(UNDEFINED, request.method());
    }

    @Test
    public void createRequestWithoutHeaders() {
        ClientSocketSpy socketSpy = new ClientSocketSpy(getInputStream(buildGETRequestLine()));
        HTTPRequest HTTPRequest = requestParser.parseRequest(socketSpy);
        HTTPMethod expectedMethod = GET;
        assertEquals(expectedMethod, HTTPRequest.method());
    }

    @Test
    public void createRequestWithHeaderAndBody() {
        ClientSocketSpy socketSpy = new ClientSocketSpy(getInputStream(buildPOSTRequest()));
        HTTPRequest httpRequest = requestParser.parseRequest(socketSpy);
        Map<EntityHeaderFields, String> expectedHeaders = emptyRequestHeaders();
        expectedHeaders.put(CONTENT_LENGTH, "11");
        assertEquals("name=myname", httpRequest.body());
        assertEquals(expectedHeaders, httpRequest.headers());
    }

    @Test
    public void ignoreUnrecognizedHeaderFields() {
        ClientSocketSpy socketSpy = new ClientSocketSpy(getInputStream(buildRequestWithUnrecognizedHeaders()));
        HTTPRequest httpRequest = requestParser.parseRequest(socketSpy);
        assertEquals(emptyRequestHeaders(), httpRequest.headers());
    }

    @Test
    public void parseRequestWithParameters() {
        ClientSocketSpy socketSpy = new ClientSocketSpy(getInputStream(buildRequestWithQueryParameters()));
        HTTPRequest httpRequest = requestParser.parseRequest(socketSpy);
        assertEquals(queryParameters(), httpRequest.queryParameters());
    }

    @Test
    public void parsePartialContentRequest() {
        ClientSocketSpy socketSpy = new ClientSocketSpy(getInputStream(partialContentRequest()));
        HTTPRequest httpRequest = requestParser.parseRequest(socketSpy);
        assertEquals("bytes=0-4", httpRequest.headers().get(RANGE));
    }

    private ByteArrayInputStream getInputStream(String request) {
        return new ByteArrayInputStream(request.getBytes());
    }

    private String partialContentRequest() {
        return new StringBuilder()
                .append(GET)
                .append(" ")
                .append(PARTIAL_CONTENT)
                .append(" ")
                .append(HTTP_1_1)
                .append("\n")
                .append(partiaContentHeaders())
                .toString();
    }

    private String partiaContentHeaders() {
        return new StringBuilder()
                .append("Host: localhost:5000")
                .append("\n")
                .append("Range: bytes=0-4")
                .append("\n")
                .append("Connection: Keep-Alive")
                .append("\n")
                .append("User-Agent: Apache-HttpClient/4.3.5 (java 1.5)")
                .append("\n")
                .append("Accept-Encoding: gzip,deflate")
                .toString();
    }

    private String buildRequestWithQueryParameters() {
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

    private Map<EntityHeaderFields, String> emptyRequestHeaders() {
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

    private String buildRequestWithErroneousRequestLine() {
        return new StringBuilder()
                .append(erroneousInput())
                .toString();
    }

    private String buildRequestWithErroneousHeader() {
        return new StringBuilder()
                .append(buildGETRequestLine())
                .append("\n")
                .append(erroneousInput())
                .toString();
    }

    private String buildRequestWithErroneousBody() {
        return new StringBuilder()
                .append(buildGETRequestLine())
                .append("\n")
                .append(postHeaders())
                .append("\r\n\r\n")
                .append(erroneousInput())
                .toString();
    }

    private String erroneousInput() {
        return "ERRONEOUS INPUT";
    }

    private String erroneousRequestHeaders() {
        return new StringBuilder()
                .append("ERRONEOUS INPUT")
                .toString();
    }
}
