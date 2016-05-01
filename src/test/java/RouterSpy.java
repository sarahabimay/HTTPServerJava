import java.util.Optional;

public class RouterSpy extends Router {
    private final Route foundRoute;
    private boolean hasFoundRoute = false;

    public RouterSpy(Route foundRoute) {
        super(null);
        this.foundRoute = foundRoute;
    }

    @Override
    public Optional<Route> findRoute(HTTPRequest request) {
        hasFoundRoute = true;
        return Optional.of(foundRoute);
    }

    public boolean hasFoundRoute() {
        return hasFoundRoute;
    }
}
