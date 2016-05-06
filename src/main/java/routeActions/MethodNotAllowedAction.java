package routeActions;

import request.HTTPMethod;
import request.HTTPRequest;
import response.EntityHeaderFields;
import response.HTTPResponse;
import router.Router;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static request.HTTPVersion.HTTP_1_1;
import static response.EntityHeaderFields.ALLOW;
import static response.HTTPStatusCode.METHOD_NOT_ALLOWED;

public class MethodNotAllowedAction implements RouteAction{
    @Override
    public HTTPResponse generateResponse(HTTPRequest request, Router router, URIProcessor uriProcessor) {
        HTTPResponse response = new HTTPResponse();
        response.setStatusLine(HTTP_1_1, METHOD_NOT_ALLOWED);
        response.setEntityHeaders(allowedMethodsHeader(router.allowedMethods(request.uri())));
        return response;
    }

    private Map<EntityHeaderFields,List<HTTPMethod>> allowedMethodsHeader(List<HTTPMethod> methods) {
        Map<EntityHeaderFields, List<HTTPMethod>> allowed = new HashMap<>();
        allowed.put(ALLOW, methods);
        return allowed;
    }
}
