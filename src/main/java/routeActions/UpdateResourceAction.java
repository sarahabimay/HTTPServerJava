package routeActions;

import request.HTTPRequest;
import response.HTTPResponse;
import response.ResponseHTTPMessageFormatter;

import static response.HTTPStatusCode.OK;

public class UpdateResourceAction implements RouteAction {
    private final URIProcessor uriProcessor;

    public UpdateResourceAction(URIProcessor uriProcessor) {
        this.uriProcessor = uriProcessor;
    }

    @Override
    public boolean isAppropriate(HTTPRequest request) {
        return true;
    }

    @Override
    public HTTPResponse generateResponse(HTTPRequest request) {
        uriProcessor.create(request.uri().uri(), request.body());
        return new HTTPResponse(new ResponseHTTPMessageFormatter()).setStatusLine(request.version(), OK);
    }
}
