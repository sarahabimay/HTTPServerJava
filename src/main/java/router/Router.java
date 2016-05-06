package router;

import request.HTTPMethod;
import request.HTTPRequest;
import request.HTTPResource;
import routeActions.MethodNotAllowedAction;
import routeActions.RouteAction;
import routeActions.StatusNOKAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Router {
    private final Map<Route, RouteAction> routeActions;

    public Router(Map<Route, RouteAction> routeActions) {
        this.routeActions = routeActions;
    }

    public RouteAction findRouteActions(HTTPRequest request) {
        for (Entry<Route, RouteAction> entry : routeActions.entrySet()) {
            if (entry.getKey().isMatch(request)) {
                return entry.getValue();
            }
        }
        if (!resourceNotFound(request.uri())) {
            return new StatusNOKAction();
        } else {
            return new MethodNotAllowedAction();
        }
    }

    private boolean resourceNotFound(HTTPResource uri) {
        return routeActions.keySet().stream().anyMatch(route -> route.resource().equals(uri));
    }

    public List<HTTPMethod> allowedMethods(HTTPResource resource) {
        List<HTTPMethod> methods = new ArrayList<>();
        for (Entry<Route, RouteAction> entry : routeActions.entrySet()) {
            if (entry.getKey().resource().uri().equals(resource.uri())) {
                methods.add(entry.getKey().method());
            }
        }
        return methods;
    }
}
