package router;

import exceptions.ServerErrorHandler;
import request.HTTPRequest;
import response.HTTPResponse;
import routeActions.URIProcessor;

public class RouteProcessorSpy extends RouteProcessor {
    private boolean hasRequestBeenParsed = false;

    public RouteProcessorSpy(Router router, URIProcessor uriProcessor, ServerErrorHandler errorHandler) {
        super(router, uriProcessor, errorHandler);
    }

    @Override
    public HTTPResponse buildResponse(HTTPRequest request) {
        hasRequestBeenParsed = true;
        return null;
    }

    public boolean hasRequestBeenParsed() {
        return hasRequestBeenParsed;
    }
}
