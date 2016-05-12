package router;

import configuration.Configuration;
import exceptions.ResourceManagementException;
import exceptions.ServerErrorHandler;
import request.HTTPRequest;
import response.HTTPResponse;
import routeActions.MethodNotAllowedAction;
import routeActions.RouteAction;

import java.util.List;
import java.util.Optional;

import static request.HTTPMethod.ERROR;

public class RouteProcessor {
    private final ServerErrorHandler errorHandler;
    private final RoutesFactory routesFactory;
    private final Configuration configuration;

    public RouteProcessor(RoutesFactory routesFactory, Configuration configuration, ServerErrorHandler errorHandler) {
        this.routesFactory = routesFactory;
        this.configuration = configuration;
        this.errorHandler = errorHandler;
    }

    public HTTPResponse buildResponse(HTTPRequest request) {
        try {
            RouteAction routeAction = selectRouteAction(routesFactory.routeActions().get(request.method()), request);
            return routeAction.generateResponse(request);
        } catch (ResourceManagementException e) {
            HTTPRequest serverErrorRequest = buildInvalidRequest(e);
            RouteAction routeAction = selectRouteAction(routesFactory.routeActions().get(ERROR), serverErrorRequest);
            return routeAction.generateResponse(serverErrorRequest);
        }
    }

    private HTTPRequest buildInvalidRequest(ResourceManagementException e) {
        return errorHandler.buildInvalidRequest(e.getMessage());
    }

    private RouteAction selectRouteAction(List<RouteAction> routeActions, HTTPRequest request) {
        Optional<RouteAction> action = routeActions.stream().filter(routeAction -> routeAction.isAppropriate(request)).findFirst();
        return action.isPresent() ? action.get() : new MethodNotAllowedAction(configuration);
    }
}
