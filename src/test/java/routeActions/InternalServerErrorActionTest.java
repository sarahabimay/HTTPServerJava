package routeActions;

import org.junit.Test;
import response.HTTPResponse;
import response.HTTPStatusCode;

import static org.junit.Assert.assertEquals;

public class InternalServerErrorActionTest {
    @Test
    public void internalServerErrorGenerated() {
        InternalServerErrorAction error = new InternalServerErrorAction();
        HTTPResponse response = error.generateResponse(null);
        assertEquals(HTTPStatusCode.SERVER_ERROR, response.getStatusCode());
    }
}
