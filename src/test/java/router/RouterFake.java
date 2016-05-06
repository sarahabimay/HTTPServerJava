package router;

import request.HTTPRequest;
import request.HTTPResource;
import routeActions.RouteAction;

import java.util.List;

public class RouterFake extends Router {
    private List<String> fakeResourceMethods;

    public RouterFake() {
        super(null);
    }

    @Override
    public RouteAction findRouteActions(HTTPRequest request) {
        return null;
    }

    @Override
    public List<String> allowedMethods(HTTPResource resource) {
        return fakeResourceMethods;
    }

    public void setFakeResourceMethods(List<String> resourceMethods) {
        fakeResourceMethods = resourceMethods;
    }
}
