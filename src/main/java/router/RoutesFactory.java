package router;

import configuration.Configuration;
import request.HTTPMethod;
import routeActions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static request.HTTPMethod.*;

public class RoutesFactory {
    private final Map<HTTPMethod, List<RouteAction>> routeActions;

    public RoutesFactory(URIProcessor uriProcessor, Configuration configuration) {
        this.routeActions = initializeRouteActions(uriProcessor, configuration );
    }

    private Map<HTTPMethod, List<RouteAction>> initializeRouteActions(URIProcessor uriProcessor, Configuration configuration) {
        Map<HTTPMethod, List<RouteAction>> routeActions = new HashMap<>();
        routeActions.put(HEAD, asList(
                new MethodNotAllowedAction(configuration),
                new AuthenticateAction(),
                new StatusNOKAction(uriProcessor),
                new HEADAction()));

        routeActions.put(GET, asList(
                new MethodNotAllowedAction(configuration),
                new AuthenticateAction(),
                new IAmATeapotAction(),
                new ParameterDecodeAction(),
                new RedirectPathAction(),
                new PartialContentAction(uriProcessor),
                new DirectoryContentsAction(uriProcessor),
                new GETResourceAction(uriProcessor),
                new StatusNOKAction(uriProcessor)));

        routeActions.put(PUT, asList(
                new MethodNotAllowedAction(configuration),
                new AuthenticateAction(),
                new UpdateResourceAction(uriProcessor),
                new StatusNOKAction(uriProcessor)));

        routeActions.put(POST, asList(
                new MethodNotAllowedAction(configuration),
                new AuthenticateAction(),
                new UpdateResourceAction(uriProcessor),
                new StatusNOKAction(uriProcessor)));

        routeActions.put(DELETE, asList(
                new MethodNotAllowedAction(configuration),
                new DeleteResourceAction(uriProcessor),
                new StatusNOKAction(uriProcessor)));

        routeActions.put(OPTIONS, asList(
                new MethodNotAllowedAction(configuration),
                new AllowOptionsAction(configuration),
                new StatusNOKAction(uriProcessor)));

        routeActions.put(PATCH, asList(
                new MethodNotAllowedAction(configuration),
                new PatchContentAction(uriProcessor),
                new StatusNOKAction(uriProcessor)));

        routeActions.put(UNDEFINED, asList(
                new MethodNotAllowedAction(configuration),
                new StatusNOKAction(uriProcessor)));

        routeActions.put(ERROR, asList(
                new InternalServerErrorAction()));
        return routeActions;
    }

    public Map<HTTPMethod, List<RouteAction>> routeActions() {
        return routeActions;
    }
}