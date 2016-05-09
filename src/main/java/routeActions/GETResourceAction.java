package routeActions;

import request.HTTPRequest;
import response.EntityHeaderFields;
import response.HTTPResponse;
import response.ResponseHTTPMessageFormatter;
import router.Router;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static request.HTTPMethod.GET;
import static response.EntityHeaderFields.*;
import static response.HTTPStatusCode.OK;

public class GETResourceAction implements RouteAction {
    @Override
    public boolean isAppropriate(HTTPRequest request) {
        return request.method() == GET;
    }

    @Override
    public HTTPResponse generateResponse(HTTPRequest request, Router router, URIProcessor uriProcessor) {
        return createGetPayloadResponse(request, uriProcessor);
    }

    private HTTPResponse createGetPayloadResponse(HTTPRequest request, URIProcessor uriProcessor) {
        byte[] payload = uriProcessor.read(request.uri().uri());
        return new HTTPResponse(new ResponseHTTPMessageFormatter())
                .setStatusLine(request.version(), OK)
                .setEntityHeaders(contentHeaders(payload))
                .setBody(payload);
    }

    private Map<EntityHeaderFields, List<String>> contentHeaders(byte[] payload) {
        Map<EntityHeaderFields, List<String>> headers = new HashMap<>();
        headers.put(CONTENT_LENGTH, Arrays.asList(Integer.toString(payload.length)));
        headers.put(CONTENT_TYPE, Arrays.asList("text/plain"));
        return headers;
    }
}
