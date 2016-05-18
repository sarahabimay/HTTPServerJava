package routeActions;

import messages.EntityHeaderBuilder;
import messages.EntityHeaderFields;
import request.HTTPMethod;
import request.HTTPRequest;
import request.HTTPResource;
import response.HTTPResponse;
import response.ResponseHTTPMessageFormatter;

import java.util.List;
import java.util.Map;

import static request.HTTPMethod.OPTIONS;
import static response.HTTPStatusCode.OK;

public class AllowOptionsAction implements RouteAction {
    private final EntityHeaderBuilder headerBuilder;

    public AllowOptionsAction(EntityHeaderBuilder entityHeaderBuilder) {
        this.headerBuilder = entityHeaderBuilder;
    }

    @Override
    public boolean isAppropriate(HTTPRequest request) {
        return request.method() == OPTIONS;
    }

    @Override
    public HTTPResponse generateResponse(HTTPRequest request) {
        return new HTTPResponse(new ResponseHTTPMessageFormatter())
                .setStatusLine(request.version(), OK)
                .setEntityHeaders(allowHeader(request.method(), request.uri()));
    }

    private Map<EntityHeaderFields, List<String>> allowHeader(HTTPMethod method, HTTPResource resource) {
        return headerBuilder.createALLOWHeader(method, resource).buildAllHeaders();
    }
}
