package routeActions;

import org.junit.Test;
import request.HTTPMethod;
import request.HTTPRequest;
import request.HTTPResource;
import response.HTTPResponse;
import router.RouterStub;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;
import static request.HTTPMethod.GET;
import static request.HTTPResource.INDEX;
import static request.HTTPVersion.HTTP_1_1;

public class DirectoryContentsActionTest {
    @Test
    public void generateResponseWithDirectoryContents() {
        DirectoryContentsAction action = new DirectoryContentsAction();
        HTTPResponse response = action.generateResponse(newRequest(GET, INDEX), new RouterStub(), createURIProcessor());
        String expectedLink = "<a href='/file1'>file1</a>";
        assertThat(response.getBody(), containsString(expectedLink));
    }

    private URIProcessor createURIProcessor() {
        String pathToPublicDirectory = "/Users/sarahjohnston/Sarah/CobSpec/public/";
        return new URIProcessor(pathToPublicDirectory);
    }

    private HTTPRequest newRequest(HTTPMethod method, HTTPResource uri) {
        return new HTTPRequest(method, uri, HTTP_1_1, null, null, null);
    }
}
