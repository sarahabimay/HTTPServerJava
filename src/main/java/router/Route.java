package router;

import request.HTTPMethod;
import request.HTTPRequest;
import request.HTTPRequestURI;
import request.HTTPVersion;

public class Route {
    private HTTPMethod method;
    private HTTPRequestURI path;
    private HTTPVersion version;

    public Route(HTTPMethod method, HTTPRequestURI path, HTTPVersion version) {
        this.method = method;
        this.path = path;
        this.version = version;
    }

    public boolean isMatch(HTTPRequest request) {
        boolean isURIMatch = request.uri().uri().equals(path.uri());
        boolean isMethodMatch = request.method().method().equals(method.method());
        boolean isVersionMatch = request.version().version().equals(version.version());

        return isURIMatch && isMethodMatch && isVersionMatch;
    }
}
