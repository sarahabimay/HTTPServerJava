package response;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static request.HTTPMethod.GET;
import static request.HTTPMethod.HEAD;
import static request.HTTPVersion.HTTP_1_1;
import static response.EntityHeaderFields.ALLOW;
import static response.HTTPStatusCode.*;

public class ResponseHTTPMessageFormatterTest {
    private ResponseHTTPMessageFormatter responseFormatter;

    @Before
    public void setUp() {
        responseFormatter = new ResponseHTTPMessageFormatter();
    }

    @Test
    public void generateResponseWithoutBody() {
        byte[] byteResponse = responseFormatter.buildRawHTTPResponse(statusOKResponse());
        assertEquals(expectedStatusOKResponse(), new String(byteResponse));
    }

    @Test
    public void generateResponseWithEmptyBody() {
        HTTPResponse response = statusOKResponse().setBody(new byte[0]);
        byte[] byteResponse = responseFormatter.buildRawHTTPResponse(response);
        assertEquals(expectedStatusOKResponse() + doubleCarriageReturn(), new String(byteResponse));
    }

    @Test
    public void generateResponseWithBodyContents() {
        HTTPResponse response = statusOKResponse().setBody("Some Text".getBytes());
        byte[] byteResponse = responseFormatter.buildRawHTTPResponse(response);
        String expectedResponse = expectedStatusOKResponse() + doubleCarriageReturn() + "Some Text";
        assertEquals(expectedResponse, new String(byteResponse));
    }

    @Test
    public void generateResponseWithAllowContents() {
        HTTPResponse response = setEntityHeaders(statusOKResponse());
        byte[] byteResponse = responseFormatter.buildRawHTTPResponse(response);
        assertEquals(expectedStatusOKResponse() + "\r\nAllow: GET,HEAD", new String(byteResponse));
    }

    private HTTPResponse setEntityHeaders(HTTPResponse response) {
        Map<EntityHeaderFields, List<String>> headers = new HashMap<>();
        headers.put(ALLOW, allowedMethods());
        return response.setEntityHeaders(headers);
    }

    private ArrayList<String> allowedMethods() {
        return new ArrayList<>(Arrays.asList(GET.method(), HEAD.method()));
    }

    private String expectedStatusOKResponse() {
        return new StringBuilder()
                .append(HTTP_1_1.version())
                .append(" ")
                .append(OK.statusCode())
                .append(" ")
                .append(OK.reason())
                .toString();
    }

    private HTTPResponse statusOKResponse() {
        return new HTTPResponse(new ResponseHTTPMessageFormatter())
                .setStatusLine(HTTP_1_1, OK);
    }

    private String doubleCarriageReturn() {
        return "\r\n\r\n";
    }
}
