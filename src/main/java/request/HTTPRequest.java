package request;

import java.util.List;

import static request.HTTPRequestURI.UNRECOGNIZED;
import static request.HTTPRequestURI.lookupURIName;
import static request.HTTPVersion.lookupVersionName;

public class HTTPRequest {
    private HTTPMethod method;
    private HTTPRequestURI uri;
    private HTTPVersion version;
    private String body;

    public HTTPRequest() {
        this.method = HTTPMethod.UNDEFINED;
        this.uri = UNRECOGNIZED;
        this.version = HTTPVersion.UNDEFINED;
        this.body = "";
    }

    public HTTPRequest(HTTPMethod method, HTTPRequestURI uri, HTTPVersion version, String body) {
        this.method = method;
        this.uri = uri;
        this.version = version;
        this.body = body;
    }

    public HTTPRequest addRequestLine(List<String> requestLine) {
        if (requestLine.size() == 3) {
            this.method = HTTPMethod.valueOf(requestLine.get(0));
            this.uri = lookupURIName(requestLine.get(1));
            this.version = lookupVersionName(requestLine.get(2));
        }
        return new HTTPRequest(method, uri, version, body);
    }

    public void addBody(String body) {
        this.body = body;
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

    public String body(){
        return body;
    }
}

