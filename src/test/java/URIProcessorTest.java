import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

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
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    private File rootFolder;
    private String parentFolder;
    private URIProcessor uriProcessor;

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
    public void createContentsAtURI() {
        uriProcessor.create("form", "data=fatcat");
        List<String> contents = contentsAtResource(parentFolder, "form");
        assertEquals("data=fatcat", contents.get(0));
    }

    @Test
    public void updateContentsAtURI() {
        String updateMeResource = "update_me";
        createFileAtResource(updateMeResource, "data=fatcat");
        uriProcessor.create(updateMeResource, "data=heathcliff");
        List<String> contents = contentsAtResource(parentFolder, updateMeResource);
        assertEquals("data=heathcliff", contents.get(0));
    }

    @Test
    public void deleteContentsAtURI() {
        createFileAtResource("delete_me", "contents");
        uriProcessor.delete("delete_me");
        List<String> contents = contentsAtResource(parentFolder, "delete_me");
        assertEquals(0, contents.size());
    }

    private void createFileAtResource(String delete_me, String contents) {
        String resourcePath = rootFolder
                .toPath()
                .resolve(delete_me)
                .toString();
        try {
            Files.write(Paths.get(resourcePath), contents.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> contentsAtResource(String parentFolder, String resource) {
        try {
            Path resourcePath = Paths.get(parentFolder)
                    .resolve(resource);

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
}
