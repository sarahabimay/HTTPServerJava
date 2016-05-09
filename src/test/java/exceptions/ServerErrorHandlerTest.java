package exceptions;

import org.junit.Test;
import request.HTTPRequest;

import static org.junit.Assert.assertEquals;
import static request.HTTPMethod.UNDEFINED;

public class ServerErrorHandlerTest {
    @Test
    public void buildInvalidHTTPRequest() {
        ServerErrorHandler errorHandler = new ServerErrorHandler();
        HTTPRequest request = errorHandler.buildInvalidRequest("Error message");
        assertEquals(UNDEFINED, request.method());
        assertEquals("Error message", request.body());
    }
}
