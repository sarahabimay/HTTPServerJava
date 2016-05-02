import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RouteFactoryTest {
//    @Test
//    public void getRoutes() {
//        RoutesFactory routesFactory = new RoutesFactory();
//        Map<Route, List<RequestController>> routes = routesFactory.routes();
//        List<Route> expectedRoutes = routes();
//        assertEquals(expectedRoutes.size(), routes.size());
//    }

    @Test
    public void getListOfRoutes() {
        RoutesFactory routesFactory = new RoutesFactory();
        List<Route> routes = routesFactory.routes();
        List<Route> expectedRoutes = routes();
        assertEquals(expectedRoutes.size(), routes.size());
    }

    private List<Route> routes() {
        List<Route> routes = new ArrayList<>();
        routes.add(new Route(HTTPMethod.GET, HTTPRequestURI.INDEX, HTTPVersion.HTTP_1_1));
        routes.add(new Route(HTTPMethod.PUT, HTTPRequestURI.FORM, HTTPVersion.HTTP_1_1));
        routes.add(new Route(HTTPMethod.POST, HTTPRequestURI.FORM, HTTPVersion.HTTP_1_1));
        routes.add(new Route(HTTPMethod.HEAD, HTTPRequestURI.INDEX, HTTPVersion.HTTP_1_1));
        routes.add(new Route(HTTPMethod.OPTIONS, HTTPRequestURI.INDEX, HTTPVersion.HTTP_1_1));
        return routes;
    }
}
