import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class RouterTest {
    private List<Route> routes;
    private Router router;

    @Before
    public void setUp() {
        routes = getRoutes();
        router = new Router(routes);
    }

    @Test
    public void findRouteForIndex() {
        Optional<Route> route = router.findRoute(
                ImmutableMap.of(
                        "method", "GET",
                        "path", "/",
                        "version", "HTTP/1.1"));
        assertEquals(true, route.isPresent());
    }

    @Test
    public void routeNotFound() {
        Optional<Route> route = router.findRoute(
                ImmutableMap.of(
                        "method", "GET",
                        "path", "/foobar",
                        "version", "HTTP/1.1"));
        assertEquals(false, route.isPresent());
    }

    private List<Route> getRoutes() {
        List<Route> routes = new ArrayList<>();
        routes.add(new Route("GET", "/"));
        return routes;
    }
}
