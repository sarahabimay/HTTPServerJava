package router;

import org.junit.Before;
import org.junit.Test;
import request.HTTPMethod;
import request.HTTPRequest;
import request.HTTPRequestURI;
import request.HTTPVersion;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static request.HTTPMethod.*;
import static request.HTTPRequestURI.*;
import static request.HTTPVersion.HTTP_1_1;

public class RouteTest {
    private Route getIndexRoute;
    private Route postFormRoute;
    private Route putFormRoute;

    @Before
    public void setUp() {
        getIndexRoute = new Route(GET, INDEX, HTTP_1_1);
        postFormRoute = new Route(POST, FORM, HTTP_1_1);
        putFormRoute = new Route(PUT, FORM, HTTP_1_1);
    }

    @Test
    public void routeDoesNotMatchRequest() {
        HTTPRequest request = unknownResourceRequest();
        request.addBody("my data");
        assertEquals(false, getIndexRoute.isMatch(request));
    }

    @Test
    public void routeMatchesGETPath() {
        HTTPRequest request = knownResourceGETRequest();
        request.addBody("my data");
        assertEquals(true, getIndexRoute.isMatch(request));
    }

    @Test
    public void routeMatchesPOSTPath() {
        HTTPRequest request = knownResourcePOSTRequest();
        request.addBody("my data");
        assertEquals(true, postFormRoute.isMatch(request));
    }

    @Test
    public void routeMatchesPUTPath() {
        HTTPRequest request = knownResourcePUTRequest();
        request.addBody("my data");
        assertEquals(true, putFormRoute.isMatch(request));
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

    private Map<String, String> createRequestLine(HTTPMethod method, HTTPRequestURI uri, String queryParams, HTTPVersion version) {
        Map<String, String> requestLine = new HashMap<>();
        requestLine.put("method", method.method());
        requestLine.put("uri", uri.uri());
        requestLine.put("version", version.version());
        requestLine.put("queryParameters", queryParams);
        return requestLine;
    }
}
