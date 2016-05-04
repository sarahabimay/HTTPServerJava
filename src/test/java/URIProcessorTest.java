import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import routeActions.URIProcessor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;

public class URIProcessorTest {
    private File rootFolder;
    private String parentFolder;
    private URIProcessor uriProcessor;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() {
        try {
            rootFolder = temporaryFolder.newFolder("test");
        } catch (IOException e) {
            e.printStackTrace();
        }
        parentFolder = pathToRootFolder();
        uriProcessor = new URIProcessor(parentFolder);
    }

    @Test
    public void readContentsAtURI() {
        String resourceToRead = "/read_me";
        createFileAtResource(resourceToRead, "data=fatcat");
        String contents = uriProcessor.read(resourceToRead);
        assertEquals("data=fatcat", contents);
    }

    @Test
    public void createContentsAtURI() {
        String newResource = "/form";
        uriProcessor.create(newResource, "data=fatcat");
        List<String> contents = contentsAtResource(parentFolder, newResource);
        assertEquals("data=fatcat", contents.get(0));
    }

    @Test
    public void updateContentsAtURI() {
        String updateMeResource = "/update_me";
        createFileAtResource(updateMeResource, "data=fatcat");
        uriProcessor.create(updateMeResource, "data=heathcliff");
        List<String> contents = contentsAtResource(parentFolder, updateMeResource);
        assertEquals("data=heathcliff", contents.get(0));
    }

    @Test
    public void deleteContentsAtURI() {
        String deleteResource = "/delete_me";
        createFileAtResource(deleteResource, "contents");
        uriProcessor.delete(deleteResource);
        List<String> contents = contentsAtResource(parentFolder, deleteResource);
        assertEquals(0, contents.size());
    }

    private void createFileAtResource(String resource, String contents) {
        String resourcePath = rootFolder
                .toPath()
                .resolve(removeLeadingBackslash(resource))
                .toString();
        try {
            Files.write(Paths.get(resourcePath), contents.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> contentsAtResource(String parentFolder, String resource) {
        try {
            Path resourcePath =
                    Paths.get(parentFolder)
                    .resolve(removeLeadingBackslash(resource));

            if (resourcePath.toFile().exists()) {
                return Files.readAllLines(resourcePath, UTF_8);
            }
            return new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private String pathToRootFolder() {
        return temporaryFolder
                .getRoot()
                .toPath()
                .resolve("test")
                .toString();
    }

    private String removeLeadingBackslash(String resource) {
        if (resource.charAt(0) == '/') {
            return resource.substring(1, resource.length());
        }
        return resource;
    }
}
