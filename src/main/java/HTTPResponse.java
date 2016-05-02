import java.nio.ByteBuffer;

public class HTTPResponse {

    private String statusLine;
    private String body;

    public byte[] buildResponse() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(
                        statusLine.getBytes().length +
                        newLine().getBytes().length +
                        newLine().getBytes().length +
                        body().length);
        byteBuffer.put(statusLine.getBytes());
        byteBuffer.put(newLine().getBytes());
        byteBuffer.put(newLine().getBytes());
        byteBuffer.put(body());
        return byteBuffer.array();
    }

    public void setStatusLine(HTTPVersion version, HTTPStatusCode statusCode) {
        this.statusLine = createStatusLine(version, statusCode);
    }

    public String getStatusLine() {
        return statusLine;
    }

    public void setBody(String body) {
        this.body = body;
    }

    private String createStatusLine(HTTPVersion version, HTTPStatusCode statusCode) {
        return String.format("%s %d %s", version.version(), statusCode.statusCode(), statusCode.reason());
    }

    private byte[] body() {
        return body == null ? new byte[0] : body.getBytes();
    }

    private String newLine() {
        return "/n";
    }
}
