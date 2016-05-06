package routeActions;

import org.junit.Test;
import request.HTTPRequest;
import response.HTTPResponse;
import router.RouterFake;
import router.URIProcessorStub;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static request.HTTPMethod.*;
import static request.HTTPResource.OPTIONS_ONE;
import static request.HTTPVersion.HTTP_1_1;
import static response.EntityHeaderFields.ALLOW;

public class AllowOptionsActionTest {
    @Test
    public void createOptionResponse() {
        RouterFake router = new RouterFake();
        router.setFakeResourceMethods(new ArrayList<>(Arrays.asList(GET.method(), HEAD.method())));
        URIProcessorStub uriProcessorStub = new URIProcessorStub();
        HTTPResponse response = new AllowOptionsAction().generateResponse(createOptionsRequest(), router , uriProcessorStub);
        assertEquals("HTTP/1.1 200 OK", response.getStatusLine());
        assertEquals(ALLOW, response.getEntityHeaders().keySet().toArray()[0]);
        assertEquals(2, response.getEntityHeaders().values().toString().split(",").length);
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
