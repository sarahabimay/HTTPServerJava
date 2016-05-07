package routeActions;

import request.HTTPRequest;
import response.HTTPResponse;
import response.ResponseHTTPMessageFormatter;
import router.Router;

import static request.HTTPMethod.DELETE;
import static response.HTTPStatusCode.OK;

public class DeleteResourceAction implements RouteAction {
    @Override
    public boolean isAppropriate(HTTPRequest request) {
        return request.method() == DELETE;
    }

    public HTTPResponse generateResponse(HTTPRequest request, Router router, URIProcessor uriProcessor) {
        uriProcessor.delete(request.uri().uri());
        return new HTTPResponse(new ResponseHTTPMessageFormatter()).setStatusLine(request.version(), OK);
    }
}
