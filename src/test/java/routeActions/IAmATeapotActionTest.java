package routeActions;

import org.junit.Before;
import org.junit.Test;
import request.HTTPMethod;
import request.HTTPRequest;
import request.HTTPResource;
import request.HTTPVersion;
import messages.EntityHeaderFields;
import response.HTTPResponse;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static request.HTTPMethod.GET;
import static request.HTTPResource.COFFEE;
import static request.HTTPResource.TEA;
import static request.HTTPVersion.HTTP_1_1;

public class IAmATeapotActionTest {

    private IAmATeapotAction action;

    @Before
    public void setUp() {
        action = new IAmATeapotAction();
    }

    @Test
    public void requestCanHaveFourEighteenActionApplied() {
        boolean isAppropriate = action.isAppropriate(newGETRequest(GET, COFFEE, HTTP_1_1, null));
        assertEquals(true, isAppropriate);
    }

    @Test
    public void requestCoffeeButGetsFourEighteen() {
        HTTPResponse response = action.generateResponse(
                newGETRequest(GET, COFFEE, HTTP_1_1, null)
        );
        assertEquals("HTTP/1.1 418 I'm a teapot", response.getStatusLine());
        assertEquals("I'm a teapot", new String(response.getBody()));
    }

    @Test
    public void requestTeaIsSuccessful() {
        HTTPResponse response = action.generateResponse(
                newGETRequest(GET, TEA, HTTP_1_1, null)
        );
        assertEquals("HTTP/1.1 200 OK", response.getStatusLine());
    }

    private HTTPRequest newGETRequest(HTTPMethod method, HTTPResource uri, HTTPVersion version, Map<EntityHeaderFields, String> headerFields) {
        return new HTTPRequest(method, uri, version, null, null, null);
    }
}
