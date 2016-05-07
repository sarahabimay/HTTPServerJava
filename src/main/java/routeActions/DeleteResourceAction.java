package routeActions;

import request.HTTPRequest;
import response.HTTPResponse;
import router.Router;

import static request.HTTPMethod.DELETE;
import static response.HTTPStatusCode.OK;

public class DeleteResourceAction implements RouteAction {
    @Override
    public boolean isAppropriate(HTTPRequest request) {
        return request.method() == DELETE;
    }

    public HTTPResponse generateResponse(HTTPRequest request, Router router, URIProcessor uriProcessor) {
        HTTPResponse response = new HTTPResponse();
        response.setStatusLine(request.version(), OK);
        uriProcessor.delete(request.uri().uri());
        return response;
    }
}
