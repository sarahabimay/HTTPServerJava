import java.util.Map;

public class Route {
    private String path;
    private String method;

    public Route(String method, String path) {
        this.method = method;
        this.path = path;
    }

    public String response() {
        return "HTTP/1.1 200 OK";
    }

    public boolean isMatch(Map<String, String> request) {
        return request.get("path").equals(path) && request.get("method").equals(method);
    }
}
