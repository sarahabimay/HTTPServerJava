package routeActions;

import org.junit.Test;
import request.HTTPMethod;
import request.HTTPRequest;
import request.HTTPResource;
import request.HTTPVersion;
import response.HTTPResponse;
import router.RouterStub;
import router.URIProcessorStub;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static request.HTTPMethod.HEAD;
import static request.HTTPResource.INDEX;
import static request.HTTPVersion.HTTP_1_1;

public class StatusOKActionTest {
    @Test
    public void createStatusOkResponse() {
        StatusOKAction action = new StatusOKAction();
        HTTPResponse response = action.generateResponse(knownResourceHEADRequest(), new RouterStub() , new URIProcessorStub());
        assertEquals("HTTP/1.1 200 OK", response.getStatusLine());
    }

    private HTTPRequest knownResourceHEADRequest() {
        return new HTTPRequest().addRequestLine(createRequestLine(HEAD, INDEX, HTTP_1_1));
    }

    private Map<String, String> createRequestLine(HTTPMethod method, HTTPResource uri, HTTPVersion version) {
        Map<String, String> requestLine = new HashMap<>();
        requestLine.put("method", method.method());
        requestLine.put("uri", uri.uri());
        requestLine.put("version", version.version());
        return requestLine;
    }
}
