package response;

import messages.EntityHeaderFields;
import org.junit.Test;
import request.HTTPVersion;

import java.util.*;

import static junit.framework.TestCase.assertEquals;
import static request.HTTPMethod.GET;
import static request.HTTPMethod.HEAD;
import static messages.EntityHeaderFields.ALLOW;

public class HTTPResponseTest {

    @Test
    public void statusLineOKForGET() {
        byte[] httpResponse =
                new HTTPResponse(new ResponseHTTPMessageFormatter())
                        .setStatusLine(HTTPVersion.HTTP_1_1, HTTPStatusCode.OK)
                        .buildResponse();
        assertEquals(expectedStatusOKResponse(), new String(httpResponse));
    }

    @Test
    public void generateResponseWithAllowContents() {
        byte[] httpResponse =
                new HTTPResponse(new ResponseHTTPMessageFormatter())
                        .setStatusLine(HTTPVersion.HTTP_1_1, HTTPStatusCode.OK)
                        .setEntityHeaders(setEntityHeaders())
                        .buildResponse();
        assertEquals(expectedStatusOKResponse() + "\r\nAllow: GET,HEAD", new String(httpResponse));
    }

    private Map<EntityHeaderFields, List<String>> setEntityHeaders() {
        Map<EntityHeaderFields, List<String>> headers = new HashMap<>();
        headers.put(ALLOW, allowedMethods());
        return headers;
    }

    private ArrayList<String> allowedMethods() {
        return new ArrayList<>(Arrays.asList(GET.method(), HEAD.method()));
    }

    private String expectedStatusOKResponse() {
        return new StringBuilder()
                .append(HTTPVersion.HTTP_1_1.version())
                .append(" ")
                .append(HTTPStatusCode.OK.statusCode())
                .append(" ")
                .append(HTTPStatusCode.OK.reason())
                .toString();
    }
}
