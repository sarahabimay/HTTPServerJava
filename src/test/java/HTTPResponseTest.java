import org.junit.Before;
import org.junit.Test;
import request.HTTPMethod;
import request.HTTPRequest;
import request.HTTPRequestURI;
import request.HTTPVersion;
import response.HTTPResponse;
import response.HTTPStatusCode;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class HTTPResponseTest {
    private HTTPRequest httpRequest;
    private HTTPResponse response;
    private String expectedResponse;

    @Before
    public void setUp() {
        httpRequest = new HTTPRequest();
        httpRequest.addRequestLine(createGETRequestLine());
        response = statusOKResponse();
        expectedResponse = expectedStatusOKResponse();
    }

    @Test
    public void generateResponseWithoutBody() {
        byte[] byteResponse = response.buildResponse();
        assertEquals(expectedResponse, new String(byteResponse));
    }

    @Test
    public void generateResponseWithEmptyBody() {
        response.setBody("");
        byte[] byteResponse = response.buildResponse();
        assertEquals(expectedResponse, new String(byteResponse));
    }

    @Test
    public void generateResponseWithBodyContents() {
        String body = "Some Text";
        response.setBody(body);
        byte[] byteResponse = response.buildResponse();
        assertEquals(expectedResponse + body, new String(byteResponse));
    }

    private ArrayList<String> createGETRequestLine() {
        String method = HTTPMethod.GET.method();
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
                .append("/n/n")
                .toString();
    }

    private HTTPResponse statusOKResponse() {
        HTTPResponse response = new HTTPResponse();
        response.setStatusLine(HTTPVersion.HTTP_1_1, HTTPStatusCode.OK);
        return response;
    }
}
