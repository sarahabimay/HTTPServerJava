package router;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import routeActions.URIProcessor;
import testHelper.TestHelpers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class URIProcessorTest {
    private File rootFolder;
    private String parentFolder;
    private URIProcessor uriProcessor;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    private TestHelpers testHelpers;

    @Before
    public void setUp() {
        String testFolder = "test";
        try {
            rootFolder = temporaryFolder.newFolder(testFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        testHelpers = new TestHelpers();
        parentFolder = testHelpers.pathToRootFolder(temporaryFolder, testFolder);
        uriProcessor = new URIProcessor(parentFolder);
    }

    @Test
    public void readContentsAtURI() {
        String resourceToRead = "/read_me";
        testHelpers.createFileAtResource(rootFolder, resourceToRead, "data=fatcat");
        String contents = uriProcessor.read(resourceToRead);
        assertEquals("data=fatcat", contents);
    }

    @Test
    public void createContentsAtURI() {
        String newResource = "/form";
        uriProcessor.create(newResource, "data=fatcat");
        List<String> contents = testHelpers.contentsAtResource(parentFolder, newResource);
        assertEquals("data=fatcat", contents.get(0));
    }

    @Test
    public void updateContentsAtURI() {
        String updateMeResource = "/update_me";
        testHelpers.createFileAtResource(rootFolder, updateMeResource, "data=fatcat");
        uriProcessor.create(updateMeResource, "data=heathcliff");
        List<String> contents = testHelpers.contentsAtResource(parentFolder, updateMeResource);
        assertEquals("data=heathcliff", contents.get(0));
    }

    @Test
    public void deleteContentsAtURI() {
        String deleteResource = "/delete_me";
        testHelpers.createFileAtResource(rootFolder, deleteResource, "contents");
        uriProcessor.delete(deleteResource);
        List<String> contents = testHelpers.contentsAtResource(parentFolder, deleteResource);
        assertEquals(0, contents.size());
    }

    @Test
    public void getDirectoryLinks() {
        String pathToPublicDirectory = "/Users/sarahjohnston/Sarah/CobSpec/public/";
        URIProcessor uriProcessor = new URIProcessor(pathToPublicDirectory);
        String htmlLinks = uriProcessor.links();
        String expectedPayload = directoryLinksHtml();
        assertThat(htmlLinks, containsString(expectedPayload));
    }

    private String directoryLinksHtml() {
        return new StringBuilder()
                .append("<a href='/file1'>file1</a>")
                .append("<br>")
                .append("<a href='/file2'>file2</a>")
                .append("<br>")
                .append("<a href='/image.gif'>image.gif</a>")
                .append("<br>")
                .append("<a href='/image.jpeg'>image.jpeg</a>")
                .append("<br>")
                .append("<a href='/image.png'>image.png</a>")
                .append("<br>")
                .append("<a href='/method_options'>method_options</a>")
                .append("<br>")
                .append("<a href='/partial_content.txt'>partial_content.txt</a>")
                .append("<br>")
                .append("<a href='/patch-content.txt'>patch-content.txt</a>")
                .append("<br>")
                .append("<a href='/text-file.txt'>text-file.txt</a>")
                .append("<br>")
                .toString();
    }

}
