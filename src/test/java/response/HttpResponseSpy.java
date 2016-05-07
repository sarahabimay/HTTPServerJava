package response;

import request.HTTPVersion;

public class HttpResponseSpy extends HTTPResponse {
    public HttpResponseSpy() {
        super(null);
    }

    @Override
    public byte[] buildResponse() {
        return new StringBuilder()
                .append(HTTPVersion.HTTP_1_1)
                .append(" ")
                .append(HTTPStatusCode.OK.statusCode())
                .append(" ")
                .append(HTTPStatusCode.OK.reason())
                .toString()
                .getBytes();
    }
}
