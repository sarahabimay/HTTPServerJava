import request.HTTPRequest;
import response.HTTPResponse;
import router.RouteProcessor;
import router.Router;

public class RouteProcessorSpy extends RouteProcessor {
    private boolean hasRequestBeenParsed = false;

    public RouteProcessorSpy(Router router) {
        super(router);
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
