package routeActions;

import request.HTTPRequest;
import response.HTTPResponse;
import response.ResponseHTTPMessageFormatter;
import router.Router;

import static response.HTTPStatusCode.OK;

public class StatusOKAction implements RouteAction {
    @Override
    public boolean isAppropriate(HTTPRequest request) {
        return true;
    }

    @Override
    public HTTPResponse generateResponse(HTTPRequest request, Router router, URIProcessor uriProcessor) {
        return new HTTPResponse(new ResponseHTTPMessageFormatter()).setStatusLine(request.version(), OK);
    }
}
