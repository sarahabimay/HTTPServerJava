package router;

import exceptions.ResourceManagementException;
import exceptions.ServerErrorHandler;
import request.HTTPRequest;
import response.HTTPResponse;
import routeActions.RouteAction;
import routeActions.URIProcessor;

import java.util.List;

public class RouteProcessor {
    private final Router router;
    private final URIProcessor uriProcessor;
    private final ServerErrorHandler errorHandler;

    public RouteProcessor(Router router, URIProcessor uriProcessor, ServerErrorHandler errorHandler) {
        this.router = router;
        this.uriProcessor = uriProcessor;
        this.errorHandler = errorHandler;
    }

    public HTTPResponse buildResponse(HTTPRequest request) {
        try {
            RouteAction routeAction = selectRouteAction(router.findRouteActions(request), request);
            return routeAction.generateResponse(request, router, uriProcessor);
        } catch (ResourceManagementException e) {
            HTTPRequest serverErrorRequest = buildInvalidRequest(e);
            RouteAction routeAction = selectRouteAction(router.findRouteActions(serverErrorRequest), serverErrorRequest);
            return routeAction.generateResponse(serverErrorRequest, router, uriProcessor);
        }
    }

    private HTTPRequest buildInvalidRequest(ResourceManagementException e) {
        return errorHandler.buildInvalidRequest(e.getMessage());
    }

    private RouteAction selectRouteAction(List<RouteAction> routeActions, HTTPRequest request) {
        return routeActions.stream().filter(routeAction -> routeAction.isAppropriate(request)).findFirst().get();
    }
}
