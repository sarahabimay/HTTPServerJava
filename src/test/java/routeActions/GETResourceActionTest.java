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
import java.util.*;

import static org.junit.Assert.assertEquals;
import static request.HTTPMethod.GET;
import static request.HTTPResource.FORM;
import static request.HTTPResource.PARTIAL_CONTENT;
import static request.HTTPVersion.HTTP_1_1;

public class GETResourceActionTest {
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
    public void generateResponseWithResourceContents() {
        String content = "data=fatcat";
        testHelpers.createFileAtResource(rootFolder, "/form", content);
        URIProcessor uriProcessor = new URIProcessor(testHelpers.pathToRootFolder(temporaryFolder, testFolder));

        HTTPRequest getRequest = newGETRequest(GET, FORM, HTTP_1_1, null);
        HTTPResponse response = new GETResourceAction().generateResponse(getRequest, new RouterStub(), uriProcessor);

        assertEquals(content, response.getBody());
    }

    @Test
    public void partialContentResponse() {
        testHelpers.createFileAtResource(rootFolder, "/partial_content.txt", payloadContent());
        URIProcessor uriProcessor = new URIProcessor(testHelpers.pathToRootFolder(temporaryFolder, testFolder));

        HTTPRequest getRequest = newGETRequest(GET, PARTIAL_CONTENT, HTTP_1_1, partialContentRangeHeader("bytes=0-4"));
        HTTPResponse response = new GETResourceAction().generateResponse(getRequest, new RouterStub(), uriProcessor);

        assertEquals("This ", response.getBody());
    }

    @Test
    public void partialContentEndRangeResponse() {
        testHelpers.createFileAtResource(rootFolder, "/partial_content.txt", payloadContent());
        URIProcessor uriProcessor = new URIProcessor(testHelpers.pathToRootFolder(temporaryFolder, testFolder));

        HTTPRequest getRequest = newGETRequest(GET, PARTIAL_CONTENT, HTTP_1_1, partialContentRangeHeader("bytes=-6"));
        HTTPResponse response = new GETResourceAction().generateResponse(getRequest, new RouterStub(), uriProcessor);

        assertEquals("a 206.", response.getBody());
    }

    @Test
    public void partialContentStartRangeGivenResponse() {
        testHelpers.createFileAtResource(rootFolder, "/partial_content.txt", payloadContent());
        URIProcessor uriProcessor = new URIProcessor(testHelpers.pathToRootFolder(temporaryFolder, testFolder));

        HTTPRequest getRequest = newGETRequest(GET, PARTIAL_CONTENT, HTTP_1_1, partialContentRangeHeader("bytes=4-"));
        HTTPResponse response = new GETResourceAction().generateResponse(getRequest, new RouterStub(), uriProcessor);

        assertEquals(" is a file that contains text to read part of in order to fulfill a 206.", response.getBody());
    }

    private String payloadContent() {
        return "This is a file that contains text to read part of in order to fulfill a 206.";
    }

    private Map<EntityHeaderFields, List<String>> partialContentRangeHeader(String range) {
        Map<EntityHeaderFields, List<String>> headers = new HashMap<>();
        headers.put(EntityHeaderFields.RANGE, new ArrayList<>(Arrays.asList(range)));
        return headers;
    }

    private HTTPRequest newGETRequest(HTTPMethod method, HTTPResource uri, HTTPVersion version, Map<EntityHeaderFields, List<String>> headerFields) {
        return new HTTPRequest(method, uri, version, null, headerFields, null);
    }
}
