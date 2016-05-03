package routeActions;

import request.HTTPMethod;
import request.HTTPRequest;
import response.EntityHeaderFields;
import response.HTTPResponse;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static request.HTTPMethod.*;
import static response.EntityHeaderFields.*;
import static response.HTTPStatusCode.OK;

public class AllowAllOptionsAction implements RouteAction {

    private final List<HTTPMethod> ALLOWED_METHODS = Arrays.asList(GET, HEAD, POST, OPTIONS, PUT);

    @Override
    public HTTPResponse generateResponse(HTTPRequest request) {
        HTTPResponse response = new HTTPResponse();
        response.setStatusLine(request.version(), OK);
        setEntityHeaders(response);
        return response;
    }

    private void setEntityHeaders(HTTPResponse response) {
        Map<EntityHeaderFields, List<HTTPMethod>> headers = new HashMap<>();
        headers.put(Allow, ALLOWED_METHODS);
        response.setEntityHeaders(headers);
    }
}
