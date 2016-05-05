package router;

import org.junit.Before;
import org.junit.Test;
import request.HTTPMethod;
import request.HTTPRequest;
import request.HTTPRequestURI;
import request.HTTPVersion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RouteTest {
    private Route getIndexRoute;
    private Route postFormRoute;
    private Route putFormRoute;

    @Before
    public void setUp() {
        getIndexRoute = new Route(HTTPMethod.GET, HTTPRequestURI.INDEX, HTTPVersion.HTTP_1_1);
        postFormRoute = new Route(HTTPMethod.POST, HTTPRequestURI.FORM, HTTPVersion.HTTP_1_1);
        putFormRoute = new Route(HTTPMethod.PUT, HTTPRequestURI.FORM, HTTPVersion.HTTP_1_1);
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
        return new HTTPRequest().addRequestLine(createGETRequestLine());
    }

    private ArrayList<String> createGETRequestLine() {
        String method = HTTPMethod.GET.method();
        String uri = HTTPRequestURI.INDEX.uri();
        String version = HTTPVersion.HTTP_1_1.version();
        return new ArrayList<>(Arrays.asList(method, uri, version));
    }

    private HTTPRequest knownResourcePOSTRequest() {
        return new HTTPRequest().addRequestLine(createPOSTRequestLine());
    }

    private ArrayList<String> createPOSTRequestLine() {
        String method = HTTPMethod.POST.method();
        String uri = HTTPRequestURI.FORM.uri();
        String version = HTTPVersion.HTTP_1_1.version();
        return new ArrayList<>(Arrays.asList(method, uri, version));
    }

    private HTTPRequest knownResourcePUTRequest() {
        return new HTTPRequest().addRequestLine(createPUTRequestLine());
    }

    private List<String> createPUTRequestLine() {
        String method = HTTPMethod.PUT.method();
        String uri = HTTPRequestURI.FORM.uri();
        String version = HTTPVersion.HTTP_1_1.version();
        return Arrays.asList(method, uri, version);
    }

    private HTTPRequest unknownResourceRequest() {
        return new HTTPRequest().addRequestLine(createGETUnrecognisedURIRequestLine());
    }

    private List<String> createGETUnrecognisedURIRequestLine() {
        String method = HTTPMethod.GET.method();
        String uri = HTTPRequestURI.FOOBAR.uri();
        String version = HTTPVersion.HTTP_1_1.version();
        return Arrays.asList(method, uri, version);
    }
}
