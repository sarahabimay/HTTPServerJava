package router;

import configuration.Configuration;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import request.HTTPMethod;
import request.HTTPRequest;
import routeActions.MethodNotAllowedAction;
import routeActions.RouteAction;
import routeActions.URIProcessor;
import testHelper.TestHelpers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static request.HTTPMethod.*;
import static request.HTTPResource.COFFEE;
import static request.HTTPResource.FILE1;
import static request.HTTPVersion.HTTP_1_1;
import static response.HTTPStatusCode.*;

public class RoutesFactoryTest {
    private File rootFolder;
    private TestHelpers testHelpers;
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() {
        testHelpers = new TestHelpers();

        try {
            rootFolder = temporaryFolder.newFolder("test");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void bogusMethodRequestReceivesMethodNotAllowed() {
        String content = "data=fatcat";
        testHelpers.createFileAtResource(rootFolder, "/file1", content);
        Map<HTTPMethod, List<RouteAction>> actions =
                new RoutesFactory(new URIProcessor(rootFolder.getAbsolutePath()), new Configuration()).routeActions();
        HTTPRequest request = new HTTPRequest(UNDEFINED, FILE1, HTTP_1_1, null, null, null);
        RouteAction action = selectRouteAction(actions.get(UNDEFINED), request);
        assertEquals(METHOD_NOT_ALLOWED, action.generateResponse(request).getStatusCode());
    }

    @Test
    public void errorMethodRequestReceivesInternalServerError() {
        String content = "data=fatcat";
        testHelpers.createFileAtResource(rootFolder, "/file1", content);
        Map<HTTPMethod, List<RouteAction>> actions =
                new RoutesFactory(new URIProcessor(rootFolder.getAbsolutePath()), new Configuration() ).routeActions();
        HTTPRequest request = new HTTPRequest(ERROR, FILE1, HTTP_1_1, null, null, null);
        RouteAction action = selectRouteAction(actions.get(ERROR), request);
        assertEquals(SERVER_ERROR, action.generateResponse(request).getStatusCode());
    }

    @Test
    public void getIAmATeapotAction() {
        Map<HTTPMethod, List<RouteAction>> actions =
                new RoutesFactory(new URIProcessor(rootFolder.getAbsolutePath()), new Configuration() ).routeActions();
        HTTPRequest request = new HTTPRequest(GET, COFFEE, HTTP_1_1, null, null, null);
        RouteAction action = selectRouteAction(actions.get(GET), request);
        assertEquals(FOUR_EIGHTEEN, action.generateResponse(request).getStatusCode());
    }

    @Test
    public void parameterDecodeAction() {
        Map<HTTPMethod, List<RouteAction>> actions =
                new RoutesFactory(new URIProcessor(rootFolder.getAbsolutePath()), new Configuration()).routeActions();
        HTTPRequest request = new HTTPRequest(GET, COFFEE, HTTP_1_1, null, null, null);
        RouteAction action = selectRouteAction(actions.get(GET), request);
        assertEquals(FOUR_EIGHTEEN, action.generateResponse(request).getStatusCode());
    }

    @Test
    public void putMethodnNotAllowed() {
        String content = "data=fatcat";
        testHelpers.createFileAtResource(rootFolder, "/file1", content);
        Map<HTTPMethod, List<RouteAction>> actions =
                new RoutesFactory(new URIProcessor(rootFolder.getAbsolutePath()), new Configuration()).routeActions();
        HTTPRequest request = new HTTPRequest(PUT, FILE1, HTTP_1_1, null, null, null);
        RouteAction action = selectRouteAction(actions.get(GET), request);
        assertEquals(METHOD_NOT_ALLOWED, action.generateResponse(request).getStatusCode());
    }

    private RouteAction selectRouteAction(List<RouteAction> routeActions, HTTPRequest request) {
        Optional<RouteAction> action = routeActions.stream().filter(routeAction -> routeAction.isAppropriate(request)).findFirst();
        return action.isPresent() ? action.get() : new MethodNotAllowedAction(new Configuration());
    }
}
