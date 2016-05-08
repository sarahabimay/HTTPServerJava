package routeActions;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import request.HTTPRequest;
import response.EntityHeaderFields;
import response.HTTPResponse;
import router.RouterFake;
import testHelper.TestHelpers;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static request.HTTPMethod.PATCH;
import static request.HTTPResource.PATCH_CONTENT;
import static request.HTTPVersion.HTTP_1_1;
import static response.EntityHeaderFields.CONTENT_LOCATION;
import static response.EntityHeaderFields.ETAG;

public class PatchContentActionTest {
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
    public void patchIfMatchEtagIsTrue() {
        String resourceContents = "default content";
        testHelpers.createFileAtResource(rootFolder, "/patch-content.txt", resourceContents);
        URIProcessor uriProcessor = new URIProcessor(testHelpers.pathToRootFolder(temporaryFolder, testFolder));

        String patchedContent = "patched content";
        HTTPResponse response = new PatchContentAction().generateResponse(createPatchRequest(patchedContent), new RouterFake(), uriProcessor);

        assertEquals("HTTP/1.1 204 No Content", response.getStatusLine());
        assertThat(response.getEntityHeaders().get(ETAG), hasItem(calculateEtag(patchedContent)));
        assertThat(response.getEntityHeaders().get(CONTENT_LOCATION), hasItem("/patch-content.txt"));
        assertEquals(patchedContent, new String(response.getBody()));
    }

    @Test
    public void doNotPatchIfMatchEtagIsFalse() {
        String resourceContents = "content changed and etag different";
        testHelpers.createFileAtResource(rootFolder, "/patch-content.txt", resourceContents);
        URIProcessor uriProcessor = new URIProcessor(testHelpers.pathToRootFolder(temporaryFolder, testFolder));

        String patchedContent = "patched content";
        HTTPResponse response = new PatchContentAction().generateResponse(createPatchRequest(patchedContent), new RouterFake(), uriProcessor);

        assertEquals("HTTP/1.1 412 Precondition Failed", response.getStatusLine());
        assertThat(response.getEntityHeaders().get(ETAG), hasItem(getResourceEtagValue(resourceContents)));
        assertThat(response.getEntityHeaders().get(CONTENT_LOCATION), hasItem("/patch-content.txt"));
    }

    private String getResourceEtagValue(String resourceContents) {
        return calculateEtag(resourceContents);
    }

    private HTTPRequest createPatchRequest(String patchedContent) {
        return new HTTPRequest()
                .addRequestLine(patchRequestLine())
                .addRequestHeader(patchHeaders())
                .addBody(patchedContent);

    }

    private Map<EntityHeaderFields, String> patchHeaders() {
        Map<EntityHeaderFields, String> headers = new HashMap<>();
        headers.put(EntityHeaderFields.CONTENT_LENGTH, "15");
        headers.put(EntityHeaderFields.IF_MATCH, "dc50a0d27dda2eee9f65644cd7e4c9cf11de8bec");
        return headers;
    }

    private Map<String, String> patchRequestLine() {
        Map<String, String> requestLine = new HashMap<>();
        requestLine.put("method", PATCH.method());
        requestLine.put("uri", PATCH_CONTENT.uri());
        requestLine.put("version", HTTP_1_1.version());
        return requestLine;
    }

    private String calculateEtag(String s) {
        ByteBuffer buf = UTF_8.encode(s);
        MessageDigest digest;
        try {
            digest = java.security.MessageDigest.getInstance("SHA1");
            digest.update(buf);
            buf.mark();
            buf.reset();
            return String.format("%s", javax.xml.bind.DatatypeConverter.printHexBinary(digest.digest())).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
