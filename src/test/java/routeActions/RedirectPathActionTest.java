package routeActions;

import org.junit.Before;
import org.junit.Test;
import request.HTTPRequest;
import request.HTTPResource;
import response.HTTPResponse;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static request.HTTPMethod.GET;
import static request.HTTPResource.*;
import static request.HTTPResource.INDEX;
import static request.HTTPResource.REDIRECT;
import static request.HTTPVersion.HTTP_1_1;
import static response.EntityHeaderFields.LOCATION;

public class RedirectPathActionTest {

    private RedirectPathAction action;

    @Before
    public void setUp() {
        action = new RedirectPathAction();
    }

    @Test
    public void redirectConfiguredResource() {
        HTTPRequest request = new HTTPRequest(GET, REDIRECT, HTTP_1_1, null, null, null);
        assertEquals(true, action.isAppropriate(request));
    }

    @Test
    public void nonConfiguredResourceIsNotRedirectable() {
        HTTPRequest request = new HTTPRequest(GET, LOGS, HTTP_1_1, null, null, null);
        assertEquals(false, action.isAppropriate(request));
    }

    @Test
    public void redirectedResponseHasLocationHeader() {
        RedirectPathAction action = new RedirectPathAction();
        HTTPResponse response = action.generateResponse(getRequest(REDIRECT));
        assertEquals("HTTP/1.1 302 Found", response.getStatusLine());
        assertThat(response.getEntityHeaders().get(LOCATION), hasItem(String.format("http://localhost:5000%s", INDEX.uri())));
    }

    private HTTPRequest getRequest(HTTPResource redirect) {
        return new HTTPRequest(GET, redirect, HTTP_1_1, null, null, null);
    }
}
