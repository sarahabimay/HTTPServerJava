package router;

import request.HTTPRequest;
import response.HTTPResponse;
import routeActions.RouteAction;
import routeActions.URIProcessor;

import java.util.List;

public class RouteProcessor {
    private final Router router;
    private final URIProcessor uriProcessor;

    public RouteProcessor(Router router, URIProcessor uriProcessor) {
        this.router = router;
        this.uriProcessor = uriProcessor;
    }

    public HTTPResponse buildResponse(HTTPRequest request) {
        return selectRouteAction(router.findRouteActions(request), request)
                .generateResponse(request, router, uriProcessor);
    }

    private RouteAction selectRouteAction(List<RouteAction> routeActions, HTTPRequest request) {
        return routeActions.stream().filter(routeAction -> routeAction.isAppropriate(request)).findFirst().get();
    }
}
