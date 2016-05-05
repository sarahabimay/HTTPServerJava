package request;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static request.HTTPMethod.GET;
import static request.HTTPRequestURI.INDEX;
import static request.HTTPVersion.HTTP_1_1;

public class HTTPRequestTest {
    @Test
    public void createGETWithNoBodyRequest() {
        HTTPRequest request = new HTTPRequest().addRequestLine(createGETRequestLine());
        assertEquals(GET, request.method());
        assertEquals(INDEX, request.uri());
        assertEquals(HTTP_1_1, request.version());
    }

    private ArrayList<String> createGETRequestLine() {
        String method = GET.method();
        String uri = INDEX.uri();
        String version = HTTP_1_1.version();
        return new ArrayList<>(Arrays.asList(method, uri, version));
    }
}
