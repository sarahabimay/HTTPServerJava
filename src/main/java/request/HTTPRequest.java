package request;

import response.EntityHeaderFields;

import java.util.List;
import java.util.Map;

import static request.HTTPMethod.lookupMethod;
import static request.HTTPRequestURI.UNRECOGNIZED;
import static request.HTTPRequestURI.lookupURIName;
import static request.HTTPVersion.lookupVersionName;

public class HTTPRequest {
    private HTTPMethod method;
    private HTTPRequestURI uri;
    private HTTPVersion version;
    private Map<EntityHeaderFields, List<String>> headers;
    private String body;

    public HTTPRequest() {
        this.method = HTTPMethod.UNDEFINED;
        this.uri = UNRECOGNIZED;
        this.version = HTTPVersion.UNDEFINED;
        this.body = "";
    }

    public HTTPRequest(HTTPMethod method, HTTPRequestURI uri, HTTPVersion version, Map<EntityHeaderFields, List<String>> headers, String body) {
        this.method = method;
        this.uri = uri;
        this.version = version;
        this.headers = headers;
        this.body = body;
    }

    public HTTPRequest addRequestLine(List<String> requestLine) {
        if (requestLine.size() == 3) {
            this.method = lookupMethod(requestLine.get(0));
            this.uri = lookupURIName(requestLine.get(1));
            this.version = lookupVersionName(requestLine.get(2));

        } else {
        }
        return new HTTPRequest(method, uri, version, headers, body);
    }

    public HTTPRequest addRequestHeader(Map<EntityHeaderFields, List<String>> requestHeaders) {
        return new HTTPRequest(method, uri, version, requestHeaders, body);
    }

    public HTTPRequest addBody(String body) {
        return new HTTPRequest(method, uri, version, headers, body);
    }

    public HTTPMethod method() {
        return method;
    }

    public HTTPRequestURI uri() {
        return uri;
    }

    public HTTPVersion version() {
        return version;
    }

    public Map<EntityHeaderFields, List<String>> headers() {
        return headers;
    }

    public String body() {
        return body;
    }
}

