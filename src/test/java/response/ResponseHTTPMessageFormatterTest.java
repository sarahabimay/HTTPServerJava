package response;

import messages.EntityHeaderFields;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static request.HTTPMethod.GET;
import static request.HTTPMethod.HEAD;
import static request.HTTPVersion.HTTP_1_1;
import static messages.EntityHeaderFields.*;
import static response.HTTPStatusCode.OK;
import static response.HTTPStatusCode.PARTIAL_CONTENT;

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
        HTTPResponse response = responseWithAllowHeader();
        byte[] byteResponse = responseFormatter.buildRawHTTPResponse(response);
        assertEquals(expectedStatusOKResponse() + "\r\nAllow: GET,HEAD", new String(byteResponse));
    }

    @Test
    public void generatePartialContentResponse() {
        HTTPResponse response = partialContentResponse();
        byte[] byteResponse = responseFormatter.buildRawHTTPResponse(response);
        assertThat(new String(byteResponse), containsString("Content-Length: 5"));
        assertThat(new String(byteResponse), containsString("Content-Range: bytes 0-4"));
        assertThat(new String(byteResponse), containsString("Content-Type: text/plain"));
    }

    private HTTPResponse partialContentResponse() {
        return new HTTPResponse(new ResponseHTTPMessageFormatter())
                .setStatusLine(HTTP_1_1, PARTIAL_CONTENT)
                .setEntityHeaders(fakePartialContentResponse())
                .setBody("This ".getBytes());
    }

    private Map<EntityHeaderFields, List<String>> fakePartialContentResponse() {
        Map<EntityHeaderFields, List<String>> headerFields = new HashMap<>();
        headerFields.put(CONTENT_LENGTH, Arrays.asList("5"));
        headerFields.put(CONTENT_TYPE, Arrays.asList(CONTENT_TYPE_PLAIN.field()));
        headerFields.put(CONTENT_RANGE, Arrays.asList("bytes 0-4"));
        return headerFields;
    }

    private HTTPResponse responseWithAllowHeader() {
        Map<EntityHeaderFields, List<String>> headers = new HashMap<>();
        headers.put(ALLOW, allowedMethods());
        return statusOKResponse().setEntityHeaders(headers);
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
