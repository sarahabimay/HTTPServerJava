package routeActions;

import configuration.Configuration;
import org.junit.Test;
import request.HTTPRequest;
import response.HTTPResponse;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static request.HTTPMethod.*;
import static request.HTTPResource.OPTIONS_ONE;
import static request.HTTPVersion.HTTP_1_1;
import static response.EntityHeaderFields.ALLOW;

public class AllowOptionsActionTest {
    @Test
    public void createOptionResponse() {
        HTTPResponse response = new AllowOptionsAction(new Configuration()).generateResponse(createOptionsRequest());
        assertEquals("HTTP/1.1 200 OK", response.getStatusLine());
        assertEquals(ALLOW, response.getEntityHeaders().keySet().toArray()[0]);
        assertThat(response.getEntityHeaders().get(ALLOW), hasItem(HEAD.method()));
        assertThat(response.getEntityHeaders().get(ALLOW), hasItem(POST.method()));
        assertThat(response.getEntityHeaders().get(ALLOW), hasItem(PUT.method()));
        assertThat(response.getEntityHeaders().get(ALLOW), hasItem(GET.method()));
        assertThat(response.getEntityHeaders().get(ALLOW), hasItem(OPTIONS.method()));
    }

    private HTTPRequest createOptionsRequest() {
        return new HTTPRequest().addRequestLine(optionsRequestLine());
    }

    private Map<String, String> optionsRequestLine() {
        Map<String, String> requestLine = new HashMap<>();
        requestLine.put("method", OPTIONS.method());
        requestLine.put("uri", OPTIONS_ONE.uri());
        requestLine.put("version", HTTP_1_1.version());
        return requestLine;
    }
}
