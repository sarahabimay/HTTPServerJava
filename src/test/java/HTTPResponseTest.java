import org.junit.Before;
import org.junit.Test;

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
        httpRequest.addRequestLine(new ArrayList<>(Arrays.asList(HTTPMethod.GET.toString(), "/", "HTTP/1.1")));
        response = new HTTPResponse();
        response.setStatusLine(httpRequest.version(), HTTPStatusCode.OK.statusCode(), HTTPStatusCode.OK.reason());
        expectedResponse = "HTTP/1.1 " +
                HTTPStatusCode.OK.statusCode() +  " " +
                HTTPStatusCode.OK.reason() + "/n/n";
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
}
