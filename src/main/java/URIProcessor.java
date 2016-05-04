import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class URIProcessor {
    private final String pathToResourceFolder;

    public URIProcessor(String pathToResourceFolder) {
        this.pathToResourceFolder = pathToResourceFolder;
    }

    public void create(String resource, String data) {
        Path root = Paths.get(pathToResourceFolder);
        Path resourcePath = root.resolve(resource);
        try {
            Files.write(resourcePath, data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(String resource) {
        Path root = Paths.get(pathToResourceFolder);
        Path resourcePath = root.resolve(resource);
        try {
            Files.delete(resourcePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String read(String resource) {
        Path root = Paths.get(pathToResourceFolder);
        Path resourcePath = root.resolve(resource);
        try {
            return new String(Files.readAllBytes(resourcePath));
        } catch (IOException e) {
            return "";
        }
    }
}

