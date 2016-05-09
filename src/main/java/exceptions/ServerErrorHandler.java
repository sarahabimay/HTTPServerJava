package exceptions;

import request.HTTPMethod;
import request.HTTPRequest;
import request.HTTPVersion;
import response.EntityHeaderFields;

import java.util.HashMap;
import java.util.Map;

import static request.HTTPResource.*;

public class ServerErrorHandler {
    public HTTPRequest buildInvalidRequest(String errorMessage) {
       return new HTTPRequest()
               .addRequestLine(requestLine())
               .addRequestHeader(emptyRequestHeaders())
               .addBody(errorMessage);
    }

    private Map<String, String> requestLine() {
        Map<String, String> requestLine = new HashMap<>();
        requestLine.put("method", HTTPMethod.UNDEFINED.method());
        requestLine.put("uri", UNRECOGNIZED.uri());
        requestLine.put("version", HTTPVersion.UNDEFINED.version());
        return requestLine;
    }

    private Map<EntityHeaderFields, String> emptyRequestHeaders() {
        return new HashMap<>();
    }
}
