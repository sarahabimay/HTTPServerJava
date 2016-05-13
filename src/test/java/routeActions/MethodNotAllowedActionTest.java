package routeActions;

import configuration.Configuration;
import messages.EntityHeaderBuilder;
import org.junit.Before;
import org.junit.Test;
import request.HTTPRequest;
import response.HTTPResponse;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static request.HTTPMethod.*;
import static request.HTTPResource.FILE1;
import static request.HTTPResource.FORM;
import static request.HTTPVersion.HTTP_1_1;
import static messages.EntityHeaderFields.ALLOW;
import static response.HTTPStatusCode.METHOD_NOT_ALLOWED;

public class MethodNotAllowedActionTest {
    private MethodNotAllowedAction action;

    @Before
    public void setUp() {
        Configuration configuration = new Configuration();
        action = new MethodNotAllowedAction(new EntityHeaderBuilder(configuration), configuration);
    }

    @Test
    public void methodNotAllowedForResource() {
        HTTPRequest request = new HTTPRequest(PUT, FILE1, HTTP_1_1, null, null, null);
        assertEquals(true, action.isAppropriate(request));
    }

    @Test
    public void methodAllowedForResource() {
        HTTPRequest request = new HTTPRequest(GET, FILE1, HTTP_1_1, null, null, null);
        assertEquals(false, action.isAppropriate(request));
    }

    @Test
    public void bogusMethodRequestHasNoAllowedMethods() {
        HTTPResponse response =  action.generateResponse(bogusRequest());
        assertEquals(METHOD_NOT_ALLOWED, response.getStatusCode());
        assertEquals(null, response.getEntityHeaders().get(ALLOW));
    }

    @Test
    public void responseHasAllowedMethods() {
        HTTPRequest request = new HTTPRequest(PUT, FILE1, HTTP_1_1, null, null, null);
        HTTPResponse response =  action.generateResponse(request);
        assertEquals(METHOD_NOT_ALLOWED, response.getStatusCode());
        assertEquals(allowedMethods(), response.getEntityHeaders().get(ALLOW));
    }

    private HTTPRequest bogusRequest() {
        return new HTTPRequest(UNDEFINED, FORM, HTTP_1_1, null, null, null);
    }

    private List<String> allowedMethods() {
        return asList(GET.method(), POST.method(), HEAD.method(), OPTIONS.method(), DELETE.method(), PATCH.method());
    }
}
