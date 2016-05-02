package routeActions;

import request.HTTPRequest;
import response.HTTPResponse;

import static response.HTTPStatusCode.OK;

public class StatusOKAction implements RouteAction {
    public HTTPResponse generateResponse(HTTPRequest request) {
        HTTPResponse response = new HTTPResponse();
        response.setStatusLine(request.version(), OK);
        response.setBody(request.body());
        return response;
    }
}
