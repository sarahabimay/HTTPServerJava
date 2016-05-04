import org.junit.Before;
import org.junit.Test;
import request.HTTPMethod;
import request.HTTPRequestURI;
import request.HTTPVersion;
import response.EntityHeaderFields;
import response.HTTPResponse;
import response.HTTPStatusCode;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static request.HTTPMethod.GET;
import static request.HTTPMethod.HEAD;
import static response.EntityHeaderFields.*;

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
        response.setBody("");
        byte[] byteResponse = response.buildResponse();
        assertEquals(expectedStatusOKResponse() + doubleCarriageReturn(), new String(byteResponse));
    }

    @Test
    public void generateResponseWithBodyContents() {
        response.setBody("Some Text");
        byte[] byteResponse = response.buildResponse();
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
        Map<EntityHeaderFields, List<HTTPMethod>> headers = new HashMap<>();
        headers.put(ALLOW, allowedMethods());
        response.setEntityHeaders(headers);
        return response;
    }

    private ArrayList<HTTPMethod> allowedMethods() {
        return new ArrayList<>(Arrays.asList(GET, HEAD));
    }

    private ArrayList<String> createGETRequestLine() {
        String method = GET.method();
        String uri = HTTPRequestURI.INDEX.uri();
        String version = HTTPVersion.HTTP_1_1.version();
        return new ArrayList<>(Arrays.asList(method, uri, version));
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
        HTTPResponse response = new HTTPResponse();
        response.setStatusLine(HTTPVersion.HTTP_1_1, HTTPStatusCode.OK);
        return response;
    }

    private String doubleCarriageReturn() {
        return "\r\n\r\n";
    }
}
