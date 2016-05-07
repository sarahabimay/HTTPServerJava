package router;

import org.junit.Before;
import org.junit.Test;
import request.HTTPMethod;
import request.HTTPRequest;
import request.HTTPResource;
import request.HTTPVersion;
import response.HTTPResponse;
import routeActions.RouteAction;
import routeActions.StatusOKAction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static request.HTTPMethod.*;
import static request.HTTPResource.*;
import static request.HTTPVersion.HTTP_1_1;

public class RouterTest {
    private Router router;
    private String statusOKResponse;
    private String statusFourOhFourResponse;
    private String statusMethodNotAllowedResponse;
    private URIProcessorStub uriProcessorStub;

    @Before
    public void setUp() {
        router = new Router(new RoutesFactory().routeActions());
        statusOKResponse = "HTTP/1.1 200 OK";
        statusFourOhFourResponse = "HTTP/1.1 404 Not Found";
        statusMethodNotAllowedResponse = "HTTP/1.1 405 Method Not Allowed";
        uriProcessorStub = new URIProcessorStub();
    }

    @Test
    public void fourOhFourResponse() {
        HTTPRequest request = unknownResourceRequest();
        List<RouteAction> actions = router.findRouteActions(request);
        HTTPResponse response = actions.get(0).generateResponse(request, router, uriProcessorStub);
        assertEquals(statusFourOhFourResponse, response.getStatusLine());
    }

    @Test
    public void methodNotAllowedResponse() {
        HTTPRequest request = bogusMethodRequest();
        List<RouteAction> actions = router.findRouteActions(request);
        HTTPResponse response = actions.get(0).generateResponse(request, router, uriProcessorStub);
        assertEquals(statusMethodNotAllowedResponse, response.getStatusLine());
    }

    @Test
    public void okActionForGETRequest() {
        HTTPRequest request = knownResourceGETRequest();
        List<RouteAction> actions = router.findRouteActions(request);
        HTTPResponse response = actions.get(0).generateResponse(request, router, uriProcessorStub);
        assertEquals(statusOKResponse, response.getStatusLine());
    }

    @Test
    public void okActionForPOSTRequest() {
        HTTPRequest request = knownResourcePOSTRequest();
        List<RouteAction> actions = router.findRouteActions(request);
        HTTPResponse response = actions.get(0).generateResponse(request, router, uriProcessorStub);
        assertEquals(statusOKResponse, response.getStatusLine());
    }

    @Test
    public void okActionForPUTRequest() {
        HTTPRequest request = knownResourcePUTRequest();
        List<RouteAction> actions = router.findRouteActions(request);
        HTTPResponse response = actions.get(0).generateResponse(request, router, uriProcessorStub);
        assertEquals(statusOKResponse, response.getStatusLine());
    }

    @Test
    public void okActionForHEADRequest() {
        HTTPRequest request = knownResourceHEADRequest();
        List<RouteAction> actions = router.findRouteActions(request);
        HTTPResponse response = actions.get(0).generateResponse(request, router, uriProcessorStub);
        assertEquals(statusOKResponse, response.getStatusLine());
    }

    @Test
    public void findMethodsAllowedForAResource() {
        HTTPResource resource = HTTPResource.FORM;
        Router router = new Router(routeActions());
        List<String> methods = router.allowedMethods(resource);
        assertThat(methods, hasItem(POST.method()));
        assertThat(methods, hasItem(PUT.method()));
    }

    public Map<Route, List<RouteAction>> routeActions() {
        Map<Route, List<RouteAction>> routeActions = new HashMap<>();
        routeActions.put(new Route(HEAD, INDEX, HTTP_1_1), singletonList(new StatusOKAction()));
        routeActions.put(new Route(GET, INDEX, HTTP_1_1), singletonList(new StatusOKAction()));
        routeActions.put(new Route(PUT, FORM, HTTP_1_1), singletonList(new StatusOKAction()));
        routeActions.put(new Route(POST, FORM, HTTP_1_1), singletonList(new StatusOKAction()));
        routeActions.put(new Route(OPTIONS, OPTIONS_ONE, HTTP_1_1), singletonList(new StatusOKAction()));
        routeActions.put(new Route(OPTIONS, OPTIONS_TWO, HTTP_1_1), singletonList(new StatusOKAction()));
        return routeActions;
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

    private HTTPRequest knownResourceHEADRequest() {
        return new HTTPRequest().addRequestLine(createRequestLine(HEAD, INDEX, "", HTTP_1_1));
    }

    private HTTPRequest unknownResourceRequest() {
        return new HTTPRequest().addRequestLine(createRequestLine(GET, FOOBAR, "", HTTP_1_1));
    }

    private HTTPRequest bogusMethodRequest() {
        return new HTTPRequest().addRequestLine(createRequestLine(UNDEFINED, INDEX, "", HTTP_1_1));
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
