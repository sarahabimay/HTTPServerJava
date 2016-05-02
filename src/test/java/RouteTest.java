import org.junit.Before;
import org.junit.Test;
import request.HTTPMethod;
import request.HTTPRequest;
import request.HTTPRequestURI;
import request.HTTPVersion;
import router.Route;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RouteTest {
    private Route getIndexRoute;
    private HTTPRequest request;
    private Route postFormRoute;
    private Route putFormRoute;

    @Before
    public void setUp() {
        request = new HTTPRequest();
        getIndexRoute = new Route(HTTPMethod.GET, HTTPRequestURI.INDEX, HTTPVersion.HTTP_1_1);
        postFormRoute = new Route(HTTPMethod.POST, HTTPRequestURI.FORM, HTTPVersion.HTTP_1_1);
        putFormRoute = new Route(HTTPMethod.PUT, HTTPRequestURI.FORM, HTTPVersion.HTTP_1_1);
    }

    @Test
    public void routeDoesNotMatchRequest() {
        request.addRequestLine(createGETUnrecognisedURIRequestLine());
        request.addBody("my data");
        assertEquals(false, getIndexRoute.isMatch(request));
    }

    @Test
    public void routeMatchesGETPath() {
        request.addRequestLine(createGETRequestLine());
        request.addBody("my data");
        assertEquals(true, getIndexRoute.isMatch(request));
    }

    @Test
    public void routeMatchesPOSTPath() {
        request.addRequestLine(createPOSTRequestLine());
        request.addBody("my data");
        assertEquals(true, postFormRoute.isMatch(request));
    }

    @Test
    public void routeMatchesPUTPath() {
        request.addRequestLine(createPUTRequestLine());
        request.addBody("my data");
        assertEquals(true, putFormRoute.isMatch(request));
    }

    private ArrayList<String> createGETRequestLine() {
        String method = HTTPMethod.GET.method();
        String uri = HTTPRequestURI.INDEX.uri();
        String version = HTTPVersion.HTTP_1_1.version();
        return new ArrayList<>(Arrays.asList(method, uri, version));
    }

    private ArrayList<String> createPOSTRequestLine() {
        String method = HTTPMethod.POST.method();
        String uri = HTTPRequestURI.FORM.uri();
        String version = HTTPVersion.HTTP_1_1.version();
        return new ArrayList<>(Arrays.asList(method, uri, version));
    }

    private List<String> createPUTRequestLine() {
        String method = HTTPMethod.PUT.method();
        String uri = HTTPRequestURI.FORM.uri();
        String version = HTTPVersion.HTTP_1_1.version();
        return Arrays.asList(method, uri, version);
    }

    private List<String> createGETUnrecognisedURIRequestLine() {
        String method = HTTPMethod.GET.method();
        String uri = HTTPRequestURI.FOOBAR.uri();
        String version = HTTPVersion.HTTP_1_1.version();
        return Arrays.asList(method, uri, version);
    }
}
