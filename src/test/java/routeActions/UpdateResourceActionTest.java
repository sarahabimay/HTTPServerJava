package routeActions;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import request.HTTPMethod;
import request.HTTPRequest;
import request.HTTPResource;
import request.HTTPVersion;
import response.HTTPResponse;
import router.RouterStub;
import testHelper.TestHelpers;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static request.HTTPMethod.POST;
import static request.HTTPMethod.PUT;
import static request.HTTPResource.FORM;
import static request.HTTPVersion.HTTP_1_1;

public class UpdateResourceActionTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    private File rootFolder;
    private TestHelpers testHelpers;
    private String testFolder;

    @Before
    public void setUp() {
        testFolder = "test";
        try {
            rootFolder = temporaryFolder.newFolder(testFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        testHelpers = new TestHelpers();
    }

    @Test
    public void generateResponseWithResourceContents() {
        String payload = "data=fatcat";
        testHelpers.createFileAtResource(rootFolder, "/form", payload);
        URIProcessor uriProcessor = new URIProcessor(testHelpers.pathToRootFolder(temporaryFolder, testFolder));

        String updatedPayload = "data=heathcliff";
        HTTPRequest getRequest = newPostRequest(PUT, FORM, HTTP_1_1, updatedPayload);
        HTTPResponse response = new UpdateResourceAction().generateResponse(getRequest, new RouterStub(), uriProcessor);

        assertEquals("HTTP/1.1 200 OK", response.getStatusLine());
    }

    @Test
    public void updatePayloadAtResource() {
        String payload = "data=fatcat";
        testHelpers.createFileAtResource(rootFolder, "/form", payload);
        URIProcessor uriProcessor = new URIProcessor(testHelpers.pathToRootFolder(temporaryFolder, testFolder));

        String updatedPayload = "data=heathcliff";
        HTTPRequest getRequest = newPostRequest(PUT, FORM, HTTP_1_1, updatedPayload);
        new UpdateResourceAction().generateResponse(getRequest, new RouterStub(), uriProcessor);

        assertEquals(updatedPayload, testHelpers.contentsAtResource(testHelpers.pathToRootFolder(temporaryFolder, testFolder), FORM.uri()).get(0));
    }

    @Test
    public void createPayloadAtResource() {
        URIProcessor uriProcessor = new URIProcessor(testHelpers.pathToRootFolder(temporaryFolder, testFolder));

        String newPayload = "data=heathcliff";
        HTTPRequest getRequest = newPostRequest(POST, FORM, HTTP_1_1, newPayload);
        new UpdateResourceAction().generateResponse(getRequest, new RouterStub(), uriProcessor);

        assertEquals(newPayload,
                testHelpers.contentsAtResource(
                        testHelpers.pathToRootFolder(temporaryFolder, testFolder),
                        FORM.uri()).get(0));
    }

    private HTTPRequest newPostRequest(HTTPMethod method, HTTPResource uri, HTTPVersion version, String payload) {
        return new HTTPRequest(method, uri, version, null, null, payload);
    }
}

