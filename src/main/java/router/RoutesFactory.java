package router;

import configuration.Configuration;
import messages.EntityHeaderBuilder;
import request.HTTPMethod;
import routeActions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static request.HTTPMethod.*;

public class RoutesFactory {
    private final Map<HTTPMethod, List<RouteAction>> routeActions;
    private final EntityHeaderBuilder entityHeaderBuilder;

    public RoutesFactory(URIProcessor uriProcessor, Configuration configuration, EntityHeaderBuilder entityHeaderBuilder) {
        this.entityHeaderBuilder = entityHeaderBuilder;
        this.routeActions = initializeRouteActions(uriProcessor, configuration);
    }

    public Map<HTTPMethod, List<RouteAction>> routeActions() {
        return routeActions;
    }

    public EntityHeaderBuilder entityHeaderBuilder() {
        return entityHeaderBuilder;
    }

    private Map<HTTPMethod, List<RouteAction>> initializeRouteActions(URIProcessor uriProcessor, Configuration configuration) {
        Map<HTTPMethod, List<RouteAction>> routeActions = new HashMap<>();
        routeActions.put(HEAD, asList(
                new MethodNotAllowedAction(entityHeaderBuilder, configuration),
                new AuthenticateAction(configuration),
                new StatusNOKAction(uriProcessor),
                new HEADAction()));

        routeActions.put(GET, asList(
                new MethodNotAllowedAction(entityHeaderBuilder, configuration),
                new AuthenticateAction(configuration),
                new IAmATeapotAction(),
                new ParameterDecodeAction(),
                new RedirectPathAction(),
                new PartialContentAction(uriProcessor),
                new DirectoryContentsAction(uriProcessor),
                new GETResourceAction(uriProcessor),
                new StatusNOKAction(uriProcessor)));

        routeActions.put(PUT, asList(
                new MethodNotAllowedAction(entityHeaderBuilder, configuration),
                new AuthenticateAction(configuration),
                new UpdateResourceAction(uriProcessor),
                new StatusNOKAction(uriProcessor)));

        routeActions.put(POST, asList(
                new MethodNotAllowedAction(entityHeaderBuilder, configuration),
                new AuthenticateAction(configuration),
                new UpdateResourceAction(uriProcessor),
                new StatusNOKAction(uriProcessor)));

        routeActions.put(DELETE, asList(
                new MethodNotAllowedAction(entityHeaderBuilder, configuration),
                new DeleteResourceAction(uriProcessor),
                new StatusNOKAction(uriProcessor)));

        routeActions.put(OPTIONS, asList(
                new MethodNotAllowedAction(entityHeaderBuilder, configuration),
                new AllowOptionsAction(entityHeaderBuilder),
                new StatusNOKAction(uriProcessor)));

        routeActions.put(PATCH, asList(
                new MethodNotAllowedAction(entityHeaderBuilder, configuration),
                new PatchContentAction(uriProcessor),
                new StatusNOKAction(uriProcessor)));

        routeActions.put(UNDEFINED, asList(
                new MethodNotAllowedAction(entityHeaderBuilder, configuration),
                new StatusNOKAction(uriProcessor)));

        routeActions.put(ERROR, asList( new InternalServerErrorAction()));
        return routeActions;
    }
}