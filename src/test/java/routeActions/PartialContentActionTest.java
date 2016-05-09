package routeActions;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import request.HTTPMethod;
import request.HTTPRequest;
import request.HTTPResource;
import request.HTTPVersion;
import response.EntityHeaderFields;
import response.HTTPResponse;
import router.RouterStub;
import testHelper.TestHelpers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static request.HTTPMethod.GET;
import static request.HTTPResource.PARTIAL_CONTENT;
import static request.HTTPVersion.HTTP_1_1;
import static response.EntityHeaderFields.*;

public class PartialContentActionTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    private File rootFolder;
    private TestHelpers testHelpers;
    private String testFolder;

    @Before
    public void setUp() {
        testFolder = "test";
        testHelpers = new TestHelpers();
        try {
            rootFolder = temporaryFolder.newFolder(testFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void partialContentWithStartAndEndRange() {
        testHelpers.createFileAtResource(rootFolder, "/partial_content.txt", payloadContent());
        URIProcessor uriProcessor = new URIProcessor(testHelpers.pathToRootFolder(temporaryFolder, testFolder));

        HTTPRequest getRequest = newGETRequest(GET, PARTIAL_CONTENT, HTTP_1_1, partialContentHeaders("bytes=0-4"));
        HTTPResponse response = new PartialContentAction().generateResponse(getRequest, new RouterStub(), uriProcessor);

        assertEquals("This ", new String(response.getBody()));
    }

    @Test
    public void partialContentWithReversedStartByteOnlyResponse() {
        testHelpers.createFileAtResource(rootFolder, "/partial_content.txt", payloadContent());
        URIProcessor uriProcessor = new URIProcessor(testHelpers.pathToRootFolder(temporaryFolder, testFolder));

        HTTPRequest getRequest = newGETRequest(GET, PARTIAL_CONTENT, HTTP_1_1, partialContentHeaders("bytes=-6"));
        HTTPResponse response = new PartialContentAction().generateResponse(getRequest, new RouterStub(), uriProcessor);

        assertEquals("a 206.", new String(response.getBody()));
    }

    @Test
    public void partialContentWithStartByteOnlyResponse() {
        testHelpers.createFileAtResource(rootFolder, "/partial_content.txt", payloadContent());
        URIProcessor uriProcessor = new URIProcessor(testHelpers.pathToRootFolder(temporaryFolder, testFolder));

        HTTPRequest getRequest = newGETRequest(GET, PARTIAL_CONTENT, HTTP_1_1, partialContentHeaders("bytes=4-"));
        HTTPResponse response = new PartialContentAction().generateResponse(getRequest, new RouterStub(), uriProcessor);

        assertEquals(" is a file that contains text to read part of in order to fulfill a 206.", new String(response.getBody()));
    }

    private String payloadContent() {
        return "This is a file that contains text to read part of in order to fulfill a 206.";
    }

    private Map<EntityHeaderFields, String> partialContentHeaders(String range) {
        Map<EntityHeaderFields, String> headers = new HashMap<>();
        headers.put(CONTENT_LENGTH, "76");
        headers.put(CONTENT_TYPE, "text/plain");
        headers.put(RANGE, range);
        return headers;
    }

    private HTTPRequest newGETRequest(HTTPMethod method, HTTPResource uri, HTTPVersion version, Map<EntityHeaderFields, String> headerFields) {
        return new HTTPRequest(method, uri, version, null, headerFields, null);
    }
}
