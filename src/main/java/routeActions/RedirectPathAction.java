package routeActions;

import request.HTTPRequest;
import request.HTTPVersion;
import response.EntityHeaderFields;
import response.HTTPResponse;
import response.HTTPStatusCode;
import response.ResponseHTTPMessageFormatter;
import router.Router;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static request.HTTPResource.INDEX;

public class RedirectPathAction implements RouteAction {
    @Override
    public boolean isAppropriate(HTTPRequest request) {
        return true;
    }

    @Override
    public HTTPResponse generateResponse(HTTPRequest request, Router router, URIProcessor uriProcessor) {
        return new HTTPResponse(new ResponseHTTPMessageFormatter())
                .setStatusLine(HTTPVersion.HTTP_1_1, HTTPStatusCode.FOUND)
                .setEntityHeaders(locationHeader());
    }

    private Map<EntityHeaderFields, List<String>> locationHeader() {
        Map<EntityHeaderFields, List<String>> headers = new HashMap<>();
        headers.put(EntityHeaderFields.LOCATION, asList(String.format("http://localhost:5000%s", INDEX.uri())));
        return headers;
    }
}
