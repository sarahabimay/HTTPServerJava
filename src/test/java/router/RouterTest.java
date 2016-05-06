package router;

import org.junit.Before;
import org.junit.Test;
import request.HTTPMethod;
import request.HTTPRequest;
import request.HTTPResource;
import request.HTTPVersion;
import response.HTTPResponse;
import routeActions.RouteAction;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static request.HTTPMethod.*;
import static request.HTTPResource.*;
import static request.HTTPVersion.HTTP_1_1;

public class RouterTest {
    private Router router;
    private String statusOKResponse;
    private String statusFourOhFourResponse;
    private URIProcessorStub uriProcessorStub;

    @Before
    public void setUp() {
        router = new Router(new RoutesFactory().routeActions());
        statusOKResponse = "HTTP/1.1 200 OK";
        statusFourOhFourResponse = "HTTP/1.1 404 Not Found";
        uriProcessorStub = new URIProcessorStub();
    }

    @Test
    public void fourOhFourResponse() {
        HTTPRequest request = unknownResourceRequest();
        List<RouteAction> actions = router.findRouteActions(request);
        HTTPResponse response = actions.get(0).generateResponse(request, uriProcessorStub);
        assertEquals(statusFourOhFourResponse, response.getStatusLine());
    }

    @Test
    public void okActionForGETRequest() {
        HTTPRequest request = knownResourceGETRequest();
        List<RouteAction> actions = router.findRouteActions(request);
        HTTPResponse response = actions.get(0).generateResponse(request, uriProcessorStub);
        assertEquals(statusOKResponse, response.getStatusLine());
    }

    @Test
    public void okActionForPOSTRequest() {
        HTTPRequest request = knownResourcePOSTRequest();
        List<RouteAction> actions = router.findRouteActions(request);
        HTTPResponse response = actions.get(0).generateResponse(request, uriProcessorStub);
        assertEquals(statusOKResponse, response.getStatusLine());
    }

    @Test
    public void okActionForPUTRequest() {
        HTTPRequest request = knownResourcePUTRequest();
        List<RouteAction> actions = router.findRouteActions(request);
        HTTPResponse response = actions.get(0).generateResponse(request, uriProcessorStub);
        assertEquals(statusOKResponse, response.getStatusLine());
    }

    @Test
    public void okActionForHEADRequest() {
        HTTPRequest request = knownResourceHEADRequest();
        List<RouteAction> actions = router.findRouteActions(request);
        HTTPResponse response = actions.get(0).generateResponse(request, uriProcessorStub);
        assertEquals(statusOKResponse, response.getStatusLine());
    }

    private HTTPRequest knownResourceGETRequest() {
        return new HTTPRequest().addRequestLine(createRequestLine(GET, INDEX, "", HTTP_1_1));
    }

    private HTTPRequest knownResourcePOSTRequest() {
        return new HTTPRequest().addRequestLine(createRequestLine(POST, FORM, "", HTTP_1_1));
    }

    private HTTPRequest knownResourcePUTRequest() {
        return new HTTPRequest().addRequestLine(createRequestLine(PUT, FORM, "", HTTP_1_1));
    }

    private HTTPRequest unknownResourceRequest() {
        return new HTTPRequest().addRequestLine(createRequestLine(GET, FOOBAR, "", HTTP_1_1));
    }

    private HTTPRequest knownResourceHEADRequest() {
        return new HTTPRequest().addRequestLine(createRequestLine(HEAD, INDEX, "", HTTP_1_1));
    }

    private Map<String, String> createRequestLine(HTTPMethod method, HTTPResource uri, String queryParams, HTTPVersion version) {
        Map<String, String> requestLine = new HashMap<>();
        requestLine.put("method", method.method());
        requestLine.put("uri", uri.uri());
        requestLine.put("version", version.version());
        requestLine.put("queryParameters", queryParams);
        return requestLine;
    }
}
