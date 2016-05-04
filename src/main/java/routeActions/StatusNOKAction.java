package routeActions;

import request.HTTPRequest;
import response.HTTPResponse;

import static response.HTTPStatusCode.NOT_FOUND;

public class StatusNOKAction implements RouteAction {
    public HTTPResponse generateResponse(HTTPRequest request, URIProcessor uriProcessor) {
        HTTPResponse response = new HTTPResponse();
        response.setStatusLine(request.version(), NOT_FOUND);
        return response;
    }
}
