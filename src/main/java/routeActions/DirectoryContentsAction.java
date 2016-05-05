package routeActions;

import request.HTTPRequest;
import response.HTTPResponse;

import static response.HTTPStatusCode.OK;

public class DirectoryContentsAction implements RouteAction {
    @Override
    public HTTPResponse generateResponse(HTTPRequest request, URIProcessor uriProcessor) {
        HTTPResponse response = new HTTPResponse();
        response.setStatusLine(request.version(), OK);
        response.setBody(uriProcessor.links());
        return response;
    }
}