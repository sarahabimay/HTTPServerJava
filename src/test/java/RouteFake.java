import java.util.Optional;

public class RouteFake extends Router {
    private final Route route;

    public RouteFake() {
        super(null);
        this.route = new Route(HTTPMethod.GET, HTTPRequestURI.INDEX, HTTPVersion.HTTP_1_1);
    }

    @Override
    public Optional<Route> findRoute(HTTPRequest request) {
        return Optional.of(route);
    }
}
