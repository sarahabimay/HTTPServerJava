package routeActions;

import request.HTTPRequest;
import messages.EntityHeaderFields;
import response.HTTPResponse;
import response.ResponseHTTPMessageFormatter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static request.HTTPMethod.GET;
import static messages.EntityHeaderFields.*;
import static response.HTTPStatusCode.OK;

public class GETResourceAction implements RouteAction {
    private final URIProcessor uriProcessor;

    public GETResourceAction(URIProcessor uriProcessor) {
        this.uriProcessor = uriProcessor;
    }

    @Override
    public boolean isAppropriate(HTTPRequest request) {
        return request.method() == GET && uriProcessor.exists(request.uri().uri());
    }

    @Override
    public HTTPResponse generateResponse(HTTPRequest request) {
        return createGetPayloadResponse(request );
    }

    private HTTPResponse createGetPayloadResponse(HTTPRequest request) {
        byte[] payload = uriProcessor.read(request.uri().uri());
        return new HTTPResponse(new ResponseHTTPMessageFormatter())
                .setStatusLine(request.version(), OK)
                .setEntityHeaders(contentHeaders(payload))
                .setBody(payload);
    }

    private Map<EntityHeaderFields, List<String>> contentHeaders(byte[] payload) {
        Map<EntityHeaderFields, List<String>> headers = new HashMap<>();
        headers.put(CONTENT_LENGTH, asList(Integer.toString(payload.length)));
        headers.put(CONTENT_TYPE, asList(CONTENT_TYPE_PLAIN.field()));
        return headers;
    }
}
