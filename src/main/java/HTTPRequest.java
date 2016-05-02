import java.util.List;

public class HTTPRequest {
    private HTTPMethod method;
    private String uri;
    private String version;
    private String body;

    public HTTPRequest() {
        this.method = HTTPMethod.UNDEFINED;
        this.uri = "";
        this.version = "";
        this.body = "";
    }

    public void addRequestLine(List<String> requestLine) {
        if (requestLine.size() == 3) {
            this.method = HTTPMethod.valueOf(requestLine.get(0));
            this.uri = requestLine.get(1);
            this.version = requestLine.get(2);
        }
    }

    public void addBody(String body) {
        this.body = body;
    }

    public HTTPMethod method() {
        return method;
    }

    public String uri() {
        return uri;
    }

    public String version() {
        return version;
    }

}
