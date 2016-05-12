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
import testHelper.TestHelpers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static request.HTTPMethod.GET;
import static request.HTTPResource.*;
import static request.HTTPResource.PARTIAL_CONTENT;
import static request.HTTPVersion.HTTP_1_1;
import static response.EntityHeaderFields.*;

public class PartialContentActionTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    private File rootFolder;
    private TestHelpers testHelpers;
    private String testFolder;
    private URIProcessor uriProcessor;

    @Before
    public void setUp() {
        testFolder = "test";
        testHelpers = new TestHelpers();
        try {
            rootFolder = temporaryFolder.newFolder(testFolder);
            testHelpers.createFileAtResource(rootFolder, "/partial_content.txt", payloadContent());
            uriProcessor = new URIProcessor(testHelpers.pathToRootFolder(temporaryFolder, testFolder));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void requestHasQueryParametersToDecode() {
        PartialContentAction action = new PartialContentAction(null);
        HTTPRequest request = new HTTPRequest(GET, FORM, HTTP_1_1, null, partialContentHeaders("bytes=0-4") , null );
        assertEquals(true, action.isAppropriate(request));
    }

    @Test
    public void requestHasNoQueryParametersToDecode() {
        PartialContentAction action = new PartialContentAction(null);
        HTTPRequest request = new HTTPRequest(GET, FORM, HTTP_1_1, null, null, null );
        assertEquals(false, action.isAppropriate(request));
    }

    @Test
    public void startAndEndRangeProvided() {
        HTTPRequest getRequest = newGETRequest(GET, PARTIAL_CONTENT, HTTP_1_1, partialContentHeaders("bytes=0-4"));
        HTTPResponse response = new PartialContentAction(uriProcessor).generateResponse(getRequest);

        assertEquals("This ", new String(response.getBody()));
    }

    @Test
    public void partialContentResponseHasCorrectHeaders() {
        HTTPRequest getRequest = newGETRequest(GET, PARTIAL_CONTENT, HTTP_1_1, partialContentHeaders("bytes=0-4"));
        HTTPResponse response = new PartialContentAction(uriProcessor).generateResponse(getRequest);

        assertEquals(asList("text/plain"), response.getEntityHeaders().get(CONTENT_TYPE));
        assertEquals(asList("5"), response.getEntityHeaders().get(CONTENT_LENGTH));
        assertEquals(asList("bytes 0-4"), response.getEntityHeaders().get(CONTENT_RANGE));
    }

    @Test
    public void endRangeValueOutOfBounds() {
        HTTPRequest getRequest = newGETRequest(GET, PARTIAL_CONTENT, HTTP_1_1, partialContentHeaders("bytes=0-77"));
        HTTPResponse response = new PartialContentAction(uriProcessor).generateResponse(getRequest);

        assertEquals(payloadContent(), new String(response.getBody()));
    }

    @Test
    public void onlyReversedStartByteProvided() {
        HTTPRequest getRequest = newGETRequest(GET, PARTIAL_CONTENT, HTTP_1_1, partialContentHeaders("bytes=-6"));
        HTTPResponse response = new PartialContentAction(uriProcessor).generateResponse(getRequest);

        assertEquals("a 206.", new String(response.getBody()));
    }

    @Test
    public void onlyStartByteProvided() {
        HTTPRequest getRequest = newGETRequest(GET, PARTIAL_CONTENT, HTTP_1_1, partialContentHeaders("bytes=4-"));
        HTTPResponse response = new PartialContentAction(uriProcessor).generateResponse(getRequest);

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
