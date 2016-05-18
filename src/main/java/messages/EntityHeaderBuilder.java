package messages;

import configuration.Configuration;
import request.HTTPMethod;
import request.HTTPResource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static messages.EntityHeaderFields.*;
import static request.HTTPMethod.*;

public class EntityHeaderBuilder {
    private final Configuration configuration;
    private Map<EntityHeaderFields, List<String>> allowHeader;

    public EntityHeaderBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public EntityHeaderBuilder createALLOWHeader(HTTPMethod method, HTTPResource resource) {
        this.allowHeader = allowHeader(method, resource);
        return this;
    }

    public Map<EntityHeaderFields, List<String>> buildAllHeaders() {
        Map<EntityHeaderFields, List<String>> allHeaders = new HashMap<>();
        allHeaders.putAll(allowHeader);
        return allHeaders;
    }

    private Map<EntityHeaderFields, List<String>> allowHeader(HTTPMethod method, HTTPResource resource) {
        return method == UNDEFINED ? noALLOWHeader() : createALLOWHeader(resource);
    }

    private Map<EntityHeaderFields, List<String>> noALLOWHeader() {
        return new HashMap<>();
    }

    private Map<EntityHeaderFields, List<String>> createALLOWHeader(HTTPResource resource) {
        Map<EntityHeaderFields, List<String>> allowed = new HashMap<>();
        allowed.put(ALLOW, allowedMethods(resource));
        return allowed;
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
