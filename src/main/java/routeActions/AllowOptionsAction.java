package routeActions;

import request.HTTPMethod;
import request.HTTPRequest;
import response.EntityHeaderFields;
import response.HTTPResponse;
import router.Router;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static response.EntityHeaderFields.ALLOW;
import static response.HTTPStatusCode.OK;

public class AllowOptionsAction implements RouteAction {

    @Override
    public HTTPResponse generateResponse(HTTPRequest request, Router router, URIProcessor uriProcessor) {
        HTTPResponse response = new HTTPResponse();
        response.setStatusLine(request.version(), OK);
        response.setEntityHeaders(allowedMethodsHeader(router.allowedMethods(request.uri())));
        return response;
    }

    private Map<EntityHeaderFields, List<String>> allowedMethodsHeader(List<String> methods) {
        Map<EntityHeaderFields, List<String>> allowed = new HashMap<>();
        allowed.put(ALLOW, methods);
        return allowed;
    }
}
