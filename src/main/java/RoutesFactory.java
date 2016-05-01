import java.util.ArrayList;
import java.util.List;

public class RoutesFactory {
    public List<Route> routes() {
        List<Route> routes = new ArrayList<>();
        routes.add(new Route("HEAD", "/", "HTTP/1.1"));
        routes.add(new Route("GET", "/", "HTTP/1.1"));
        routes.add(new Route("PUT", "/form", "HTTP/1.1"));
        routes.add(new Route("POST", "/form", "HTTP/1.1"));
        return routes;
    }
}
