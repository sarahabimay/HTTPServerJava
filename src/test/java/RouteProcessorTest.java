import org.junit.Before;
import org.junit.Test;
import request.HTTPRequest;
import response.HTTPResponse;
import routeActions.RouteAction;
import routeActions.StatusOKAction;
import router.Route;
import router.RouteProcessor;
import router.Router;

import java.util.*;

import static functions.FunctionHelpers.insertToList;
import static org.junit.Assert.assertEquals;
import static request.HTTPMethod.*;
import static request.HTTPRequestURI.*;
import static request.HTTPVersion.HTTP_1_1;

public class RouteProcessorTest {
    private String statusLineOKResponse;
    private String statusLineFourOhFourResponse;
    private RouteProcessor routeProcessor;

    @Before
    public void setUp() {
        statusLineOKResponse = "HTTP/1.1 200 OK";
        statusLineFourOhFourResponse = "HTTP/1.1 404 Not Found";
        routeProcessor = new RouteProcessor(new Router(routeActions()));
    }

    @Test
    public void buildStatusOKResponse() {
        HTTPRequest request = new HTTPRequest().addRequestLine(new ArrayList<>(Arrays.asList(GET.method(), INDEX.uri(), HTTP_1_1.version())));
        HTTPResponse response = routeProcessor.buildResponse(request);
        assertEquals(statusLineOKResponse, response.getStatusLine());
    }

    @Test
    public void buildStatusNotFoundResponse() {
        HTTPRequest request = new HTTPRequest().addRequestLine(new ArrayList<>(Arrays.asList(GET.method(), FOOBAR.uri(), HTTP_1_1.version())));
        HTTPResponse response = routeProcessor.buildResponse(request);
        assertEquals(statusLineFourOhFourResponse, response.getStatusLine());
    }

    @Test
    public void buildResponseForPOSTRequest() {
        HTTPRequest request = new HTTPRequest().addRequestLine(new ArrayList<>(Arrays.asList(POST.method(), FORM.uri(), HTTP_1_1.version())));
        HTTPResponse response = routeProcessor.buildResponse(request);
        assertEquals(statusLineOKResponse, response.getStatusLine());
    }

    @Test
    public void buildResponseForPUTRequest() {
        HTTPRequest request = new HTTPRequest().addRequestLine(new ArrayList<>(Arrays.asList(PUT.method(), FORM.uri(), HTTP_1_1.version())));
        HTTPResponse response = routeProcessor.buildResponse(request);
        assertEquals(statusLineOKResponse, response.getStatusLine());
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
