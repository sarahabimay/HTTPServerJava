public class HttpResponseSpy extends HTTPResponse {
    private static final String STATUS_OK_RESPONSE =
            "HTTP/1.1 " +
            HTTPStatusCode.OK.statusCode() + " " +
            HTTPStatusCode.OK.reason();

    @Override
    public byte[] buildResponse() {
        return STATUS_OK_RESPONSE.getBytes();
    }
}
