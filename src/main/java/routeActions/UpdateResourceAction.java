package routeActions;

import request.HTTPRequest;
import response.HTTPResponse;
import response.HTTPStatusCode;
import router.Router;

public class UpdateResourceAction implements RouteAction {
    @Override
    public HTTPResponse generateResponse(HTTPRequest request, Router router, URIProcessor uriProcessor) {
        HTTPResponse response = new HTTPResponse();
        response.setStatusLine(request.version(), HTTPStatusCode.OK);
        uriProcessor.create(request.uri().uri(), request.body());
        return response;
    }
}
