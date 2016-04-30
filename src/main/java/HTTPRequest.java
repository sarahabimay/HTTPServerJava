import java.util.*;

public class HTTPRequest {
    private final String METHOD_KEY = "method";
    private final String PATH_KEY = "path";
    private final String VERSION_KEY = "version";
    private final String FOUR_OH_FOUR_RESPONSE = "HTTP/1.1 404 Not Found";
    private Optional<Router> router = Optional.empty();
    private Map<String, String> request;

    public HTTPRequest(Router router, Optional<HttpClientSocket> clientSocket) {
        this.request = parse_request(clientSocket.get().request());
        this.router = Optional.of(router);
    }

    public Map<String, String> request() {
        return request;
    }

    public String response() {
        return router.isPresent() ? routeResponse() : FOUR_OH_FOUR_RESPONSE;
    }

    private String routeResponse() {
        return route().isPresent() ? route().get().response() : FOUR_OH_FOUR_RESPONSE;
    }

    private Optional<Route> route() {
        return request.size() > 0 ? router.get().findRoute(request) : Optional.empty();
    }

    private Map<String, String> parse_request(String request) {
        Map<String, String> requestDetails = new HashMap<>();
        List<String> requestValues = new ArrayList<>(Arrays.asList(request.split(" ")));
        if (requestValues.size() == 3) {
            List<String> requestKeys = new ArrayList<>(Arrays.asList(METHOD_KEY, PATH_KEY, VERSION_KEY));
            for (int i = 0; i < requestKeys.size(); i++) {
                requestDetails.put(requestKeys.get(i), requestValues.get(i));
            }
        }
        return requestDetails;
    }
}
