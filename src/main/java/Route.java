public class Route {
    private HTTPMethod method;
    private HTTPRequestURI path;
    private HTTPVersion version;

    public Route(HTTPMethod method, HTTPRequestURI path, HTTPVersion version) {
        this.method = method;
        this.path = path;
        this.version = version;
    }

    public HTTPResponse createStatusOKResponseNoBody(HTTPRequest request) {
        HTTPResponse response = new HTTPResponse();
        response.setStatusLine(request.version(), HTTPStatusCode.OK);
        response.setBody("");
        return response;
    }

    public boolean isMatch(HTTPRequest request) {
        boolean isURIMatch = request.uri().uri().equals(path.uri());
        boolean isMethodMatch = request.method().method().equals(method.method());
        boolean isVersionMatch = request.version().version().equals(version.version());

        return isURIMatch && isMethodMatch && isVersionMatch;
    }
}
