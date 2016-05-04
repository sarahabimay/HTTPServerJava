package response;

import request.HTTPMethod;
import request.HTTPVersion;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

public class HTTPResponse {
    private final String COLON = ": ";
    private final String COMMA = ",";
    private String statusLine;
    private String body;
    private Map<EntityHeaderFields, List<HTTPMethod>> entityHeaders;

    public void setStatusLine(HTTPVersion version, HTTPStatusCode statusCode) {
        this.statusLine = createStatusLine(version, statusCode);
    }

    public String getStatusLine() {
        return statusLine;
    }

    public void setEntityHeaders(Map<EntityHeaderFields, List<HTTPMethod>> headers) {
        this.entityHeaders = headers;
    }

    public Map<EntityHeaderFields, List<HTTPMethod>> getEntityHeaders() {
        return entityHeaders;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public byte[] buildResponse() {
        return addFormattedResponse(allocateByteBuffer()).array();
    }

    private String createStatusLine(HTTPVersion version, HTTPStatusCode statusCode) {
        return String.format("%s %d %s", version.version(), statusCode.statusCode(), statusCode.reason());
    }

    private ByteBuffer allocateByteBuffer() {
        return ByteBuffer.allocate(responseMessageLength());
    }

    private ByteBuffer addFormattedResponse(ByteBuffer byteBuffer) {
        byteBuffer.put(statusLine.getBytes());
        byteBuffer.put(getBytes(formattedEntityHeader()));
        byteBuffer.put(getBytes(formattedBody()));
        return byteBuffer;
    }

    private int responseMessageLength() {
        return statusLine.getBytes().length + lengthOfEntityHeaders() + lengthOfBody();
    }

    private int lengthOfEntityHeaders() {
        return entityHeaders == null ? 0 : formattedEntityHeader().getBytes().length;
    }

    private int lengthOfBody() {
        return body == null ? 0 : formattedBody().getBytes().length;
    }

    private String formattedEntityHeader() {
        return entityHeaders == null ? new String() : formatEachEntityHeader();
    }

    private String formatEachEntityHeader() {
        StringBuilder entityHeader = new StringBuilder();
        for (Map.Entry<EntityHeaderFields, List<HTTPMethod>> entry : entityHeaders.entrySet()) {
            entityHeader = addFormattedField(entry, entityHeader);
        }
        return entityHeader.append(crLF()).toString();
    }

    private StringBuilder addFormattedField(Map.Entry<EntityHeaderFields, List<HTTPMethod>> entry, StringBuilder entityHeader) {
        return entityHeader
                .append(crLF())
                .append(entry.getKey().field())
                .append(COLON)
                .append(join(entry.getValue(), COMMA));
    }

    private String join(List<HTTPMethod> methods, String characters) {
        String combined = methods.stream().map(HTTPMethod::toString).reduce("", (acc, method) -> acc + method + characters);
        combined = combined.substring(0, combined.length() - characters.length());
        return combined;
    }

    private String formattedBody() {
        return body == null ? body : new StringBuilder().append(doubleCRLF()).append(body).toString();
    }

    private byte[] getBytes(String line) {
        return line == null ? new byte[0] : line.getBytes();
    }

    private String doubleCRLF(){
        return crLF() + crLF();
    }

    private String crLF() {
        return "\r\n";
    }
}
