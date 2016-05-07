package response;

import org.junit.Before;
import org.junit.Test;
import request.HTTPVersion;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static request.HTTPMethod.GET;
import static request.HTTPMethod.HEAD;
import static response.EntityHeaderFields.ALLOW;

public class HTTPResponseTest {
    private HTTPResponse response;

    @Before
    public void setUp() {
        response = statusOKResponse();
    }

    @Test
    public void generateResponseWithoutBody() {
        byte[] byteResponse = statusOKResponse().buildResponse();
        assertEquals(expectedStatusOKResponse(), new String(byteResponse));
    }

    @Test
    public void generateResponseWithEmptyBody() {
        byte[] byteResponse =
                response
                        .setBody(new byte[0])
                        .buildResponse();
        assertEquals(expectedStatusOKResponse() + doubleCarriageReturn(), new String(byteResponse));
    }

    @Test
    public void generateResponseWithBodyContents() {
        byte[] byteResponse =
                response
                        .setBody("Some Text".getBytes())
                        .buildResponse();
        String expectedResponse = expectedStatusOKResponse() + doubleCarriageReturn() + "Some Text";
        assertEquals(expectedResponse, new String(byteResponse));
    }

    @Test
    public void generateResponseWithAllowContents() {
        HTTPResponse response = setEntityHeaders(statusOKResponse());
        byte[] byteResponse = response.buildResponse();
        assertEquals(expectedStatusOKResponse() + "\r\nAllow: GET,HEAD\r\n", new String(byteResponse));
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
                .append(HTTPVersion.HTTP_1_1.version())
                .append(" ")
                .append(HTTPStatusCode.OK.statusCode())
                .append(" ")
                .append(HTTPStatusCode.OK.reason())
                .toString();
    }

    private HTTPResponse statusOKResponse() {
        HTTPResponse response =
                new HTTPResponse()
                        .setStatusLine(HTTPVersion.HTTP_1_1, HTTPStatusCode.OK);
        return response;
    }

    private String doubleCarriageReturn() {
        return "\r\n\r\n";
    }
}
