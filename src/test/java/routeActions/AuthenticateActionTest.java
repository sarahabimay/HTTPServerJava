package routeActions;

import configuration.Configuration;
import messages.EntityHeaderFields;
import org.junit.Before;
import org.junit.Test;
import request.HTTPRequest;
import response.HTTPResponse;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static messages.EntityHeaderFields.AUTHENTICATE;
import static messages.EntityHeaderFields.AUTHORIZATION;
import static org.junit.Assert.assertEquals;
import static request.HTTPMethod.GET;
import static request.HTTPResource.*;
import static request.HTTPVersion.HTTP_1_1;
import static response.HTTPStatusCode.OK;

public class AuthenticateActionTest {

    private AuthenticateAction action;

    @Before
    public void setUp() {
        action = new AuthenticateAction(new Configuration()
                .addAuthorisationCredentials("admin:hunter2")
                .addResourcesRequiringAuth(Arrays.asList(LOGS, LOG, THESE, REQUESTS)));
    }

    @Test
    public void authenticationRequiredForLOGSResource() {
        HTTPRequest request = new HTTPRequest(GET, LOGS, HTTP_1_1, null, emptyCredentials(), null);
        assertEquals(true, action.isAppropriate(request));
    }

    @Test
    public void authenticationRequiredForLOGResource() {
        HTTPRequest request = new HTTPRequest(GET, LOG, HTTP_1_1, null, emptyCredentials(), null);
        assertEquals(true, action.isAppropriate(request));
    }

    @Test
    public void authenticationRequiredForTHESEResource() {
        HTTPRequest request = new HTTPRequest(GET, THESE, HTTP_1_1, null, emptyCredentials(), null);
        assertEquals(true, action.isAppropriate(request));
    }

    @Test
    public void authenticationRequiredForREQUESTResource() {
        HTTPRequest request = new HTTPRequest(GET, REQUESTS, HTTP_1_1, null, emptyCredentials(), null);
        assertEquals(true, action.isAppropriate(request));
    }

    @Test
    public void authenticationNotRequiredForResource() {
        HTTPRequest request = new HTTPRequest(GET, FORM, HTTP_1_1, null, emptyCredentials(), null);
        assertEquals(false, action.isAppropriate(request));
    }

    @Test
    public void requestReceivedWithoutCredentials() {
        HTTPRequest request = new HTTPRequest(GET, LOGS, HTTP_1_1, null, emptyCredentials(), null);
        HTTPResponse response = action.generateResponse(request);
        assertEquals(true, response.getEntityHeaders().containsKey(AUTHENTICATE));
    }

    @Test
    public void requestReceivedWithCredentials() {
        HTTPRequest request = new HTTPRequest(GET, LOGS, HTTP_1_1, null, base64Credentials(), null);
        HTTPResponse response = action.generateResponse(request);
        assertEquals(OK, response.getStatusCode());
    }

    @Test
    public void authorizedRequestRespondsWithBody() {
        HTTPRequest request = new HTTPRequest(GET, LOGS, HTTP_1_1, null, base64Credentials(), null);
        HTTPResponse response = action.generateResponse(request);
        assertEquals(expectedBodyContainsProtectionSpace(), new String(response.getBody()));
    }

    private String expectedBodyContainsProtectionSpace() {
        return String.format("%s\n%s\n%s\n", "GET /log HTTP/1.1", "PUT /these HTTP/1.1", "HEAD /requests HTTP/1.1");
    }

    private Map<EntityHeaderFields, String> base64Credentials() {
        Map<EntityHeaderFields, String> headers = new HashMap<>();
        headers.put(AUTHORIZATION, "Basic YWRtaW46aHVudGVyMg==");
        return headers;
    }

    private Map<EntityHeaderFields, String> emptyCredentials() {
        return new HashMap<>();
    }
}
