package routeActions;

import request.HTTPRequest;
import messages.EntityHeaderFields;
import response.HTTPResponse;
import response.ResponseHTTPMessageFormatter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static functions.FunctionHelpers.calculateEtag;
import static java.util.Arrays.asList;
import static request.HTTPMethod.PATCH;
import static messages.EntityHeaderFields.*;
import static response.HTTPStatusCode.NO_CONTENT;
import static response.HTTPStatusCode.PRECONDITION_FAILED;

public class PatchContentAction implements RouteAction {
    private final URIProcessor uriProcessor;

    public PatchContentAction(URIProcessor uriProcessor) {
        this.uriProcessor = uriProcessor;
    }

    @Override
    public boolean isAppropriate(HTTPRequest request) {
        return request.method() == PATCH;
    }

    @Override
    public HTTPResponse generateResponse(HTTPRequest request) {
        if (!hasPatchEtagExpired(request, payloadAtResource(request, uriProcessor))) {
            updateResourcePayload(request, uriProcessor);
            return patchSuccessResponse(request, uriProcessor);
        } else {
            return preconditionFailedResponse(request, uriProcessor);
        }
    }

    private String payloadAtResource(HTTPRequest request, URIProcessor uriProcessor) {
        return new String(uriProcessor.read(request.uri().uri()));
    }

    private void updateResourcePayload(HTTPRequest request, URIProcessor uriProcessor) {
        uriProcessor.create(request.uri().uri(), request.body());
    }

    private boolean hasPatchEtagExpired(HTTPRequest request, String currentResourcePayload) {
        return !calculateEtag.apply(currentResourcePayload).equals(request.headers().get(IF_MATCH));
    }

    private HTTPResponse patchSuccessResponse(HTTPRequest request, URIProcessor uriProcessor) {
        return new HTTPResponse(new ResponseHTTPMessageFormatter())
                .setStatusLine(request.version(), NO_CONTENT)
                .setEntityHeaders(patchEntityHeaders(request, calculateEtag.apply(request.body())))
                .setBody(uriProcessor.read(request.uri().uri()));
    }

    private HTTPResponse preconditionFailedResponse(HTTPRequest request, URIProcessor uriProcessor) {
        return new HTTPResponse(new ResponseHTTPMessageFormatter())
                .setStatusLine(request.version(), PRECONDITION_FAILED)
                .setEntityHeaders(patchEntityHeaders(request, calculateEtag.apply(payloadAtResource(request, uriProcessor))));
    }

    private Map<EntityHeaderFields, List<String>> patchEntityHeaders(HTTPRequest request, String currentEtag) {
        Map<EntityHeaderFields, List<String>> headers = new HashMap<>();
        headers.put(CONTENT_LOCATION, asList(request.uri().uri()));
        headers.put(ETAG, asList(currentEtag));
        return headers;
    }
}
