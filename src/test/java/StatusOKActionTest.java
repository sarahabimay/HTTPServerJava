import org.junit.Test;
import request.HTTPVersion;
import response.HTTPResponse;
import response.HTTPStatusCode;
import routeActions.StatusOKAction;

import static org.junit.Assert.assertEquals;

public class StatusOKActionTest {
    @Test
    public void createStatusOkResponse() {
        StatusOKAction action = new StatusOKAction();
        HTTPRequestSpy requestSpy = new HTTPRequestSpy();
        HTTPResponse response = action.generateResponse(requestSpy);
        assertEquals(statusOKResponseLine(), response.getStatusLine());
    }

    private String statusOKResponseLine() {
        return new StringBuilder()
                .append(HTTPVersion.HTTP_1_1.version())
                .append(" ")
                .append(HTTPStatusCode.OK.statusCode())
                .append(" ")
                .append(HTTPStatusCode.OK.reason())
                .toString();
    }
}
