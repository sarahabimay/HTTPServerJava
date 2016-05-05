package routeActions;

import org.junit.Test;
import request.HTTPMethod;
import request.HTTPRequest;
import request.HTTPRequestURI;
import request.HTTPVersion;
import response.HTTPResponse;
import router.URIProcessorStub;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static request.HTTPMethod.GET;
import static request.HTTPRequestURI.FOOBAR;
import static request.HTTPVersion.HTTP_1_1;
import static response.HTTPStatusCode.NOT_FOUND;

public class StatusNOKActionTest {
    @Test
    public void createStatusNOKResponse() {
        StatusNOKAction action = new StatusNOKAction();
        HTTPResponse response = action.generateResponse(unavailableResourceRequest(), new URIProcessorStub());
        assertEquals(statusNOKResponseLine(), response.getStatusLine());
    }

    private HTTPRequest unavailableResourceRequest() {
        return new HTTPRequest().addRequestLine(createRequestLine(GET, FOOBAR, "", HTTP_1_1));
    }

    private Map<String, String> createRequestLine(HTTPMethod method, HTTPRequestURI uri, String queryParams, HTTPVersion version) {
        Map<String, String> requestLine = new HashMap<>();
        requestLine.put("method", method.method());
        requestLine.put("uri", uri.uri());
        requestLine.put("version", version.version());
        requestLine.put("queryParameters", queryParams);
        return requestLine;
    }

    private String statusNOKResponseLine() {
        return new StringBuilder()
                .append(HTTP_1_1.version())
                .append(" ")
                .append(NOT_FOUND.statusCode())
                .append(" ")
                .append(NOT_FOUND.reason())
                .toString();
    }
}
