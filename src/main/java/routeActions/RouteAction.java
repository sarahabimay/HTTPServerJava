package routeActions;

import request.HTTPRequest;
import response.HTTPResponse;

public interface RouteAction {
    HTTPResponse generateResponse(HTTPRequest request);
}
