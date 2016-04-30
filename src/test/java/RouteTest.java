import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RouteTest {

    private final String STATUS_OK_RESPONSE = "HTTP/1.1 200 OK";

    @Test
    public void routeDoesNotMatchGivenPath() {
        Route route = new Route("GET", "/");
        Map<String, String> request = ImmutableMap.of(
                "method", "GET",
                "path", "/foobar",
                "version", "HTTP/1.1",
                "data", "my data");
        assertEquals(false, route.isMatch(request));
    }

    @Test
    public void routeMatchesGETPath() {
        Route route = new Route("GET", "/");
        Map<String, String> request = ImmutableMap.of(
                "method", "GET",
                "path", "/",
                "version", "HTTP/1.1",
                "data", "my data");
        assertEquals(true, route.isMatch(request));
    }

    @Test
    public void routeMatchesPostPath() {
        Route route = new Route("POST", "/form");
        Map<String, String> request = ImmutableMap.of(
                "method", "POST",
                "path", "/form",
                "version", "HTTP/1.1",
                "data", "my data");
        assertEquals(true, route.isMatch(request));
    }

    @Test
    public void routeMatchesPUTPath() {
        Route route = new Route("PUT", "/form");
        Map<String, String> request = ImmutableMap.of(
                "method", "PUT",
                "path", "/form",
                "version", "HTTP/1.1",
                "data", "my data");
        assertEquals(true, route.isMatch(request));
    }

    @Test
    public void createStatusOKResponse() {
        Route route = new Route("GET", "/");
        assertEquals(STATUS_OK_RESPONSE, route.response());
    }

    @Test
    public void statusOKForPostRequest() {
        Route route = new Route("POST", "/form");
        assertEquals(STATUS_OK_RESPONSE, route.response());
    }

    @Test
    public void statusOKForPutRequest() {
        Route route = new Route("PUT", "/form");
        assertEquals(STATUS_OK_RESPONSE, route.response());
    }
}
