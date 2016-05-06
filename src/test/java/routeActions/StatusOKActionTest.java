package routeActions;

import org.junit.Test;
import request.HTTPRequestFake;
import response.HTTPResponse;
import router.RouterStub;
import router.URIProcessorStub;

import static org.junit.Assert.assertEquals;
import static request.HTTPVersion.HTTP_1_1;
import static response.HTTPStatusCode.OK;

public class StatusOKActionTest {
    @Test
    public void createStatusOkResponse() {
        StatusOKAction action = new StatusOKAction();
        HTTPResponse response = action.generateResponse(new HTTPRequestFake(), new RouterStub() , new URIProcessorStub());
        assertEquals(statusOKResponseLine(), response.getStatusLine());
    }

    private String statusOKResponseLine() {
        return new StringBuilder()
                .append(HTTP_1_1.version())
                .append(" ")
                .append(OK.statusCode())
                .append(" ")
                .append(OK.reason())
                .toString();
    }
}
