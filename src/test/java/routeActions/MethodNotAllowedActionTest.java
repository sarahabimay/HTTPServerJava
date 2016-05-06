package routeActions;

import org.junit.Before;
import org.junit.Test;
import request.HTTPMethod;
import request.HTTPRequest;
import request.HTTPResource;
import request.HTTPVersion;
import response.EntityHeaderFields;
import response.HTTPResponse;
import router.RouterFake;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static request.HTTPMethod.GET;
import static request.HTTPMethod.PUT;
import static response.EntityHeaderFields.ALLOW;
import static response.HTTPStatusCode.METHOD_NOT_ALLOWED;

public class MethodNotAllowedActionTest {

    private List<String> allowedMethods;

    @Before
    public void setUp() {
        allowedMethods = Arrays.asList(GET.method(), PUT.method());
    }

    @Test
    public void generateMethodNotFoundResponse() {
        RouterFake routerFake = new RouterFake();
        routerFake.setFakeResourceMethods(new ArrayList<>(allowedMethods));
        MethodNotAllowedAction action = new MethodNotAllowedAction();
        HTTPResponse response =  action.generateResponse(bogusRequest(), routerFake, uriProcessor());
        assertEquals(METHOD_NOT_ALLOWED, response.getStatusCode());
        assertEquals(expectedAllowedMethods(), response.getEntityHeaders());
    }

    private Map<EntityHeaderFields, List<String>> expectedAllowedMethods() {
        Map<EntityHeaderFields, List<String>> allowed = new HashMap<>();
        allowed.put(ALLOW, new ArrayList<>(allowedMethods));
        return allowed;
    }

    private HTTPRequest bogusRequest() {
        return new HTTPRequest(HTTPMethod.UNDEFINED, HTTPResource.FORM, HTTPVersion.HTTP_1_1, null, null, null);
    }

    private URIProcessor uriProcessor() {
        String pathToPublicDirectory = "/Users/sarahjohnston/Sarah/CobSpec/public/";
        return new URIProcessor(pathToPublicDirectory);
    }
}
