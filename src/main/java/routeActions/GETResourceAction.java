package routeActions;

import request.HTTPRequest;
import response.HTTPResponse;
import router.Router;

import static request.HTTPMethod.GET;
import static response.HTTPStatusCode.OK;

public class GETResourceAction implements RouteAction {
    @Override
    public boolean isAppropriate(HTTPRequest request) {
        return request.method() == GET;
    }

    @Override
    public HTTPResponse generateResponse(HTTPRequest request, Router router, URIProcessor uriProcessor) {
        HTTPResponse response = new HTTPResponse();
        response.setStatusLine(request.version(), OK);
        response.setBody(uriProcessor.read(request.uri().uri()));
        return response;
    }
}
