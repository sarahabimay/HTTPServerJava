package router;

import request.HTTPMethod;
import request.HTTPRequest;
import request.HTTPResource;
import routeActions.RouteAction;

import java.util.List;

public class RouterFake extends Router {
    private List<HTTPMethod> fakeResourceMethods;
    private List<RouteAction> fakeRouteActions;

    public RouterFake() {
        super(null);
    }

    @Override
    public List<RouteAction> findRouteActions(HTTPRequest request) {
        return fakeRouteActions;
    }

    @Override
    public List<HTTPMethod> allowedMethods(HTTPResource resource) {
        return fakeResourceMethods;
    }

    public void setFakeRouteActions(List<RouteAction> routeActions) {
        fakeRouteActions = routeActions;
    }

    public void setFakeResourceMethods(List<HTTPMethod> resourceMethods) {
        fakeResourceMethods = resourceMethods;
    }
}
