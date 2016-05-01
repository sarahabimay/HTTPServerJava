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
        parsedRequestSpy.addRequestLine(Arrays.asList("GET", "/foobar", "HTTP/1.1"));
        Optional<Route> route = router.findRoute(parsedRequestSpy);
        assertEquals(false, route.isPresent());
    }

    @Test
    public void findRouteForIndex() {
        parsedRequestSpy.addRequestLine(Arrays.asList("GET", "/", "HTTP/1.1"));
        Optional<Route> route = router.findRoute(parsedRequestSpy);
        assertEquals(true, route.isPresent());
    }

    private List<Route> getRoutes() {
        List<Route> routes = new ArrayList<>();
        routes.add(new Route("GET", "/", "HTTP/1.1"));
        return routes;
    }
}
