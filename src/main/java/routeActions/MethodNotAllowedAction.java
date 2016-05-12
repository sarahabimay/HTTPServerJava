package routeActions;

import configuration.Configuration;
import request.HTTPMethod;
import request.HTTPRequest;
import request.HTTPResource;
import response.EntityHeaderFields;
import response.HTTPResponse;
import response.ResponseHTTPMessageFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static request.HTTPMethod.*;
import static request.HTTPVersion.HTTP_1_1;
import static response.EntityHeaderFields.ALLOW;
import static response.HTTPStatusCode.METHOD_NOT_ALLOWED;

public class MethodNotAllowedAction implements RouteAction {
    private final Configuration configuration;

    public MethodNotAllowedAction(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public boolean isAppropriate(HTTPRequest request) {
        List<String> methods = configuration.methodsNotAllowed().get(request.uri());
        return methods != null ? methods.contains(request.method().method()) : false;
    }

    @Override
    public HTTPResponse generateResponse(HTTPRequest request) {
        return new HTTPResponse(new ResponseHTTPMessageFormatter())
                .setStatusLine(HTTP_1_1, METHOD_NOT_ALLOWED)
                .setEntityHeaders(allowedMethodsHeader(request.method(), request.uri()));
    }

    private Map<EntityHeaderFields, List<String>> allowedMethodsHeader(HTTPMethod method, HTTPResource resource) {
        Map<EntityHeaderFields, List<String>> allowed = new HashMap<>();
        List<String> allowedMethods = findAllowedMethods(method, resource);
        if (allowedMethods != null) {
            allowed.put(ALLOW, allowedMethods);
        }
        return allowed;
    }

    private List<String> findAllowedMethods(HTTPMethod method, HTTPResource resource) {
        return (method == UNDEFINED) ? noAllowedMethods() : allowedMethods(resource);
    }

    private List<String> noAllowedMethods() {
        return new ArrayList<>();
    }

    private List<String> allowedMethods(HTTPResource resource) {
        List<String> excludedMethods = configuration.methodsNotAllowed().get(resource);
        return httpMethods()
                .stream()
                .filter(httpMethod -> isMethodAllowed(excludedMethods, httpMethod))
                .collect(Collectors.toList());
    }

    private boolean isMethodAllowed(List<String> excludedMethods, String httpMethod) {
        return excludedMethods == null || !excludedMethods.contains(httpMethod);
    }
}
