import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RouteFactoryTest {
    @Test
    public void getListOfRoutes() {
        RoutesFactory routesFactory = new RoutesFactory();
        List<Route> routes = routesFactory.routes();
        List<Route> expectedRoutes = routes();
        assertEquals(expectedRoutes.size(), routes.size());
    }

    private List<Route> routes() {
        List<Route> routes = new ArrayList<>();
        routes.add(new Route(HTTPMethod.GET, "/", "HTTP/1.1"));
        routes.add(new Route(HTTPMethod.PUT, "/form", "HTTP/1.1"));
        routes.add(new Route(HTTPMethod.POST, "/form", "HTTP/1.1"));
        routes.add(new Route(HTTPMethod.HEAD, "/", "HTTP/1.1"));
        routes.add(new Route(HTTPMethod.OPTIONS, "/", "HTTP/1.1"));
        return routes;
    }
}
