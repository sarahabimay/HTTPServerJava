package router;

import request.HTTPMethod;
import request.HTTPRequest;
import request.HTTPResource;
import routeActions.RouteAction;

import java.util.List;

public class RouterFake extends Router {
    private List<HTTPMethod> fakeResourceMethods;

    public RouterFake() {
        super(null);
    }

    @Override
    public RouteAction findRouteActions(HTTPRequest request) {
        return null;
    }

    @Override
    public List<HTTPMethod> allowedMethods(HTTPResource resource) {
        return fakeResourceMethods;
    }

    public void setFakeResourceMethods(List<HTTPMethod> resourceMethods) {
        fakeResourceMethods = resourceMethods;
    }
}
