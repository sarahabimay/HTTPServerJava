import org.junit.Before;
import org.junit.Test;
import request.HTTPRequest;
import response.HTTPResponse;
import routeActions.RouteAction;
import router.Router;
import router.RoutesFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static request.HTTPMethod.*;
import static request.HTTPRequestURI.*;
import static request.HTTPVersion.HTTP_1_1;

public class RouterTest {
    private Router router;
    private HTTPRequest parsedRequest;
    private String statusOKResponse;
    private String statusFourOhFourResponse;

    @Before
    public void setUp() {
        router = new Router(new RoutesFactory().routeActions());
        parsedRequest = new HTTPRequest();
        statusOKResponse = "HTTP/1.1 200 OK";
        statusFourOhFourResponse = "HTTP/1.1 404 Not Found";
    }

    @Test
    public void fourOhFourAction() {
        parsedRequest.addRequestLine(createGETUnrecognisedURIRequestLine());
        List<RouteAction> actions = router.findRouteActions(parsedRequest);
        HTTPResponse response = actions.get(0).generateResponse(parsedRequest);
        assertEquals(statusFourOhFourResponse, response.getStatusLine());
    }

    @Test
    public void okActionForGETRequest() {
        parsedRequest.addRequestLine(createKnownGETRequestLine());
        List<RouteAction> actions = router.findRouteActions(parsedRequest);
        HTTPResponse response = actions.get(0).generateResponse(parsedRequest);
        assertEquals(statusOKResponse, response.getStatusLine());
    }

    @Test
    public void okActionForPOSTRequest() {
        parsedRequest.addRequestLine(createPOSTRequestLine());
        List<RouteAction> actions = router.findRouteActions(parsedRequest);
        HTTPResponse response = actions.get(0).generateResponse(parsedRequest);
        assertEquals(statusOKResponse, response.getStatusLine());
    }

    @Test
    public void okActionForPUTRequest() {
        parsedRequest.addRequestLine(createPUTRequestLine());
        List<RouteAction> actions = router.findRouteActions(parsedRequest);
        HTTPResponse response = actions.get(0).generateResponse(parsedRequest);
        assertEquals(statusOKResponse, response.getStatusLine());
    }

    @Test
    public void okActionForHEADRequest() {
        parsedRequest.addRequestLine(createHEADRequestLine());
        List<RouteAction> actions = router.findRouteActions(parsedRequest);
        HTTPResponse response = actions.get(0).generateResponse(parsedRequest);
        assertEquals(statusOKResponse, response.getStatusLine());
    }

    private List<String> createGETUnrecognisedURIRequestLine() {
        String method = GET.method();
        String uri = FOOBAR.uri();
        String version = HTTP_1_1.version();
        return Arrays.asList(method, uri, version);
    }

    private List<String> createKnownGETRequestLine() {
        String method = GET.method();
        String uri = INDEX.uri();
        String version = HTTP_1_1.version();
        return new ArrayList<>(Arrays.asList(method, uri, version));
    }

    private ArrayList<String> createPOSTRequestLine() {
        String method = POST.method();
        String uri = FORM.uri();
        String version = HTTP_1_1.version();
        return new ArrayList<>(Arrays.asList(method, uri, version));
    }

    private List<String> createPUTRequestLine() {
        String method = PUT.method();
        String uri = FORM.uri();
        String version = HTTP_1_1.version();
        return Arrays.asList(method, uri, version);
    }

    private List<String> createHEADRequestLine() {
        String method = PUT.method();
        String uri = FORM.uri();
        String version = HTTP_1_1.version();
        return Arrays.asList(method, uri, version);
    }
}
