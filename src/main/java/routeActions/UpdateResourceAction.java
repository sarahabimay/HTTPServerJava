package routeActions;

import request.HTTPRequest;
import response.HTTPResponse;
import router.Router;

import static response.HTTPStatusCode.OK;

public class UpdateResourceAction implements RouteAction {
    @Override
    public boolean isAppropriate(HTTPRequest request) {
        return true;
    }

    @Override
    public HTTPResponse generateResponse(HTTPRequest request, Router router, URIProcessor uriProcessor) {
        HTTPResponse response = new HTTPResponse();
        response.setStatusLine(request.version(), OK);
        uriProcessor.create(request.uri().uri(), request.body());
        return response;
    }
}
