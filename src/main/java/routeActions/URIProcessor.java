package routeActions;

import exceptions.ResourceManagementException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class URIProcessor {
    private final String pathToResourceFolder;
    private final Path root;

    public URIProcessor(String pathToResourceFolder) {
        this.pathToResourceFolder = pathToResourceFolder;
        this.root = Paths.get(pathToResourceFolder);
    }

    public void create(String resource, String newContent) {
        Path resourcePath = root.resolve(removeLeadingBackslash(resource));
        try {
            Files.write(resourcePath, newContent.getBytes());
        } catch (IOException e) {
            throw new ResourceManagementException(e.getMessage());
        }
    }

    public void delete(String resource) {
        Path resourcePath = root.resolve(removeLeadingBackslash(resource));
        try {
            Files.delete(resourcePath);
        } catch (IOException e) {
            throw new ResourceManagementException(e.getMessage());
        }
    }

    public byte[] read(String resource) {
        Path resourcePath = root.resolve(removeLeadingBackslash(resource));
        try {
            return Files.readAllBytes(resourcePath);
        } catch (IOException e) {
            throw new ResourceManagementException(e.getMessage());
        }
    }

    public List<String> directoryContents() {
        File[] listOfFiles = new File(pathToResourceFolder).listFiles();
        List<String> anchors = new ArrayList<>();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                anchors.add(i, listOfFiles[i].getName());
            }
        }
        return anchors;
    }

    public boolean exists(String resource) {
        Path resourcePath = root.resolve(removeLeadingBackslash(resource));
        try {
            return Files.exists(resourcePath);
        } catch(Exception e) {
            return false;
        }
    }

    private String removeLeadingBackslash(String resource) {
        if (!resource.isEmpty() && resource.charAt(0) == '/') {
            return resource.substring(1, resource.length());
        }
        return resource;
    }
}

