package request;

import response.EntityHeaderFields;

import java.util.List;
import java.util.Map;

import static request.HTTPMethod.lookupMethod;
import static request.HTTPResource.UNRECOGNIZED;
import static request.HTTPResource.lookupURIName;
import static request.HTTPVersion.lookupVersionName;

public class HTTPRequest {
    private HTTPMethod method;
    private HTTPResource uri;
    private HTTPVersion version;
    private Map<EntityHeaderFields, List<String>> headers;
    private String body;
    private String queryParameters;

    public HTTPRequest() {
        this.method = HTTPMethod.UNDEFINED;
        this.uri = UNRECOGNIZED;
        this.version = HTTPVersion.UNDEFINED;
        this.body = "";
        this.queryParameters = "";
    }

    public HTTPRequest(HTTPMethod method, HTTPResource uri, HTTPVersion version, String queryParameters, Map<EntityHeaderFields, List<String>> headers, String body) {
        this.method = method;
        this.uri = uri;
        this.version = version;
        this.queryParameters = queryParameters;
        this.headers = headers;
        this.body = body;
    }

    public HTTPRequest addRequestLine(Map<String, String> requestLine) {
        this.method = lookupMethod(requestLine.get("method"));
        this.uri = lookupURIName(requestLine.get("uri"));
        this.version = lookupVersionName(requestLine.get("version"));
        this.queryParameters = requestLine.get("queryParameters");
        return new HTTPRequest(method, uri, version, queryParameters, headers, body);
    }

    public HTTPRequest addRequestHeader(Map<EntityHeaderFields, List<String>> requestHeaders) {
        return new HTTPRequest(method, uri, version, queryParameters, requestHeaders, body);
    }

    public HTTPRequest addBody(String body) {
        return new HTTPRequest(method, uri, version, queryParameters, headers, body);
    }

    public HTTPMethod method() {
        return method;
    }

    public HTTPResource uri() {
        return uri;
    }

    public String queryParameters() {
        return queryParameters;
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

