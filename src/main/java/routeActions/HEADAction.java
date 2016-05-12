package routeActions;

import request.HTTPRequest;
import response.HTTPResponse;
import response.ResponseHTTPMessageFormatter;

import static response.HTTPStatusCode.OK;

public class HEADAction implements RouteAction {
    @Override
    public boolean isAppropriate(HTTPRequest request) {
        return true;
    }

    @Override
    public HTTPResponse generateResponse(HTTPRequest request) {
        return new HTTPResponse(new ResponseHTTPMessageFormatter()).setStatusLine(request.version(), OK);
    }
}
