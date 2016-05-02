public class Route {
    private String path;
    private HTTPMethod method;
    private String version;

    public Route(HTTPMethod method, String path, String version) {
        this.method = method;
        this.path = path;
        this.version = version;
    }

    public HTTPResponse createStatusOKResponseNoBody(HTTPRequest request) {
        HTTPResponse response = new HTTPResponse();
        response.setStatusLine(request.version(), HTTPStatusCode.OK.statusCode(), HTTPStatusCode.OK.reason());
        response.setBody("");
        return response;
    }

    public boolean isMatch(HTTPRequest request) {
        return request.uri().equals(path) &&
                request.method().equals(method) &&
                request.version().equals(this.version);
    }
}
