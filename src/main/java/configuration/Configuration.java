package configuration;

import request.HTTPResource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static request.HTTPMethod.*;

public class Configuration {
    private Map<HTTPResource, List<String>> methodsNotAllowed;

    public Configuration() {
        methodsNotAllowed = initializeMethodsNotAllowed();
    }

    private Map<HTTPResource, List<String>> initializeMethodsNotAllowed() {
        Map<HTTPResource, List<String>> methodsNotAllowed = new HashMap<>();
        methodsNotAllowed.put(HTTPResource.FILE1, Arrays.asList(PUT.method()));
        methodsNotAllowed.put(HTTPResource.TEXT_FILE, Arrays.asList(POST.method()));
        methodsNotAllowed.put(HTTPResource.OPTIONS_ONE, Arrays.asList(DELETE.method(), PATCH.method()));
        methodsNotAllowed.put(HTTPResource.OPTIONS_TWO, Arrays.asList(HEAD.method(), POST.method(), PUT.method()));
        return methodsNotAllowed;
    }

    public Map<HTTPResource, List<String>> methodsNotAllowed() {
       return methodsNotAllowed;
    }
}
