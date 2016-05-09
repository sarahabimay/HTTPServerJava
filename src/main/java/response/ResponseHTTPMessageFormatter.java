package response;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

public class ResponseHTTPMessageFormatter {
    private static final String COLON = ": ";
    private static final String COMMA = ",";

    public byte[] buildRawHTTPResponse(HTTPResponse response) {
        return addFormattedResponse(response, allocateByteBuffer(response)).array();
    }

    private ByteBuffer addFormattedResponse(HTTPResponse response, ByteBuffer byteBuffer) {
        byteBuffer.put(response.getStatusLine().getBytes());
        byteBuffer.put(getBytes(formattedEntityHeader(response)));
        addBodyToByteResponse(response, byteBuffer);
        return byteBuffer;
    }

    private ByteBuffer allocateByteBuffer(HTTPResponse response) {
        return ByteBuffer.allocate(responseMessageLength(response));
    }

    private int responseMessageLength(HTTPResponse response) {
        return response.getStatusLine().getBytes().length + lengthOfEntityHeaders(response) + lengthOfBody(response);
    }

    private int lengthOfEntityHeaders(HTTPResponse response) {
        return response.getEntityHeaders() == null ? 0 : formattedEntityHeader(response).getBytes().length;
    }

    private int lengthOfBody(HTTPResponse response) {
        return response.getBody() == null ? 0 : doubleCRLF().getBytes().length + response.getBody().length;
    }

    private String formattedEntityHeader(HTTPResponse response) {
        return response.getEntityHeaders() == null ? "" : formatEachEntityHeader(response);
    }

    private String formatEachEntityHeader(HTTPResponse response) {
        StringBuilder entityHeader = new StringBuilder();
        for (Map.Entry<EntityHeaderFields, List<String>> entry : response.getEntityHeaders().entrySet()) {
            entityHeader = addFormattedField(entry, entityHeader);
        }
        return entityHeader.toString();
    }

    private StringBuilder addFormattedField(Map.Entry<EntityHeaderFields, List<String>> entry, StringBuilder entityHeader) {
        return entityHeader
                .append(crLF())
                .append(entry.getKey().field())
                .append(COLON)
                .append(join(entry.getValue(), COMMA));
    }

    private void addBodyToByteResponse(HTTPResponse response, ByteBuffer byteBuffer) {
        if (response.getBody() != null) {
            byteBuffer.put(doubleCRLF().getBytes());
            byteBuffer.put(response.getBody());
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
