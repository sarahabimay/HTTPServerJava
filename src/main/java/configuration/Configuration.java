package configuration;

import request.HTTPResource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration {
    private Map<HTTPResource, List<String>> methodsNotAllowed;
    private int serverPortNumber;
    private String pathToPublicDirectory;
    private String authorisationCredentials;
    private List<HTTPResource> authorizedResources;

    public Configuration() {
        methodsNotAllowed = new HashMap<>();
        this.serverPortNumber = 0;
        this.pathToPublicDirectory = "";
        this.authorisationCredentials = "";
        this.authorizedResources = new ArrayList<>();
    }

    public Map<HTTPResource, List<String>> methodsNotAllowed() {
        return methodsNotAllowed;
    }

    public Configuration addServerPortNumber(int portNumber) {
        this.serverPortNumber = portNumber;
        return this;
    }

    public Configuration addPublicDirectory(String pathToPublicDir) {
        this.pathToPublicDirectory = pathToPublicDir;
        return this;
    }

    public Configuration addAuthorisationCredentials(String authorisationCredentials) {
        this.authorisationCredentials = authorisationCredentials;
        return this;
    }

    public Configuration addResourcesRequiringAuth(List<HTTPResource> resources) {
        this.authorizedResources = resources;
        return this;
    }

    public Configuration addMethodsNotAllowed(Map<HTTPResource, List<String>> methodsNotAllowed) {
        this.methodsNotAllowed = methodsNotAllowed;
        return this;
    }

    public String pathToPublicDirectory() {
        return pathToPublicDirectory;
    }

    public int serverPortNumber() {
        return serverPortNumber;
    }

    public String authorisationCredentials() {
        return authorisationCredentials;
    }

    public List<HTTPResource> authorizedResources() {
        return authorizedResources;
    }
}
