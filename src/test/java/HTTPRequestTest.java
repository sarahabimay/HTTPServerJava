import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class HTTPRequestTest {
    @Test
    public void createGETWithNoBodyRequest() {
        HTTPRequest request = new HTTPRequest();
        request.addRequestLine(createGETRequestLine());
        assertEquals(HTTPMethod.GET, request.method());
        assertEquals(HTTPRequestURI.INDEX, request.uri());
        assertEquals(HTTPVersion.HTTP_1_1, request.version());
    }

    private ArrayList<String> createGETRequestLine() {
        String method = HTTPMethod.GET.method();
        String uri = HTTPRequestURI.INDEX.uri();
        String version = HTTPVersion.HTTP_1_1.version();
        return new ArrayList<>(Arrays.asList(method, uri, version));
    }
}
