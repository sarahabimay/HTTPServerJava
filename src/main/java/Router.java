import java.util.List;
import java.util.Optional;

public class Router {
    private final List<Route> routes;

    public Router(List<Route> routes) {
        this.routes = routes;
    }

    public Optional<Route> findRoute(HTTPRequest request) {
        return routes.stream().filter(r -> r.isMatch(request)).findFirst();
    }
}
