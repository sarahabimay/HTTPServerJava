package router;

import request.HTTPRequest;
import response.HTTPResponse;
import routeActions.URIProcessor;

public class RouteProcessor {
    private final Router router;
    private final URIProcessor uriProcessor;

    public RouteProcessor(Router router, URIProcessor uriProcessor) {
        this.router = router;
        this.uriProcessor = uriProcessor;
    }

    public HTTPResponse buildResponse(HTTPRequest request) {
        return router
                .findRouteActions(request)
                .get(0)
                .generateResponse(request, uriProcessor);
    }
}
