package response;

import request.HTTPVersion;

import java.util.List;
import java.util.Map;

public class HTTPResponse {
    private final String COLON = ": ";
    private final String COMMA = ",";
    private final ResponseHTTPMessageFormatter httpFormatter;
    private Map<EntityHeaderFields, List<String>> entityHeaders;
    private byte[] body;
    private HTTPStatusCode statusCode;
    private HTTPVersion version;

    public HTTPResponse(HTTPVersion version, HTTPStatusCode statusCode, Map<EntityHeaderFields, List<String>> entityHeaders, byte[] body, ResponseHTTPMessageFormatter httpFormatter) {
        this.statusCode = statusCode;
        this.version = version;
        this.entityHeaders = entityHeaders;
        this.body = body;
        this.httpFormatter = httpFormatter;
    }

    public HTTPResponse(ResponseHTTPMessageFormatter httpMessageFormatter) {
        this.statusCode = HTTPStatusCode.NOT_FOUND;
        this.version = HTTPVersion.UNDEFINED;
        this.entityHeaders = null;
        this.httpFormatter = httpMessageFormatter;
    }

    public HTTPResponse setStatusLine(HTTPVersion version, HTTPStatusCode statusCode) {
        return new HTTPResponse(version, statusCode, entityHeaders, body, httpFormatter);
    }

    public HTTPStatusCode getStatusCode() {
        return statusCode;
    }

    public String getStatusLine() {
        return createStatusLine(version, statusCode);
    }

    public HTTPResponse setEntityHeaders(Map<EntityHeaderFields, List<String>> headers) {
        return new HTTPResponse(version, statusCode, headers, body, httpFormatter);
    }

    public Map<EntityHeaderFields, List<String>> getEntityHeaders() {
        return entityHeaders;
    }

    public HTTPResponse setBody(byte[] body) {
        return new HTTPResponse(version, statusCode, entityHeaders, body, httpFormatter);
    }

    public byte[] getBody() {
        return body;
    }

    private String createStatusLine(HTTPVersion version, HTTPStatusCode statusCode) {
        return String.format("%s %d %s", version.version(), statusCode.statusCode(), statusCode.reason());
    }

    public byte[] buildResponse() {
        return httpFormatter.buildRawHTTPResponse(this);
    }
}
