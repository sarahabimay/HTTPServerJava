import org.junit.Test;
import request.HTTPMethod;
import request.HTTPRequest;
import request.HTTPRequestURI;
import request.HTTPVersion;
import response.HTTPResponse;
import response.HTTPStatusCode;
import routeActions.StatusNOKAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class StatusNOKActionTest {
    @Test
    public void createStatusNOKResponse() {
        StatusNOKAction action = new StatusNOKAction();
        HTTPRequest request = new HTTPRequest();
        request.addRequestLine(requestUnavailableResource());
        HTTPResponse response = action.generateResponse(request);
        assertEquals(statusNOKResponseLine(), response.getStatusLine());
    }

    private List<String> requestUnavailableResource() {
        return new ArrayList<>(Arrays.asList(HTTPMethod.GET.method(), HTTPRequestURI.FOOBAR.uri(), HTTPVersion.HTTP_1_1.version()));
    }

    private String statusNOKResponseLine() {
        return new StringBuilder()
                .append(HTTPVersion.HTTP_1_1.version())
                .append(" ")
                .append(HTTPStatusCode.NOT_FOUND.statusCode())
                .append(" ")
                .append(HTTPStatusCode.NOT_FOUND.reason())
                .toString();
    }
}
