import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class RouterTest {
    private List<Route> routes;
    private Router router;
    private HTTPRequestSpy parsedRequestSpy;

    @Before
    public void setUp() {
        routes = getRoutes();
        router = new Router(routes);
        parsedRequestSpy = new HTTPRequestSpy();
    }

    @Test
    public void routeNotFoundForParsedRequest() {
        parsedRequestSpy.addRequestLine(createGETUnrecognisedURIRequestLine());
        Optional<Route> route = router.findRoute(parsedRequestSpy);
        assertEquals(false, route.isPresent());
    }

    @Test
    public void findRouteForIndex() {
        parsedRequestSpy.addRequestLine(createGETRequestLine());
        Optional<Route> route = router.findRoute(parsedRequestSpy);
        assertEquals(true, route.isPresent());
    }

    private List<Route> getRoutes() {
        List<Route> routes = new ArrayList<>();
        routes.add(new Route(HTTPMethod.GET, HTTPRequestURI.INDEX, HTTPVersion.HTTP_1_1));
        return routes;
    }

    private ArrayList<String> createGETRequestLine() {
        String method = HTTPMethod.GET.method();
        String uri = HTTPRequestURI.INDEX.uri();
        String version = HTTPVersion.HTTP_1_1.version();
        return new ArrayList<>(Arrays.asList(method, uri, version));
    }

    private List<String> createGETUnrecognisedURIRequestLine() {
        String method = HTTPMethod.GET.method();
        String uri = HTTPRequestURI.FOOBAR.uri();
        String version = HTTPVersion.HTTP_1_1.version();
        return Arrays.asList(method, uri, version);
    }
}
