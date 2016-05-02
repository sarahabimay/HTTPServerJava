import java.util.List;

public class HTTPRequest {
    private HTTPMethod method;
    private HTTPRequestURI uri;
    private HTTPVersion version;
    private String body;

    public HTTPRequest() {
        this.method = HTTPMethod.UNDEFINED;
        this.uri = HTTPRequestURI.UNRECOGNIZED;
        this.version = HTTPVersion.UNDEFINED;
        this.body = "";
    }

    public void addRequestLine(List<String> requestLine) {
        if (requestLine.size() == 3) {
            this.method = HTTPMethod.valueOf(requestLine.get(0));
            this.uri = HTTPRequestURI.lookupURIName(requestLine.get(1));
            this.version = HTTPVersion.lookupVersionName(requestLine.get(2));
        }
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

}
