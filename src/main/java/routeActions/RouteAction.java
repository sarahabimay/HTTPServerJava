package routeActions;

import request.HTTPRequest;
import response.HTTPResponse;

public interface RouteAction {
    boolean isAppropriate(HTTPRequest request);

    HTTPResponse generateResponse(HTTPRequest request);
}
