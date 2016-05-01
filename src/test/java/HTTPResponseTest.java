import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class HTTPResponseTest {

    private HTTPRequest httpRequest;
    private HTTPResponse response;

    @Before
    public void setUp() {
        httpRequest = new HTTPRequest();
        httpRequest.addRequestLine(new ArrayList<>(Arrays.asList("GET", "/", "HTTP/1.1")));
        response = new HTTPResponse();
        response.setStatusLine(httpRequest.version(), 200, "OK");
    }

    @Test
    public void generateResponseWithoutBody() {
        byte[] byteResponse = response.buildResponse();
        String expectedResponse = "HTTP/1.1 200 OK/n/n";
        assertEquals(expectedResponse, new String(byteResponse));
    }

    @Test
    public void generateResponseWithEmptyBody() {
        response.setBody("");
        byte[] byteResponse = response.buildResponse();
        String expectedResponse = "HTTP/1.1 200 OK/n/n";
        assertEquals(expectedResponse, new String(byteResponse));
    }

    @Test
    public void generateResponseWithBodyContents() {
        response.setBody("Some Text");
        byte[] byteResponse = response.buildResponse();
        String expectedResponse = "HTTP/1.1 200 OK/n/nSome Text";
        assertEquals(expectedResponse, new String(byteResponse));
    }
}
