public class Route {
    private String path;
    private String method;
    private String version;

    public Route(String method, String path, String version) {
        this.method = method;
        this.path = path;
        this.version = version;
    }

    public HTTPResponse createStatusOKResponseNoBody(HTTPRequest request) {
        HTTPResponse response = new HTTPResponse();
        response.setStatusLine(request.version(), 200, "OK");
        response.setBody("");
        return response;
    }

    public boolean isMatch(HTTPRequest request) {
        return request.uri().equals(path) && request.method().equals(method) && request.version().equals(this.version);
    }
}
