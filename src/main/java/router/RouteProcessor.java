package router;

import request.HTTPRequest;
import response.HTTPResponse;

public class RouteProcessor {
    private final Router router;

    public RouteProcessor(Router router) {
        this.router = router;
    }

    public HTTPResponse buildResponse(HTTPRequest request) {
        return router
                .findRouteActions(request)
                .get(0)
                .generateResponse(request);
    }
}
