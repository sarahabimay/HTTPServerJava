package router;

import request.HTTPMethod;
import routeActions.RouteAction;

import java.util.List;
import java.util.Map;

public class RoutesFactoryFake extends RoutesFactory {
    private final Map<HTTPMethod, List<RouteAction>> routeActions;

    public RoutesFactoryFake(Map<HTTPMethod, List<RouteAction>> routeActions) {
        super(null, null);
        this.routeActions = routeActions;
    }

    @Override
    public Map<HTTPMethod, List<RouteAction>> routeActions() {
        return routeActions;
    }
}
