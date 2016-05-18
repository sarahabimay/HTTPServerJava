package routeActions;

import configuration.Configuration;
import messages.EntityHeaderBuilder;
import messages.EntityHeaderFields;
import request.HTTPMethod;
import request.HTTPRequest;
import request.HTTPResource;
import response.HTTPResponse;
import response.ResponseHTTPMessageFormatter;

import java.util.List;
import java.util.Map;

import static request.HTTPVersion.HTTP_1_1;
import static response.HTTPStatusCode.METHOD_NOT_ALLOWED;

public class MethodNotAllowedAction implements RouteAction {
    private final Configuration configuration;
    private final EntityHeaderBuilder headerBuilder;

    public MethodNotAllowedAction(EntityHeaderBuilder entityHeaderBuilder, Configuration configuration) {
        this.headerBuilder = entityHeaderBuilder;
        this.configuration = configuration;
    }

    @Override
    public boolean isAppropriate(HTTPRequest request) {
        List<String> methods = configuration.methodsNotAllowed().get(request.uri());
        return methods != null && methods.contains(request.method().method());
    }

    @Override
    public HTTPResponse generateResponse(HTTPRequest request) {
        return new HTTPResponse(new ResponseHTTPMessageFormatter())
                .setStatusLine(HTTP_1_1, METHOD_NOT_ALLOWED)
                .setEntityHeaders(allowHeader(request.method(), request.uri()));
    }

    private Map<EntityHeaderFields, List<String>> allowHeader(HTTPMethod method, HTTPResource resource) {
        return headerBuilder.createALLOWHeader(method, resource).buildAllHeaders();
    }
}
