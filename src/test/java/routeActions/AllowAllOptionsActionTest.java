package routeActions;

import org.junit.Test;
import request.HTTPRequest;
import response.HTTPResponse;
import router.URIProcessorStub;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static request.HTTPMethod.OPTIONS;
import static request.HTTPResource.OPTIONS_ONE;
import static request.HTTPVersion.HTTP_1_1;
import static response.EntityHeaderFields.ALLOW;

public class AllowAllOptionsActionTest {
    @Test
    public void createOptionResponse() {
        URIProcessorStub uriProcessorStub = new URIProcessorStub();
        HTTPResponse response = new AllowAllOptionsAction().generateResponse(createOptionsRequest(), uriProcessorStub);
        assertEquals("HTTP/1.1 200 OK", response.getStatusLine());
        assertEquals(ALLOW, response.getEntityHeaders().keySet().toArray()[0]);
        assertEquals(5, response.getEntityHeaders().values().toString().split(",").length);
    }

    private HTTPRequest createOptionsRequest() {
        return new HTTPRequest().addRequestLine(optionsRequestLine());
    }

    private Map<String, String> optionsRequestLine() {
        Map<String, String> requestLine = new HashMap<>();
        requestLine.put("method", OPTIONS.method());
        requestLine.put("uri", OPTIONS_ONE.uri());
        requestLine.put("version", HTTP_1_1.version());
        return requestLine;
    }
}
