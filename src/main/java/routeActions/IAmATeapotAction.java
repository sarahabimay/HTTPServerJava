package routeActions;

import request.HTTPRequest;
import response.HTTPResponse;
import response.HTTPStatusCode;
import response.ResponseHTTPMessageFormatter;
import router.Router;

import static request.HTTPMethod.GET;
import static request.HTTPResource.COFFEE;
import static request.HTTPResource.TEA;
import static request.HTTPVersion.HTTP_1_1;
import static response.HTTPStatusCode.FOUR_EIGHTEEN;
import static response.HTTPStatusCode.OK;

public class IAmATeapotAction implements RouteAction {
    @Override
    public boolean isAppropriate(HTTPRequest request) {
        return request.method() == GET && (request.uri() == COFFEE || request.uri() == TEA);
    }

    @Override
    public HTTPResponse generateResponse(HTTPRequest request, Router router, URIProcessor uriProcessor) {
        if (request.uri() != TEA) {
            return new HTTPResponse(new ResponseHTTPMessageFormatter())
                    .setStatusLine(HTTP_1_1, FOUR_EIGHTEEN)
                    .setBody(HTTPStatusCode.FOUR_EIGHTEEN.reason().getBytes());
        } else {
            return new HTTPResponse(new ResponseHTTPMessageFormatter()).setStatusLine(HTTP_1_1, OK);
        }
    }
}
