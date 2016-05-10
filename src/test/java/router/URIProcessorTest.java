package router;

import exceptions.ResourceManagementException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import routeActions.URIProcessor;
import testHelper.TestHelpers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class URIProcessorTest {
    private File rootFolder;
    private String parentFolder;
    private URIProcessor uriProcessor;
    private TestHelpers testHelpers;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

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
        String contents = new String(uriProcessor.read(resourceToRead));
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
        List<String> htmlLinks = uriProcessor.directoryContents();
        assertThat(htmlLinks, hasItem("file1"));
    }

    @Test
    public void expectedExceptionThrownOnRead() {
        expectedException.expect(ResourceManagementException.class);
        expectedException.expectMessage("Error Occurred Managing a Resource: \n" + "Read Resource Exception");

        String pathToRoot = temporaryFolder.getRoot().getAbsolutePath();
        URIProcessor resourceHandler = new URIProcessor(pathToRoot) {
            public byte[] read(String resource) {
                throw new ResourceManagementException("Read Resource Exception");
            }
        };
        resourceHandler.read("/anything");
    }

    @Test
    public void expectedExceptionThrownOnCreate() {
        expectedException.expect(ResourceManagementException.class);
        expectedException.expectMessage("Error Occurred Managing a Resource: \n" + "Create Resource Exception");

        String pathToRoot = temporaryFolder.getRoot().getAbsolutePath();
        URIProcessor resourceHandler = new URIProcessor(pathToRoot) {
            public void create(String resource, String newContent) {
                throw new ResourceManagementException("Create Resource Exception");
            }
        };
        resourceHandler.create("/anything", "new content");
    }

    @Test
    public void expectedExceptionThrownOnDelete() {
        expectedException.expect(ResourceManagementException.class);
        expectedException.expectMessage("Error Occurred Managing a Resource: \n" + "Delete Resource Exception");

        String pathToRoot = temporaryFolder.getRoot().getAbsolutePath();
        URIProcessor resourceHandler = new URIProcessor(pathToRoot) {
            public void delete(String resource) {
                throw new ResourceManagementException("Delete Resource Exception");
            }
        };
        resourceHandler.delete("/anything");
    }
}
