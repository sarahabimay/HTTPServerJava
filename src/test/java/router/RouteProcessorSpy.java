package router;

import configuration.Configuration;
import exceptions.ServerErrorHandler;
import request.HTTPRequest;
import response.HTTPResponse;

public class RouteProcessorSpy extends RouteProcessor {
    private boolean hasRequestBeenParsed = false;

    public RouteProcessorSpy(RoutesFactory routesFactory, Configuration configuration, ServerErrorHandler errorHandler) {
        super(routesFactory, configuration, null);
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
