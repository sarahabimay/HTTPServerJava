package request;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static request.HTTPMethod.GET;
import static request.HTTPResource.INDEX;
import static request.HTTPVersion.HTTP_1_1;

public class HTTPRequestTest {
    @Test
    public void createGETWithNoBodyRequest() {
        HTTPRequest request = createGetRequestLineOnly();
        assertEquals(GET, request.method());
        assertEquals(INDEX, request.uri());
        assertEquals(HTTP_1_1, request.version());
        assertEquals("", request.body());
    }

    @Test
    public void createGETWithQueryParameters() {
        HTTPRequest request = createGetRequestLineOnly();
        assertEquals(GET, request.method());
        assertEquals(INDEX, request.uri());
        assertEquals(HTTP_1_1, request.version());
        assertEquals("key=value&key2=value2", request.queryParameters());
    }

    private HTTPRequest createGetRequestLineOnly() {
        return new HTTPRequest().addRequestLine(requestLineWithQueryParameters());
    }

    private Map<String, String> requestLineWithQueryParameters() {
        Map<String, String> requestLine = new HashMap<>();
        requestLine.put("method", GET.method());
        requestLine.put("uri", INDEX.uri());
        requestLine.put("version", HTTP_1_1.version());
        requestLine.put("queryParameters", "key=value&key2=value2");
        return requestLine;
    }
}
