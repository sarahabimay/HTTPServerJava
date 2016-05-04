import request.HTTPRequest;
import response.HTTPResponse;
import routeActions.URIProcessor;
import router.RouteProcessor;
import router.Router;

public class RouteProcessorSpy extends RouteProcessor {
    private boolean hasRequestBeenParsed = false;

    public RouteProcessorSpy(Router router, URIProcessor uriProcessor) {
        super(router, uriProcessor);
    }

    @Override
    public HTTPResponse buildResponse(HTTPRequest request) {
        hasRequestBeenParsed = true;
        return null;
    }

    public boolean hasRequestBeenParsed() {
        return hasRequestBeenParsed;
    }
}
