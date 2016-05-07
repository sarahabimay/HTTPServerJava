package router;

import request.HTTPRequest;
import response.EntityHeaderFields;
import response.HTTPResponse;
import response.ResponseHTTPMessageFormatter;
import routeActions.RouteAction;
import routeActions.URIProcessor;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;
import static request.HTTPMethod.PATCH;
import static response.EntityHeaderFields.*;
import static response.HTTPStatusCode.NO_CONTENT;
import static response.HTTPStatusCode.PRECONDITION_FAILED;

public class PatchContentAction implements RouteAction {
    @Override
    public boolean isAppropriate(HTTPRequest request) {
        return request.method() == PATCH;
    }

    @Override
    public HTTPResponse generateResponse(HTTPRequest request, Router router, URIProcessor uriProcessor) {
        String currentResourcePayload = new String(uriProcessor.read(request.uri().uri()));
        if (!hasPatchEtagExpired(request, currentResourcePayload)) {
            uriProcessor.create(request.uri().uri(), request.body());
            return new HTTPResponse(new ResponseHTTPMessageFormatter())
                    .setStatusLine(request.version(), NO_CONTENT)
                    .setEntityHeaders(patchEntityHeaders(request, calculateEtag(request.body())))
                    .setBody(uriProcessor.read(request.uri().uri()));
        } else {
            return new HTTPResponse(new ResponseHTTPMessageFormatter())
                    .setStatusLine(request.version(), PRECONDITION_FAILED)
                    .setEntityHeaders(patchEntityHeaders(request, calculateEtag(currentResourcePayload)));
        }
    }

    private boolean hasPatchEtagExpired(HTTPRequest request, String currentResourcePayload) {
        return !calculateEtag(currentResourcePayload).equals(request.headers().get(IF_MATCH));
    }

    private Map<EntityHeaderFields, List<String>> patchEntityHeaders(HTTPRequest request, String currentEtag) {
        Map<EntityHeaderFields, List<String>> headers = new HashMap<>();
        headers.put(CONTENT_LOCATION, asList(request.uri().uri()));
        headers.put(ETAG, asList(currentEtag));
        return headers;
    }

    private String calculateEtag(String s) {
        ByteBuffer buf = UTF_8.encode(s);
        MessageDigest digest;
        try {
            digest = java.security.MessageDigest.getInstance("SHA1");
            digest.update(buf);
            buf.mark();
            buf.reset();
            return String.format("%s", javax.xml.bind.DatatypeConverter.printHexBinary(digest.digest())).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
