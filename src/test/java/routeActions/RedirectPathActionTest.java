package routeActions;

import org.junit.Test;
import request.HTTPMethod;
import request.HTTPRequest;
import request.HTTPResource;
import request.HTTPVersion;
import response.EntityHeaderFields;
import response.HTTPResponse;
import router.RouterStub;
import router.URIProcessorStub;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static request.HTTPResource.INDEX;
import static request.HTTPResource.REDIRECT;
import static response.EntityHeaderFields.LOCATION;

public class RedirectPathActionTest {
    @Test
    public void redirectedResponseHasLocationHeader() {
        RedirectPathAction action = new RedirectPathAction();
        HTTPResponse response = action.generateResponse(getRequest(REDIRECT), new RouterStub(), new URIProcessorStub());
        System.out.println("Response headers: " + response.getEntityHeaders().get(EntityHeaderFields.LOCATION));
        assertEquals("HTTP/1.1 302 Found", response.getStatusLine());
        assertThat(response.getEntityHeaders().get(LOCATION), hasItem(String.format("http://localhost:5000%s", INDEX.uri())));
    }

    private HTTPRequest getRequest(HTTPResource redirect) {
        return new HTTPRequest(HTTPMethod.GET, redirect, HTTPVersion.HTTP_1_1, null, null, null);
    }
}
