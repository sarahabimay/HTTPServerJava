package routeActions;

import request.HTTPRequest;
import response.HTTPResponse;
import response.ResponseHTTPMessageFormatter;
import router.Router;

import static request.HTTPVersion.HTTP_1_1;
import static response.HTTPStatusCode.SERVER_ERROR;

public class InternalServerErrorAction implements RouteAction {
    @Override
    public boolean isAppropriate(HTTPRequest request) {
        return true;
    }

    @Override
    public HTTPResponse generateResponse(HTTPRequest request, Router router, URIProcessor uriProcessor) {
        return new HTTPResponse(new ResponseHTTPMessageFormatter()).setStatusLine(HTTP_1_1, SERVER_ERROR);
    }
}
