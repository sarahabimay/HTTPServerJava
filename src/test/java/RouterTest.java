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
    public void fourOhFourAction() {
        HTTPRequest request = unrecognisedResourceRequest();
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

    private HTTPRequest unrecognisedResourceRequest() {
        return new HTTPRequest().addRequestLine(createGETUnrecognisedURIRequestLine());
    }

    private List<String> createGETUnrecognisedURIRequestLine() {
        String method = GET.method();
        String uri = FOOBAR.uri();
        String version = HTTP_1_1.version();
        return Arrays.asList(method, uri, version);
    }

    private HTTPRequest knownResourceGETRequest() {
        return new HTTPRequest().addRequestLine(createKnownGETRequestLine());
    }

    private List<String> createKnownGETRequestLine() {
        String method = GET.method();
        String uri = INDEX.uri();
        String version = HTTP_1_1.version();
        return new ArrayList<>(Arrays.asList(method, uri, version));
    }

    private HTTPRequest knownResourcePOSTRequest() {
        return new HTTPRequest().addRequestLine(createPOSTRequestLine());
    }

    private ArrayList<String> createPOSTRequestLine() {
        String method = POST.method();
        String uri = FORM.uri();
        String version = HTTP_1_1.version();
        return new ArrayList<>(Arrays.asList(method, uri, version));
    }

    private HTTPRequest knownResourcePUTRequest() {
        return new HTTPRequest().addRequestLine(createPUTRequestLine());
    }

    private List<String> createPUTRequestLine() {
        String method = PUT.method();
        String uri = FORM.uri();
        String version = HTTP_1_1.version();
        return Arrays.asList(method, uri, version);
    }

    private HTTPRequest knownResourceHEADRequest() {
        return new HTTPRequest().addRequestLine(createHEADRequestLine());
    }

    private List<String> createHEADRequestLine() {
        String method = PUT.method();
        String uri = FORM.uri();
        String version = HTTP_1_1.version();
        return Arrays.asList(method, uri, version);
    }
}
