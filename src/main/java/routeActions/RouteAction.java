package routeActions;

import request.HTTPRequest;
import response.HTTPResponse;
import router.Router;

public interface RouteAction {
    HTTPResponse generateResponse(HTTPRequest request, Router router, URIProcessor uriProcessor);
}
