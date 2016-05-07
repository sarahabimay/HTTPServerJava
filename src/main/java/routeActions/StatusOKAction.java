package routeActions;

import request.HTTPRequest;
import response.HTTPResponse;
import router.Router;

import static response.HTTPStatusCode.OK;

public class StatusOKAction implements RouteAction {
    @Override
    public boolean isAppropriate(HTTPRequest request) {
        return true;
    }

    @Override
    public HTTPResponse generateResponse(HTTPRequest request, Router router, URIProcessor uriProcessor) {
        HTTPResponse response =
                new HTTPResponse()
                        .setStatusLine(request.version(), OK);
        return response;
    }
}
