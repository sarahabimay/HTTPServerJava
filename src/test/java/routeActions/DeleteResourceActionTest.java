package routeActions;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import request.HTTPMethod;
import request.HTTPRequest;
import request.HTTPRequestURI;
import request.HTTPVersion;
import testHelper.TestHelpers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static request.HTTPMethod.DELETE;
import static request.HTTPRequestURI.FORM;
import static request.HTTPVersion.HTTP_1_1;

public class DeleteResourceActionTest {
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
    public void resourceHasBeenRemoved() {

    }
    @Test
    public void generateResponseWithResourceContents() {
        String payload = "data=fatcat";
        testHelpers.createFileAtResource(rootFolder, "/form", payload);
        URIProcessor uriProcessor = new URIProcessor(testHelpers.pathToRootFolder(temporaryFolder, testFolder));

        HTTPRequest getRequest = newDeleteRequest(DELETE, FORM, HTTP_1_1);
        new DeleteResourceAction().generateResponse(getRequest, uriProcessor);

        Assert.assertEquals(new ArrayList<>(), testHelpers.contentsAtResource(
                testHelpers.pathToRootFolder(temporaryFolder, testFolder), FORM.uri()));
    }

    private HTTPRequest newDeleteRequest(HTTPMethod method, HTTPRequestURI uri, HTTPVersion version) {
        return new HTTPRequest(method, uri, version, null, null);
    }
}
