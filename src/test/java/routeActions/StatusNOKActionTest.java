package routeActions;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import request.HTTPMethod;
import request.HTTPRequest;
import request.HTTPResource;
import request.HTTPVersion;
import response.HTTPResponse;
import testHelper.TestHelpers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static request.HTTPMethod.GET;
import static request.HTTPResource.FOOBAR;
import static request.HTTPVersion.HTTP_1_1;
import static response.HTTPStatusCode.NOT_FOUND;

public class StatusNOKActionTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    private File rootFolder;
    private TestHelpers testHelpers;

    @Test
    public void createStatusNOKResponse() {
        testHelpers = new TestHelpers();
        try {
            rootFolder = temporaryFolder.newFolder("test");
        } catch (IOException e) {
            e.printStackTrace();
        }
        URIProcessor uriProcessor = new URIProcessor(rootFolder.getAbsolutePath());
        StatusNOKAction action = new StatusNOKAction(uriProcessor);
        HTTPResponse response = action.generateResponse(unavailableResourceRequest());
        assertEquals(statusNOKResponseLine(), response.getStatusLine());
    }

    private HTTPRequest unavailableResourceRequest() {
        return new HTTPRequest().addRequestLine(createRequestLine(GET, FOOBAR, "", HTTP_1_1));
    }

    private Map<String, String> createRequestLine(HTTPMethod method, HTTPResource uri, String queryParams, HTTPVersion version) {
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
