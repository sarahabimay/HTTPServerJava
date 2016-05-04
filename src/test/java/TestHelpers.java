import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TestHelpers {
    public void createFileAtResource(File rootFolder, String resource, String contents) {
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

    public List<String> contentsAtResource(String parentFolder, String resource) {
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

    public String pathToRootFolder(TemporaryFolder temporaryFolder, String folder) {
        return temporaryFolder
                .getRoot()
                .toPath()
                .resolve(folder)
                .toString();
    }

    private String removeLeadingBackslash(String resource) {
        if (resource.charAt(0) == '/') {
            return resource.substring(1, resource.length());
        }
        return resource;
    }
}
