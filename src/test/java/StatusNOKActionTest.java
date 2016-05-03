import org.junit.Test;
import request.HTTPRequest;
import response.HTTPResponse;
import routeActions.StatusNOKAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static request.HTTPMethod.GET;
import static request.HTTPRequestURI.FOOBAR;
import static request.HTTPVersion.HTTP_1_1;
import static response.HTTPStatusCode.NOT_FOUND;

public class StatusNOKActionTest {
    @Test
    public void createStatusNOKResponse() {
        StatusNOKAction action = new StatusNOKAction();
        HTTPRequest request = new HTTPRequest().addRequestLine(requestUnavailableResource());
        HTTPResponse response = action.generateResponse(request);
        assertEquals(statusNOKResponseLine(), response.getStatusLine());
    }

    private List<String> requestUnavailableResource() {
        return new ArrayList<>(Arrays.asList(GET.method(), FOOBAR.uri(), HTTP_1_1.version()));
    }

    private String statusNOKResponseLine() {
        return new StringBuilder()
                .append(HTTP_1_1.version())
                .append(" ")
                .append(NOT_FOUND.statusCode())
                .append(" ")
                .append(NOT_FOUND.reason())
                .toString();
    }
}
