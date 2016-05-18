package routeActions;

import configuration.Configuration;
import messages.EntityHeaderFields;
import request.HTTPRequest;
import response.HTTPResponse;
import response.ResponseHTTPMessageFormatter;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static messages.EntityHeaderFields.AUTHENTICATE;
import static messages.EntityHeaderFields.AUTHORIZATION;
import static request.HTTPVersion.HTTP_1_1;
import static response.HTTPStatusCode.OK;
import static response.HTTPStatusCode.UNAUTHORIZED;

public class AuthenticateAction implements RouteAction {
    private final Configuration configuration;

    public AuthenticateAction(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public boolean isAppropriate(HTTPRequest request) {
        return configuration.authorizedResources().contains(request.uri());
    }

    @Override
    public HTTPResponse generateResponse(HTTPRequest request) {
        return isAuthorized(request) ? createProtectionSpaceResponse() : createUnauthorizedResponse();
    }

    private HTTPResponse createUnauthorizedResponse() {
        return new HTTPResponse(new ResponseHTTPMessageFormatter())
                .setStatusLine(HTTP_1_1, UNAUTHORIZED)
                .setEntityHeaders(unauthorizedHeaders());
    }

    private HTTPResponse createProtectionSpaceResponse() {
        return new HTTPResponse(new ResponseHTTPMessageFormatter())
                .setStatusLine(HTTP_1_1, OK)
                .setBody(documentProtectionSpace().getBytes());
    }

    private boolean isAuthorized(HTTPRequest request) {
        if (request.headers().containsKey(AUTHORIZATION)) {
            return parseAuthorization(request.headers().get(AUTHORIZATION)).equals(configuration.authorisationCredentials());
        }
        return false;
    }

    private String parseAuthorization(String authorizationValue) {
        String encodedCredentials = getEncodedCredentials(authorizationValue);
        return new String(Base64.getDecoder().decode(encodedCredentials.trim()));
    }

    private String documentProtectionSpace() {
        return String.format("%s\n%s\n%s\n", "GET /log HTTP/1.1", "PUT /these HTTP/1.1", "HEAD /requests HTTP/1.1");
    }

    private Map<EntityHeaderFields, List<String>> unauthorizedHeaders() {
        Map<EntityHeaderFields, List<String>> headers = new HashMap<>();
        headers.put(AUTHENTICATE, asList("Basic realm='WallyWorld'"));
        return headers;
    }

    private String getEncodedCredentials(String authorizationValue) {
        return authorizationValue.split(" ")[1];
    }
}
