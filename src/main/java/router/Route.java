package router;

import request.HTTPMethod;
import request.HTTPRequest;
import request.HTTPResource;
import request.HTTPVersion;

public class Route {
    private HTTPMethod method;
    private HTTPResource resource;
    private HTTPVersion version;

    public Route(HTTPMethod method, HTTPResource resource, HTTPVersion version) {
        this.method = method;
        this.resource = resource;
        this.version = version;
    }

    public HTTPMethod method() {
        return method;
    }

    public HTTPResource resource() {
        return resource;
    }

    public boolean isMatch(HTTPRequest request) {
        boolean isURIMatch = request.uri().uri().equals(resource.uri());
        boolean isMethodMatch = request.method().method().equals(method.method());
        boolean isVersionMatch = request.version().version().equals(version.version());

        return isURIMatch && isMethodMatch && isVersionMatch;
    }
}
