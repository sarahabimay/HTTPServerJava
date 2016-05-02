public class HttpResponseSpy extends HTTPResponse {
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
