package router;

import routeActions.AllowAllOptionsAction;
import routeActions.AllowGETAndOPTIONSAction;
import routeActions.RouteAction;
import routeActions.StatusOKAction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static functions.FunctionHelpers.insertToList;
import static request.HTTPMethod.*;
import static request.HTTPRequestURI.*;
import static request.HTTPVersion.HTTP_1_1;

public class RoutesFactory {
    public Map<Route, List<RouteAction>> routeActions() {
        Map<Route, List<RouteAction>> routeActions = new HashMap<>();
        routeActions.put(new Route(HEAD, INDEX, HTTP_1_1), insertToList.apply(new StatusOKAction()));
        routeActions.put(new Route(GET, INDEX, HTTP_1_1), insertToList.apply(new StatusOKAction()));
        routeActions.put(new Route(GET, FORM, HTTP_1_1), insertToList.apply(new StatusOKAction()));
        routeActions.put(new Route(PUT, FORM, HTTP_1_1), insertToList.apply(new StatusOKAction()));
        routeActions.put(new Route(POST, FORM, HTTP_1_1), insertToList.apply(new StatusOKAction()));
        routeActions.put(new Route(OPTIONS, OPTIONS_ONE, HTTP_1_1), insertToList.apply(new AllowAllOptionsAction()));
        routeActions.put(new Route(GET, OPTIONS_ONE, HTTP_1_1), insertToList.apply(new StatusOKAction()));
        routeActions.put(new Route(PUT, OPTIONS_ONE, HTTP_1_1), insertToList.apply(new StatusOKAction()));
        routeActions.put(new Route(POST, OPTIONS_ONE, HTTP_1_1), insertToList.apply(new StatusOKAction()));
        routeActions.put(new Route(HEAD, OPTIONS_ONE, HTTP_1_1), insertToList.apply(new StatusOKAction()));
        routeActions.put(new Route(OPTIONS, OPTIONS_TWO, HTTP_1_1), insertToList.apply(new AllowGETAndOPTIONSAction()));
        routeActions.put(new Route(GET, OPTIONS_TWO, HTTP_1_1), insertToList.apply(new StatusOKAction()));
        return routeActions;
    }
}