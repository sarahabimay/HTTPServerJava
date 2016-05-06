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

import static functions.FunctionHelpers.insertToList;

public class Router {
    private final Map<Route, List<RouteAction>> routeActions;

    public Router(Map<Route, List<RouteAction>> routeActions) {
        this.routeActions = routeActions;
    }

    public List<RouteAction> findRouteActions(HTTPRequest request) {
        for (Entry<Route, List<RouteAction>> entry : routeActions.entrySet()) {
            if (entry.getKey().isMatch(request)) {
                return entry.getValue();
            }
        }
        if (!resourceNotFound(request.uri())) {
            return insertToList.apply(new StatusNOKAction());
        } else {
            return insertToList.apply(new MethodNotAllowedAction());
        }
    }

    private boolean resourceNotFound(HTTPResource uri) {
        return routeActions.keySet().stream().anyMatch(route -> route.resource().equals(uri));
    }

    public List<HTTPMethod> allowedMethods(HTTPResource resource) {
        List<HTTPMethod> methods = new ArrayList<>();
        for (Entry<Route, List<RouteAction>> entry : routeActions.entrySet()) {
            if (entry.getKey().resource().uri().equals(resource.uri())) {
                methods.add(entry.getKey().method());
            }
        }
        return methods;
    }
}
