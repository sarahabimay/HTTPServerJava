package routeActions;

import request.HTTPRequest;
import request.HTTPResource;
import request.HTTPVersion;
import response.EntityHeaderFields;
import response.HTTPResponse;
import response.HTTPStatusCode;
import response.ResponseHTTPMessageFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static request.HTTPResource.INDEX;
import static request.HTTPResource.REDIRECT;

public class RedirectPathAction implements RouteAction {
    private final ArrayList<HTTPResource> resourcesToRedirect;

    public RedirectPathAction() {
        this.resourcesToRedirect = new ArrayList<>(asList(REDIRECT));
    }

    @Override
    public boolean isAppropriate(HTTPRequest request) {
        return resourcesToRedirect.contains(request.uri());
    }

    @Override
    public HTTPResponse generateResponse(HTTPRequest request) {
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
