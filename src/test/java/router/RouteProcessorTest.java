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

import java.util.*;

import static functions.FunctionHelpers.insertToList;
import static org.junit.Assert.assertEquals;
import static request.HTTPMethod.*;
import static request.HTTPResource.*;
import static request.HTTPVersion.HTTP_1_1;

public class RouteProcessorTest {
    private String statusLineOKResponse;
    private String statusLineFourOhFourResponse;
    private RouteProcessor routeProcessor;

    @Before
    public void setUp() {
        statusLineOKResponse = "HTTP/1.1 200 OK";
        statusLineFourOhFourResponse = "HTTP/1.1 404 Not Found";
        routeProcessor = new RouteProcessor(new Router(routeActions()), new URIProcessorStub());
    }

    @Test
    public void buildStatusOyKResponse() {
        HTTPRequest request = createRequest(GET, INDEX, "", HTTP_1_1);
        HTTPResponse response = routeProcessor.buildResponse(request);
        assertEquals(statusLineOKResponse, response.getStatusLine());
    }

    @Test
    public void buildStatusNotFoundResponse() {
        HTTPRequest request = createRequest(GET, FOOBAR, "", HTTP_1_1);
        HTTPResponse response = routeProcessor.buildResponse(request);
        assertEquals(statusLineFourOhFourResponse, response.getStatusLine());
    }

    @Test
    public void buildResponseForPOSTRequest() {
        HTTPRequest request = createRequest(POST, FORM, "", HTTP_1_1);
        HTTPResponse response = routeProcessor.buildResponse(request);
        assertEquals(statusLineOKResponse, response.getStatusLine());
    }

    @Test
    public void buildResponseForPUTRequest() {
        HTTPRequest request = createRequest(PUT, FORM, "", HTTP_1_1);
        HTTPResponse response = routeProcessor.buildResponse(request);
        assertEquals(statusLineOKResponse, response.getStatusLine());
    }

    private HTTPRequest createRequest(HTTPMethod method, HTTPResource uri, String queryParams, HTTPVersion version) {
        return new HTTPRequest().addRequestLine(createRequestLine(method, uri, queryParams, version));
    }

    private Map<String, String> createRequestLine(HTTPMethod method, HTTPResource uri, String queryParams, HTTPVersion version) {
        Map<String, String> requestLine = new HashMap<>();
        requestLine.put("method", method.method());
        requestLine.put("uri", uri.uri());
        requestLine.put("version", version.version());
        requestLine.put("queryParameters", queryParams);
        return requestLine;
    }

    public Map<Route, List<RouteAction>> routeActions() {
        Map<Route, List<RouteAction>> routeActions = new HashMap<>();
        routeActions.put(new Route(HEAD, INDEX, HTTP_1_1), insertToList.apply(new StatusOKAction()));
        routeActions.put(new Route(GET, INDEX, HTTP_1_1), insertToList.apply(new StatusOKAction()));
        routeActions.put(new Route(PUT, FORM, HTTP_1_1), insertToList.apply(new StatusOKAction()));
        routeActions.put(new Route(POST, FORM, HTTP_1_1), insertToList.apply(new StatusOKAction()));
        routeActions.put(new Route(OPTIONS, OPTIONS_ONE, HTTP_1_1), insertToList.apply(new StatusOKAction()));
        routeActions.put(new Route(OPTIONS, OPTIONS_TWO, HTTP_1_1), insertToList.apply(new StatusOKAction()));
        return routeActions;
    }
}
