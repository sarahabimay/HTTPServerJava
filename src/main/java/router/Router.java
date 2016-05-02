package router;

import request.HTTPRequest;
import routeActions.RouteAction;
import routeActions.StatusNOKAction;

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
        return insertToList.apply(new StatusNOKAction());
    }
}
