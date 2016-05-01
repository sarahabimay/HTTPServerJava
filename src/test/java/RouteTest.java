import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class RouteTest {

    private static final String REASON = "OK";
    private static final String VERSION = "HTTP/1.1";
    private static final int STATUS_CODE = 200;
    private final String GET = "GET";
    private final String PUT = "PUT";
    private final String POST = "POST";
    private Route getIndexRoute;
    private HTTPRequest request;
    private Route postFormRoute;
    private Route putFormRoute;

    @Before
    public void setUp() {
        request = new HTTPRequest();
        getIndexRoute = new Route(GET, "/", VERSION);
        postFormRoute = new Route(POST, "/form", VERSION);
        putFormRoute = new Route(PUT, "/form", VERSION);
    }

    @Test
    public void routeDoesNotMatchRequest() {
        request.addRequestLine(Arrays.asList(GET, "/foobar", VERSION));
        request.addBody("my data");
        assertEquals(false, getIndexRoute.isMatch(request));
    }

    @Test
    public void routeMatchesGETPath() {
        request.addRequestLine(Arrays.asList(GET, "/", VERSION));
        request.addBody("my data");
        assertEquals(true, getIndexRoute.isMatch(request));
    }

    @Test
    public void routeMatchesPOSTPath() {
        request.addRequestLine(Arrays.asList(POST, "/form", VERSION));
        request.addBody("my data");
        assertEquals(true, postFormRoute.isMatch(request));
    }

    @Test
    public void routeMatchesPUTPath() {
        request.addRequestLine(Arrays.asList(PUT, "/form", VERSION));
        request.addBody("my data");
        assertEquals(true, putFormRoute.isMatch(request));
    }

    @Test
    public void statusOKForGetRequest() {
        Route route = new Route(GET, "/", VERSION);
        request.addRequestLine(new ArrayList<>(Arrays.asList(GET, "/", VERSION)));

        HTTPResponse actualResponse = route.createStatusOKResponseNoBody(request);
        assertEquals(statusOKResponse().getStatusLine(), actualResponse.getStatusLine());
    }

    @Test
    public void statusOKForPostRequest() {
        Route route = new Route(POST, "/form", VERSION);
        request.addRequestLine(new ArrayList<>(Arrays.asList(POST, "/form", VERSION)));

        HTTPResponse actualResponse = route.createStatusOKResponseNoBody(request);
        assertEquals(statusOKResponse().getStatusLine(), actualResponse.getStatusLine());
    }

    @Test
    public void statusOKForPutRequest() {
        Route route = new Route(PUT, "/form", VERSION);
        request.addRequestLine(new ArrayList<>(Arrays.asList(PUT, "/form", VERSION)));

        HTTPResponse actualResponse = route.createStatusOKResponseNoBody(request);
        assertEquals(statusOKResponse().getStatusLine(), actualResponse.getStatusLine());
    }

    private HTTPResponse statusOKResponse() {
        HTTPResponse expectedResponse = new HTTPResponse();
        expectedResponse.setStatusLine(VERSION, STATUS_CODE, REASON);
        return expectedResponse;
    }
}
