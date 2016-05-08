package routeActions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class URIProcessor {
    private final String pathToResourceFolder;

    public URIProcessor(String pathToResourceFolder) {
        this.pathToResourceFolder = pathToResourceFolder;
    }

    public void create(String resource, String data) {
        Path root = Paths.get(pathToResourceFolder);
        Path resourcePath = root.resolve(removeLeadingBackslash(resource));
        try {
            Files.write(resourcePath, data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(String resource) {
        Path root = Paths.get(pathToResourceFolder);
        Path resourcePath = root.resolve(removeLeadingBackslash(resource));
        try {
            Files.delete(resourcePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] read(String resource) {
        Path root = Paths.get(pathToResourceFolder);
        Path resourcePath = root.resolve(removeLeadingBackslash(resource));
        try {
            return Files.readAllBytes(resourcePath);
        } catch (IOException e) {
            return new byte[0];
        }
    }

    private String removeLeadingBackslash(String resource) {
        if (resource.charAt(0) == '/') {
            return resource.substring(1, resource.length());
        }
        return resource;
    }

    public List<String> links() {
        File[] listOfFiles = new File(pathToResourceFolder).listFiles();
        List<String> anchors = new ArrayList<>();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                anchors.add(i, listOfFiles[i].getName());
            }
        }
        return anchors;
    }


}

