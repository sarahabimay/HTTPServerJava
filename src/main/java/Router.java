import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Router {
    private final List<Route> routes;

    public Router(List<Route> routes) {
        this.routes = routes;
    }

    public Optional<Route> findRoute(Map<String, String> requestDetails) {
        return routes.stream().filter(r -> r.isMatch(requestDetails)).findFirst();
    }
}
