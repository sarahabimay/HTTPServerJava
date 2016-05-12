package routeActions;

import org.junit.Before;
import org.junit.Test;
import request.HTTPRequest;
import response.HTTPResponse;

import static org.junit.Assert.assertEquals;
import static request.HTTPMethod.GET;
import static request.HTTPResource.PARAMETERS;
import static request.HTTPVersion.HTTP_1_1;

public class ParameterDecodeActionTest {

    private ParameterDecodeAction action;

    @Before
    public void setUp() {
        action = new ParameterDecodeAction();
    }

    @Test
    public void requestHasQueryParametersToDecode() {
        HTTPRequest request = new HTTPRequest(GET, PARAMETERS, HTTP_1_1, queryParameters(), null, null );
        assertEquals(true, action.isAppropriate(request));
    }

    @Test
    public void requestHasNoQueryParametersToDecode() {
        HTTPRequest request = new HTTPRequest(GET, PARAMETERS, HTTP_1_1, null, null, null );
        assertEquals(false, action.isAppropriate(request));
    }

    @Test
    public void decodeQueryParameters() {
        HTTPRequest request = new HTTPRequest(GET, PARAMETERS, HTTP_1_1, queryParameters(), null, null );
        HTTPResponse response = action.generateResponse(request);
        assertEquals(expectedDecodedParams(), new String(response.getBody()));
    }

    private String queryParameters() {
        return "variable_1=Operators%20%3C%2C%20%3E%2C%20%3D%2C%20!%3D%3B%20%2B%2C%20-%2C%20*%2C%20%26%2C%20%40%2C%20%23%2C%20%24%2C%20%5B%2C%20%5D%3A%20%22is%20that%20all%22%3F&variable_2=stuff";
    }

    private String expectedDecodedParams() {
        return "variable_1 = Operators <, >, =, !=; +, -, *, &, @, #, $, [, ]: \"is that all\"?" + "\n" +
                "variable_2 = stuff";
    }
}
