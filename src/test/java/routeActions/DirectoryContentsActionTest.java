package routeActions;

import org.junit.Test;
import request.HTTPMethod;
import request.HTTPRequest;
import request.HTTPRequestURI;
import response.HTTPResponse;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;
import static request.HTTPMethod.GET;
import static request.HTTPRequestURI.INDEX;
import static request.HTTPVersion.HTTP_1_1;

public class DirectoryContentsActionTest {
    @Test
    public void generateResponseWithDirectoryContents() {
        DirectoryContentsAction action = new DirectoryContentsAction();
        HTTPResponse response = action.generateResponse(newRequest(GET, INDEX), createURIProcessor());
        String expectedLink = "<a href='/file1'>file1</a>";
        assertThat(response.getBody(), containsString(expectedLink));
    }

    private URIProcessor createURIProcessor() {
        String pathToPublicDirectory = "/Users/sarahjohnston/Sarah/CobSpec/public/";
        return new URIProcessor(pathToPublicDirectory);
    }

    private HTTPRequest newRequest(HTTPMethod method, HTTPRequestURI uri) {
        return new HTTPRequest(method, uri, HTTP_1_1, null, null, null);
    }
}
