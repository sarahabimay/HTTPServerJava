package routeActions;

import org.junit.Before;
import org.junit.Test;
import request.HTTPMethod;
import request.HTTPRequest;
import response.EntityHeaderFields;
import response.HTTPResponse;
import router.RouterFake;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static request.HTTPMethod.GET;
import static request.HTTPMethod.PUT;
import static request.HTTPResource.FORM;
import static request.HTTPVersion.HTTP_1_1;
import static response.EntityHeaderFields.ALLOW;
import static response.HTTPStatusCode.METHOD_NOT_ALLOWED;

public class MethodNotAllowedActionTest {
    private URIProcessor uriProcessor;

    @Before
    public void setUp() {
        uriProcessor = new URIProcessor("/Users/sarahjohnston/Sarah/CobSpec/public/");
    }

    @Test
    public void generateMethodNotFoundResponse() {
        RouterFake routerFake = new RouterFake();
        routerFake.setFakeResourceMethods(allowedMethods());
        MethodNotAllowedAction action = new MethodNotAllowedAction();
        HTTPResponse response =  action.generateResponse(bogusRequest(), routerFake, uriProcessor);
        assertEquals(METHOD_NOT_ALLOWED, response.getStatusCode());
        assertEquals(expectedAllowedMethods(), response.getEntityHeaders());
    }

    private Map<EntityHeaderFields, List<String>> expectedAllowedMethods() {
        Map<EntityHeaderFields, List<String>> allowed = new HashMap<>();
        allowed.put(ALLOW, allowedMethods());
        return allowed;
    }

    private HTTPRequest bogusRequest() {
        return new HTTPRequest(HTTPMethod.UNDEFINED, FORM, HTTP_1_1, null, null, null);
    }

    private List<String> allowedMethods() {
        return asList(GET.method(), PUT.method());
    }
}
