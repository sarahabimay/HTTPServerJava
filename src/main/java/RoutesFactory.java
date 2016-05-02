import java.util.ArrayList;
import java.util.List;

public class RoutesFactory {
    public List<Route> routes() {
        List<Route> routes = new ArrayList<>();
        routes.add(new Route(HTTPMethod.HEAD, "/", "HTTP/1.1"))
        routes.add(new Route(HTTPMethod.GET, "/", "HTTP/1.1"));
        routes.add(new Route(HTTPMethod.PUT, "/form", "HTTP/1.1"));
        routes.add(new Route(HTTPMethod.POST, "/form", "HTTP/1.1"));
        routes.add(new Route(HTTPMethod.OPTIONS, "/form", "HTTP/1.1"));
        return routes;
    }
}
