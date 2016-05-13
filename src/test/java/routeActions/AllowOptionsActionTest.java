package routeActions;

import configuration.Configuration;
import messages.EntityHeaderBuilder;
import org.junit.Test;
import request.HTTPRequest;
import request.HTTPResource;
import response.HTTPResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static messages.EntityHeaderFields.ALLOW;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static request.HTTPMethod.*;
import static request.HTTPResource.OPTIONS_ONE;
import static request.HTTPVersion.HTTP_1_1;

public class AllowOptionsActionTest {
    @Test
    public void optionsMethodOnlyAllowed() {
        ConfigurationFake configuration = new ConfigurationFake();
        AllowOptionsAction action = new AllowOptionsAction(new EntityHeaderBuilder(configuration));
        HTTPResponse response = action.generateResponse(createOptionsRequest());
        assertEquals("HTTP/1.1 200 OK", response.getStatusLine());
        assertEquals(ALLOW, response.getEntityHeaders().keySet().toArray()[0]);
        assertThat(response.getEntityHeaders().get(ALLOW), hasItem(OPTIONS.method()));
    }

    @Test
    public void createOptionResponse() {
        AllowOptionsAction action = new AllowOptionsAction(new EntityHeaderBuilder(new Configuration()));
        HTTPResponse response = action.generateResponse(createOptionsRequest());
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

    private class ConfigurationFake extends Configuration {
        private Map<HTTPResource, List<String>> methodsNotAllowed;

        public ConfigurationFake() {
            methodsNotAllowed = initializeMethodsNotAllowed();
        }

        public Map<HTTPResource, List<String>> methodsNotAllowed() {
            return methodsNotAllowed;
        }

        private Map<HTTPResource, List<String>> initializeMethodsNotAllowed() {
            Map<HTTPResource, List<String>> notAllowedMethods = new HashMap<>();
            notAllowedMethods.put(OPTIONS_ONE, asList(
                    DELETE.method(),
                    GET.method(),
                    PATCH.method(),
                    POST.method(),
                    PUT.method(),
                    HEAD.method()));
            return notAllowedMethods;
        }
    }
}
