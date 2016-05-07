package router;

import routeActions.*;

import java.util.*;

import static java.util.Arrays.asList;
import static request.HTTPMethod.*;
import static request.HTTPResource.*;
import static request.HTTPVersion.HTTP_1_1;

public class RoutesFactory {
    public Map<Route, List<RouteAction>> routeActions() {
        Map<Route, List<RouteAction>> routeActions = new HashMap<>();
        routeActions.put(new Route(HEAD, INDEX, HTTP_1_1), asList(new StatusOKAction()));
        routeActions.put(new Route(GET, INDEX, HTTP_1_1), asList(new DirectoryContentsAction()));
        routeActions.put(new Route(GET, FORM, HTTP_1_1), asList(
                new PartialContentAction(),
                new GETResourceAction()));
        routeActions.put(new Route(PUT, FORM, HTTP_1_1), asList(new UpdateResourceAction()));
        routeActions.put(new Route(POST, FORM, HTTP_1_1), asList(new UpdateResourceAction()));
        routeActions.put(new Route(GET, FILE1, HTTP_1_1), asList(
                new PartialContentAction(),
                new GETResourceAction()));
        routeActions.put(new Route(GET, IMAGEJPEG, HTTP_1_1), asList(new GETResourceAction()));
        routeActions.put(new Route(GET, IMAGEPNG, HTTP_1_1), asList(new GETResourceAction()));
        routeActions.put(new Route(GET, IMAGEGIF, HTTP_1_1), asList(new GETResourceAction()));
        routeActions.put(new Route(GET, PARAMETERS, HTTP_1_1), asList(
                new ParameterDecodeAction(),
                new PartialContentAction(),
                new GETResourceAction()));
        routeActions.put(new Route(OPTIONS, OPTIONS_ONE, HTTP_1_1), asList(new AllowOptionsAction()));
        routeActions.put(new Route(GET, OPTIONS_ONE, HTTP_1_1), asList(
                new PartialContentAction(),
                new GETResourceAction()));
        routeActions.put(new Route(PUT, OPTIONS_ONE, HTTP_1_1), asList(new UpdateResourceAction()));
        routeActions.put(new Route(POST, OPTIONS_ONE, HTTP_1_1), asList(new UpdateResourceAction()));
        routeActions.put(new Route(HEAD, OPTIONS_ONE, HTTP_1_1), asList(new StatusOKAction()));
        routeActions.put(new Route(OPTIONS, OPTIONS_TWO, HTTP_1_1), asList(new AllowOptionsAction()));
        routeActions.put(new Route(GET, OPTIONS_TWO, HTTP_1_1), asList(
                new PartialContentAction(),
                new GETResourceAction()));
        routeActions.put(new Route(GET, TEXT_FILE, HTTP_1_1), asList(
                new PartialContentAction(),
                new GETResourceAction()));
        routeActions.put(new Route(GET, PARTIAL_CONTENT, HTTP_1_1), asList(
                new PartialContentAction(),
                new GETResourceAction()));
        routeActions.put(new Route(GET, PATCH_CONTENT, HTTP_1_1), asList(new GETResourceAction()));
        routeActions.put(new Route(PATCH, PATCH_CONTENT, HTTP_1_1), asList(new PatchContentAction()));
        routeActions.put(new Route(DELETE, FORM, HTTP_1_1), asList(new DeleteResourceAction()));
        return routeActions;
    }
}