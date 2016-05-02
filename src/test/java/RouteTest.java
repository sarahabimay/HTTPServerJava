import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class RouteTest {
    private static final String VERSION = "HTTP/1.1";
    private Route getIndexRoute;
    private HTTPRequest request;
    private Route postFormRoute;
    private Route putFormRoute;

    @Before
    public void setUp() {
        request = new HTTPRequest();
        getIndexRoute = new Route(HTTPMethod.GET, "/", VERSION);
        postFormRoute = new Route(HTTPMethod.POST, "/form", VERSION);
        putFormRoute = new Route(HTTPMethod.PUT, "/form", VERSION);
    }

    @Test
    public void routeDoesNotMatchRequest() {
        request.addRequestLine(Arrays.asList(HTTPMethod.GET.method(), "/foobar", VERSION));
        request.addBody("my data");
        assertEquals(false, getIndexRoute.isMatch(request));
    }

    @Test
    public void routeMatchesGETPath() {
        request.addRequestLine(Arrays.asList(HTTPMethod.GET.method(), "/", VERSION));
        request.addBody("my data");
        assertEquals(true, getIndexRoute.isMatch(request));
    }

    @Test
    public void routeMatchesPOSTPath() {
        request.addRequestLine(Arrays.asList(HTTPMethod.POST.method(), "/form", VERSION));
        request.addBody("my data");
        assertEquals(true, postFormRoute.isMatch(request));
    }

    @Test
    public void routeMatchesPUTPath() {
        request.addRequestLine(Arrays.asList(HTTPMethod.PUT.method(), "/form", VERSION));
        request.addBody("my data");
        assertEquals(true, putFormRoute.isMatch(request));
    }

    @Test
    public void statusOKForGetRequest() {
        request.addRequestLine(new ArrayList<>(Arrays.asList(HTTPMethod.GET.method(), "/", VERSION)));

        HTTPResponse actualResponse = getIndexRoute.createStatusOKResponseNoBody(request);
        assertEquals(statusOKResponse().getStatusLine(), actualResponse.getStatusLine());
    }

    @Test
    public void statusOKForPostRequest() {
        request.addRequestLine(new ArrayList<>(Arrays.asList(HTTPMethod.POST.method(), "/form", VERSION)));

        HTTPResponse actualResponse = postFormRoute.createStatusOKResponseNoBody(request);
        assertEquals(statusOKResponse().getStatusLine(), actualResponse.getStatusLine());
    }

    @Test
    public void statusOKForPutRequest() {
        request.addRequestLine(new ArrayList<>(Arrays.asList(HTTPMethod.PUT.method(), "/form", VERSION)));

        HTTPResponse actualResponse = putFormRoute.createStatusOKResponseNoBody(request);
        assertEquals(statusOKResponse().getStatusLine(), actualResponse.getStatusLine());
    }

    private HTTPResponse statusOKResponse() {
        HTTPResponse expectedResponse = new HTTPResponse();
        expectedResponse.setStatusLine(VERSION, HTTPStatusCode.OK.statusCode(), HTTPStatusCode.OK.reason());
        return expectedResponse;
    }
}
