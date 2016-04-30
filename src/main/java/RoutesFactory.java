import java.util.ArrayList;
import java.util.List;

public class RoutesFactory {
    public List<Route> routes() {
        List<Route> routes = new ArrayList<>();
        routes.add(new Route("HEAD", "/"));
        routes.add(new Route("GET", "/"));
        routes.add(new Route("PUT", "/form"));
        routes.add(new Route("POST", "/form"));
        return routes;
    }
}
