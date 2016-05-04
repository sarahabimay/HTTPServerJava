package routeActions;

import request.HTTPRequest;
import response.HTTPResponse;

import static response.HTTPStatusCode.OK;

public class GETResourceAction  implements RouteAction {
    @Override
    public HTTPResponse generateResponse(HTTPRequest request, URIProcessor uriProcessor) {
        HTTPResponse response = new HTTPResponse();
        response.setStatusLine(request.version(), OK);
        response.setBody(uriProcessor.read(request.uri().uri()));
        return response;
    }
}
