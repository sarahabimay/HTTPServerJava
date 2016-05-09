package router;

import org.junit.Before;
import org.junit.Test;
import request.HTTPMethod;
import request.HTTPRequest;
import request.HTTPResource;
import request.HTTPVersion;
import routeActions.*;

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
    @Before
    public void setUp() {
        router = new Router(new RoutesFactory().routeActions());
    }

    @Test
    public void fourOhFourRouteActionForResourceNotFound() {
        HTTPRequest request = unknownResourceRequest();
        List<RouteAction> actions = router.findRouteActions(request);
        assertEquals(StatusNOKAction.class, actions.get(0).getClass());
    }

    @Test
    public void okActionFoundForGETRequest() {
        HTTPRequest request = knownResourceGETRequest();
        List<RouteAction> actions = router.findRouteActions(request);
        assertEquals(GETResourceAction.class, actions.get(0).getClass());
    }

    @Test
    public void methodNotAllowedActionFound() {
        HTTPRequest request = bogusMethodRequest();
        List<RouteAction> actions = router.findRouteActions(request);
        assertEquals(MethodNotAllowedAction.class, actions.get(0).getClass());
    }

    @Test
    public void updateResourceActionFoundForPOSTRequest() {
        HTTPRequest request = knownResourcePOSTRequest();
        List<RouteAction> actions = router.findRouteActions(request);
        assertEquals(UpdateResourceAction.class, actions.get(0).getClass());
    }

    @Test
    public void updateResourceActionFoundForPUTRequest() {
        HTTPRequest request = knownResourcePUTRequest();
        List<RouteAction> actions = router.findRouteActions(request);
        assertEquals(UpdateResourceAction.class, actions.get(0).getClass());
    }

    @Test
    public void directoryContentsActionFound() {
        HTTPRequest request = getIndexResourceRequest();
        List<RouteAction> actions = router.findRouteActions(request);
        assertEquals(DirectoryContentsAction.class, actions.get(0).getClass());
    }

    @Test
    public void partialContentActionFound() {
        HTTPRequest request = getResourceAllowingPartialContentRequest();
        List<RouteAction> actions = router.findRouteActions(request);
        assertEquals(PartialContentAction.class, actions.get(0).getClass());
    }

    @Test
    public void parameterDecodeActionFound() {
        HTTPRequest request = getParameterRequest();
        List<RouteAction> actions = router.findRouteActions(request);
        assertEquals(ParameterDecodeAction.class, actions.get(0).getClass());
    }

    @Test
    public void patchContentActionFound() {
        HTTPRequest request = getResourceAllowingPatch();
        List<RouteAction> actions = router.findRouteActions(request);
        assertEquals(PatchContentAction.class, actions.get(0).getClass());
    }

    @Test
    public void authenticateActionFound() {
        HTTPRequest request = getResourceRequiringAuth();
        List<RouteAction> actions = router.findRouteActions(request);
        assertEquals(AuthenticateAction.class, actions.get(0).getClass());
    }

    @Test
    public void teapotActionFound() {
        HTTPRequest request = getCoffeeResource();
        List<RouteAction> actions = router.findRouteActions(request);
        assertEquals(IAmATeapotAction.class, actions.get(0).getClass());
    }

    @Test
    public void okActionForHEADFound() {
        HTTPRequest request = knownResourceHEADRequest();
        List<RouteAction> actions = router.findRouteActions(request);
        assertEquals(StatusOKAction.class, actions.get(0).getClass());
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
        return new HTTPRequest().addRequestLine(createRequestLine(GET, PATCH_CONTENT, "", HTTP_1_1));
    }

    private HTTPRequest getIndexResourceRequest() {
        return new HTTPRequest().addRequestLine(createRequestLine(GET, INDEX, "", HTTP_1_1));
    }

    private HTTPRequest getResourceAllowingPartialContentRequest() {
        return new HTTPRequest().addRequestLine(createRequestLine(GET, FILE1, "", HTTP_1_1));
    }

    private HTTPRequest getParameterRequest() {
        return new HTTPRequest().addRequestLine(createRequestLine(GET, PARAMETERS, "", HTTP_1_1));
    }

    private HTTPRequest getCoffeeResource() {
        return new HTTPRequest().addRequestLine(createRequestLine(GET, COFFEE, "", HTTP_1_1));
    }

    private HTTPRequest getResourceRequiringAuth() {
        return new HTTPRequest().addRequestLine(createRequestLine(GET, LOGS, "", HTTP_1_1));
    }

    private HTTPRequest getResourceAllowingPatch() {
        return new HTTPRequest().addRequestLine(createRequestLine(PATCH, PATCH_CONTENT, "", HTTP_1_1));
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
