package routeActions;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import request.HTTPMethod;
import request.HTTPRequest;
import request.HTTPResource;
import request.HTTPVersion;
import messages.EntityHeaderFields;
import response.HTTPResponse;
import testHelper.TestHelpers;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static request.HTTPMethod.GET;
import static request.HTTPResource.FORM;
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
        HTTPResponse response = new GETResourceAction(uriProcessor).generateResponse(getRequest);

        assertEquals(content, new String(response.getBody()));
    }

    private HTTPRequest newGETRequest(HTTPMethod method, HTTPResource uri, HTTPVersion version, Map<EntityHeaderFields, String> headerFields) {
        return new HTTPRequest(method, uri, version, null, headerFields, null);
    }
}
