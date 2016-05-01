import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class HTTPRequestTest {
    @Test
    public void createGETWithNoBodyRequest() {
        HTTPRequest request = new HTTPRequest();
        String method = "GET";
        String uri = "/";
        String version = "HTTP/1.1";
        request.addRequestLine(new ArrayList<>(Arrays.asList(method, uri, version)));
        assertEquals(method, request.method());
        assertEquals(uri, request.uri());
        assertEquals(version, request.version());
    }
}
