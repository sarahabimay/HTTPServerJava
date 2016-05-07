package response;

import request.HTTPVersion;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

public class HTTPResponse {
    private final String COLON = ": ";
    private final String COMMA = ",";
    private Map<EntityHeaderFields, List<String>> entityHeaders;
    private byte[] body;
    private HTTPStatusCode statusCode;
    private HTTPVersion version;

    public HTTPResponse() {
        this.statusCode = HTTPStatusCode.NOT_FOUND;
        this.version = HTTPVersion.UNDEFINED;
        this.entityHeaders = null;
    }

    public HTTPResponse(HTTPVersion version, HTTPStatusCode statusCode, Map<EntityHeaderFields, List<String>> entityHeaders, byte[] body) {
        this.statusCode = statusCode;
        this.version = version;
        this.entityHeaders = entityHeaders;
        this.body = body;
    }

    public HTTPResponse setStatusLine(HTTPVersion version, HTTPStatusCode statusCode) {
        return new HTTPResponse(version, statusCode, entityHeaders, body);
    }

    public HTTPStatusCode getStatusCode() {
        return statusCode;
    }

    public String getStatusLine() {
        return createStatusLine(version, statusCode);
    }

    public HTTPResponse setEntityHeaders(Map<EntityHeaderFields, List<String>> headers) {
        return new HTTPResponse(version, statusCode, headers, body);
    }

    public Map<EntityHeaderFields, List<String>> getEntityHeaders() {
        return entityHeaders;
    }

    public HTTPResponse setBody(byte[] body) {
        return new HTTPResponse(version, statusCode, entityHeaders, body);
    }

    public String getBody() {
        return new String(body);
    }

    public byte[] buildResponse() {
        return addFormattedResponse(allocateByteBuffer()).array();
    }

    private ByteBuffer addFormattedResponse(ByteBuffer byteBuffer) {
        byteBuffer.put(getStatusLine().getBytes());
        byteBuffer.put(getBytes(formattedEntityHeader()));
        addBodyToByteResponse(byteBuffer);
        return byteBuffer;
    }

    private String createStatusLine(HTTPVersion version, HTTPStatusCode statusCode) {
        return String.format("%s %d %s", version.version(), statusCode.statusCode(), statusCode.reason());
    }

    private ByteBuffer allocateByteBuffer() {
        return ByteBuffer.allocate(responseMessageLength());
    }

    private int responseMessageLength() {
        return getStatusLine().getBytes().length + lengthOfEntityHeaders() + lengthOfBody();
    }

    private int lengthOfEntityHeaders() {
        return entityHeaders == null ? 0 : formattedEntityHeader().getBytes().length;
    }

    private int lengthOfBody() {
        return body == null ? 0 : doubleCRLF().getBytes().length + body.length;
    }

    private String formattedEntityHeader() {
        return entityHeaders == null ? new String() : formatEachEntityHeader();
    }

    private String formatEachEntityHeader() {
        StringBuilder entityHeader = new StringBuilder();
        for (Map.Entry<EntityHeaderFields, List<String>> entry : entityHeaders.entrySet()) {
            entityHeader = addFormattedField(entry, entityHeader);
        }
        return entityHeader.append(crLF()).toString();
    }

    private StringBuilder addFormattedField(Map.Entry<EntityHeaderFields, List<String>> entry, StringBuilder entityHeader) {
        return entityHeader
                .append(crLF())
                .append(entry.getKey().field())
                .append(COLON)
                .append(join(entry.getValue(), COMMA));
    }

    private void addBodyToByteResponse(ByteBuffer byteBuffer) {
        if (body != null) {
            byteBuffer.put(doubleCRLF().getBytes());
            byteBuffer.put(body);
        }
    }

    private String join(List<String> methods, String characters) {
        String combined = methods.stream().map(String::toString).reduce("", (acc, method) -> acc + method + characters);
        combined = combined.substring(0, combined.length() - characters.length());
        return combined;
    }

    private byte[] getBytes(String line) {
        return line == null ? new byte[0] : line.getBytes();
    }

    private String doubleCRLF() {
        return crLF() + crLF();
    }

    private String crLF() {
        return "\r\n";
    }
}
