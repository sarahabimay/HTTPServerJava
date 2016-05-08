package routeActions;

import org.junit.Before;
import org.junit.Test;
import request.HTTPRequest;
import response.EntityHeaderFields;
import response.HTTPResponse;
import router.RouterStub;
import router.URIProcessorStub;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static request.HTTPMethod.GET;
import static request.HTTPResource.LOGS;
import static request.HTTPVersion.HTTP_1_1;
import static response.EntityHeaderFields.*;
import static response.HTTPStatusCode.OK;

public class AuthenticateActionTest {

    private AuthenticateAction action;

    @Before
    public void setUp() {
        action = new AuthenticateAction();
    }

    @Test
    public void authenticationCanBeAppliedToAnyRoute() {
        assertEquals(true, action.isAppropriate(new HTTPRequest()));
    }

    @Test
    public void requestReceivedWithoutCredentials() {
        HTTPRequest request = new HTTPRequest(GET, LOGS, HTTP_1_1, null, emptyCredentials(), null);
        HTTPResponse response = action.generateResponse(request, new RouterStub(), new URIProcessorStub());
        assertEquals(true, response.getEntityHeaders().containsKey(AUTHENTICATE));
    }

    @Test
    public void requestReceivedWithCredentials() {
        HTTPRequest request = new HTTPRequest(GET, LOGS, HTTP_1_1, null, base64Credentials(), null);
        HTTPResponse response = action.generateResponse(request, new RouterStub(), new URIProcessorStub());
        assertEquals(OK, response.getStatusCode());
    }

    @Test
    public void authorizedRequestRespondsWithBody() {
        HTTPRequest request = new HTTPRequest(GET, LOGS, HTTP_1_1, null, base64Credentials(), null);
        HTTPResponse response = action.generateResponse(request, new RouterStub(), new URIProcessorStub());
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

    private Map<EntityHeaderFields,String> emptyCredentials() {
        return new HashMap<>();
    }
}
