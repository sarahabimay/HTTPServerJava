package routeActions;

import configuration.Configuration;
import request.HTTPMethod;
import request.HTTPRequest;
import request.HTTPResource;
import response.EntityHeaderFields;
import response.HTTPResponse;
import response.ResponseHTTPMessageFormatter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static request.HTTPMethod.OPTIONS;
import static response.EntityHeaderFields.ALLOW;
import static response.HTTPStatusCode.OK;

public class AllowOptionsAction implements RouteAction {
    private final Configuration configuration;

    public AllowOptionsAction(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public boolean isAppropriate(HTTPRequest request) {
        return request.method() == OPTIONS;
    }

    @Override
    public HTTPResponse generateResponse(HTTPRequest request) {
        return new HTTPResponse(new ResponseHTTPMessageFormatter())
                .setStatusLine(request.version(), OK)
                .setEntityHeaders(allowedMethodsHeader(request.uri()));
    }

    private Map<EntityHeaderFields, List<String>> allowedMethodsHeader(HTTPResource resource) {
        Map<EntityHeaderFields, List<String>> allowed = new HashMap<>();
        allowed.put(ALLOW, findAllowedMethods(resource));
        return allowed;
    }

    private List<String> findAllowedMethods(HTTPResource resource) {
        List<String> excludedMethods = configuration.methodsNotAllowed().get(resource);
        return HTTPMethod.httpMethods()
                .stream()
                .filter(httpMethod -> excludedMethods == null || !excludedMethods.contains(httpMethod))
                .collect(Collectors.toList());
    }
}
