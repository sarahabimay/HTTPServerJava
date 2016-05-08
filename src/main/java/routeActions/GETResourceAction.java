package routeActions;

import request.HTTPRequest;
import response.HTTPResponse;
import response.ResponseHTTPMessageFormatter;
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
        return createGetPayloadResponse(request, uriProcessor);
    }

    private HTTPResponse createGetPayloadResponse(HTTPRequest request, URIProcessor uriProcessor) {
        return new HTTPResponse(new ResponseHTTPMessageFormatter())
                .setStatusLine(request.version(), OK)
                .setBody(uriProcessor.read(request.uri().uri()));
    }
}
