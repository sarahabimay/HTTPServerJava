package routeActions;

import request.HTTPRequest;
import response.HTTPResponse;
import response.ResponseHTTPMessageFormatter;

import static response.HTTPStatusCode.NOT_FOUND;

public class StatusNOKAction implements RouteAction {
    private final URIProcessor uriProcessor;

    public StatusNOKAction(URIProcessor uriProcessor) {
        this.uriProcessor = uriProcessor;
    }

    @Override
    public boolean isAppropriate(HTTPRequest request) {
        return !uriProcessor.exists(request.uri().uri());
    }

    public HTTPResponse generateResponse(HTTPRequest request) {
        return new HTTPResponse(new ResponseHTTPMessageFormatter()).setStatusLine(request.version(), NOT_FOUND);
    }
}
