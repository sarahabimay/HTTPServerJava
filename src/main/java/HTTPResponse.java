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

    public void setStatusLine(String version, int statusCode, String reason) {
        this.statusLine = createStatusLine(version, statusCode, reason);
    }

    public String getStatusLine() {
        return statusLine;
    }

    public void setBody(String body) {
        this.body = body;
    }

    private String createStatusLine(String version, int statusCode, String reason) {
        return String.format("%s %d %s", version, statusCode, reason);
    }

    private byte[] body() {
        return body == null ? new byte[0] : body.getBytes();
    }

    private String newLine() {
        return "/n";
    }
}
