package routeActions;

import request.HTTPMethod;
import request.HTTPRequest;
import response.EntityHeaderFields;
import response.HTTPResponse;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static request.HTTPMethod.GET;
import static request.HTTPMethod.OPTIONS;
import static response.EntityHeaderFields.ALLOW;
import static response.HTTPStatusCode.OK;

public class AllowGETAndOPTIONSAction implements RouteAction {
    private final List<HTTPMethod> ALLOWED_METHODS = Arrays.asList(GET, OPTIONS);

    @Override
    public HTTPResponse generateResponse(HTTPRequest request, URIProcessor uriProcessor) {
        HTTPResponse response = new HTTPResponse();
        response.setStatusLine(request.version(), OK);
        setEntityHeaders(response);
        return response;
    }

    private void setEntityHeaders(HTTPResponse response) {
        Map<EntityHeaderFields, List<HTTPMethod>> headers = new HashMap<>();
        headers.put(ALLOW, ALLOWED_METHODS);
        response.setEntityHeaders(headers);
    }
}
