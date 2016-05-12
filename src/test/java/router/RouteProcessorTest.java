package router;

import configuration.Configuration;
import exceptions.ResourceManagementException;
import exceptions.ServerErrorHandler;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import request.HTTPMethod;
import request.HTTPRequest;
import request.HTTPResource;
import request.HTTPVersion;
import response.HTTPResponse;
import routeActions.HEADAction;
import routeActions.InternalServerErrorAction;
import routeActions.RouteAction;
import routeActions.URIProcessor;
import testHelper.TestHelpers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static request.HTTPMethod.*;
import static request.HTTPResource.*;
import static request.HTTPVersion.HTTP_1_1;
import static response.EntityHeaderFields.ALLOW;
import static response.HTTPStatusCode.SERVER_ERROR;

public class RouteProcessorTest {
    private String statusLineOKResponse;
    private String statusLineFourOhFourResponse;
    private RouteProcessor routeProcessor;
    private Configuration configuration;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    private File rootFolder;
    private TestHelpers testHelpers;

    @Before
    public void setUp() {
        testHelpers = new TestHelpers();

        statusLineOKResponse = "HTTP/1.1 200 OK";
        statusLineFourOhFourResponse = "HTTP/1.1 404 Not Found";

        try {
            rootFolder = temporaryFolder.newFolder("test");
        } catch (IOException e) {
            e.printStackTrace();
        }
        configuration = new Configuration();
        routeProcessor = new RouteProcessor(
                new RoutesFactory(new URIProcessor(rootFolder.getAbsolutePath()), new Configuration()),
                configuration,
                new ServerErrorHandler());
    }

    @Test
    public void buildInternalServerErrorResponse() {
        HTTPRequest request =
                createRequest(ERROR, UNRECOGNIZED, "Some Error Message", HTTPVersion.UNDEFINED);
        HTTPResponse response = routeProcessor.buildResponse(request);
        assertEquals("HTTP/1.1 500 Internal Server Error", response.getStatusLine());
    }

    @Test
    public void buildStatusOKResponse() {
        String content = "data=fatcat";
        testHelpers.createFileAtResource(rootFolder, "/form", content);
        HTTPRequest request = createRequest(GET, FORM, "", HTTP_1_1);
        HTTPResponse response = routeProcessor.buildResponse(request);
        assertEquals(statusLineOKResponse, response.getStatusLine());
    }

    @Test
    public void build404NotFoundResponse() {
        HTTPRequest request = createRequest(GET, FOOBAR, "", HTTP_1_1);
        HTTPResponse response = routeProcessor.buildResponse(request);
        assertEquals(statusLineFourOhFourResponse, response.getStatusLine());
    }

    @Test
    public void buildResponseForPOSTRequest() {
        String content = "data=fatcat";
        testHelpers.createFileAtResource(rootFolder, "/form", content);
        HTTPRequest request = createRequest(POST, FORM, "", HTTP_1_1);
        HTTPResponse response = routeProcessor.buildResponse(request);
        assertEquals(statusLineOKResponse, response.getStatusLine());
    }

    @Test
    public void buildResponseForPUTRequest() {
        String content = "data=fatcat";
        testHelpers.createFileAtResource(rootFolder, "/form", content);
        HTTPRequest request = createRequest(PUT, FORM, "", HTTP_1_1);
        HTTPResponse response = routeProcessor.buildResponse(request);
        assertEquals(statusLineOKResponse, response.getStatusLine());
    }

    @Test
    public void optionRequestWithAllowedMethods() {
        String content = "data=fatcat";
        testHelpers.createFileAtResource(rootFolder, "/method_options", content);
        HTTPRequest request = createRequest(OPTIONS, OPTIONS_ONE, "", HTTP_1_1);
        HTTPResponse response = routeProcessor.buildResponse(request);
        assertEquals("HTTP/1.1 200 OK", response.getStatusLine());
        assertThat(response.getEntityHeaders().get(ALLOW), hasItem(PUT.method()));
        assertThat(response.getEntityHeaders().get(ALLOW), hasItem(POST.method()));
    }

    @Test
    public void methodNotAllowedWithAllowedMethods() {
        String content = "data=fatcat";
        testHelpers.createFileAtResource(rootFolder, "/file1", content);
        HTTPRequest request = createRequest(PUT, FILE1, "", HTTP_1_1);
        HTTPResponse response = routeProcessor.buildResponse(request);
        String methodNotFoundResponse = "HTTP/1.1 405 Method Not Allowed";
        assertEquals(methodNotFoundResponse, response.getStatusLine());
        assertThat(response.getEntityHeaders().get(ALLOW), hasItem(POST.method()));
    }

    @Test
    public void resourceManagementExceptionCaught() {
        RouteProcessor routeProcessor = new RouteProcessor(
                new RoutesFactoryFake(routeActionsForExceptionTest()),
                configuration,
                new ServerErrorHandler());
        HTTPRequest request = new HTTPRequest(HEAD, INDEX, HTTP_1_1, null, null, null);
        HTTPResponse response = routeProcessor.buildResponse(request);
        assertEquals(SERVER_ERROR, response.getStatusCode());
    }

    private HTTPRequest createRequest(HTTPMethod method, HTTPResource uri, String queryParams, HTTPVersion version) {
        return new HTTPRequest().addRequestLine(createRequestLine(method, uri, queryParams, version));
    }

    private Map<String, String> createRequestLine(HTTPMethod method, HTTPResource uri, String queryParams, HTTPVersion version) {
        Map<String, String> requestLine = new HashMap<>();
        requestLine.put("method", method.method());
        requestLine.put("uri", uri.uri());
        requestLine.put("version", version.version());
        requestLine.put("queryParameters", queryParams);
        return requestLine;
    }

    public Map<HTTPMethod, List<RouteAction>> routeActionsForExceptionTest() {
        Map<HTTPMethod, List<RouteAction>> routeActions = new HashMap<>();
        routeActions.put(HEAD, asList(new ThrowExceptionActionFake()));
        routeActions.put(ERROR, asList(new InternalServerErrorAction()));
        return routeActions;
    }

    public Map<Route, List<RouteAction>> routeActions() {
        Map<Route, List<RouteAction>> routeActions = new HashMap<>();
        routeActions.put(new Route(HEAD, INDEX, HTTP_1_1), asList(new HEADAction()));
        routeActions.put(new Route(GET, INDEX, HTTP_1_1), asList(new HEADAction()));
        routeActions.put(new Route(PUT, FORM, HTTP_1_1), asList(new HEADAction()));
        routeActions.put(new Route(POST, FORM, HTTP_1_1), asList(new HEADAction()));
        routeActions.put(new Route(OPTIONS, OPTIONS_ONE, HTTP_1_1), asList(new HEADAction()));
        routeActions.put(new Route(OPTIONS, OPTIONS_TWO, HTTP_1_1), asList(new HEADAction()));
        routeActions.put(
                new Route(ERROR, UNRECOGNIZED, HTTPVersion.UNDEFINED),
                asList(new InternalServerErrorAction()));
        return routeActions;
    }

    private class ThrowExceptionActionFake implements RouteAction {
        @Override
        public boolean isAppropriate(HTTPRequest request) {
            return true;
        }

        @Override
        public HTTPResponse generateResponse(HTTPRequest request) {
            throw new ResourceManagementException("Fake Exception");
        }
    }
}
