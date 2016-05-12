package routeActions;

import request.HTTPRequest;
import response.EntityHeaderFields;
import response.HTTPResponse;
import response.ResponseHTTPMessageFormatter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Arrays.copyOfRange;
import static response.EntityHeaderFields.*;
import static response.HTTPStatusCode.PARTIAL_CONTENT;

public class PartialContentAction implements RouteAction {
    private final String EQUAL = "=";
    private final URIProcessor uriProcessor;

    public PartialContentAction(URIProcessor uriProcessor) {
        this.uriProcessor = uriProcessor;
    }

    @Override
    public boolean isAppropriate(HTTPRequest request) {
        return isPartialContentRequest(request);
    }

    @Override
    public HTTPResponse generateResponse(HTTPRequest request) {
        byte[] currentPayload = payloadAtResource(request, uriProcessor);
        Integer[] startAndEndIndexes = startAndEndIndexes(currentPayload, request);
        byte[] partialContent = getPartialContent(currentPayload, startAndEndIndexes);
        return partialContentResponse(request, partialContent, startAndEndIndexes);
    }

    private boolean isPartialContentRequest(HTTPRequest request) {
        return request.headers() != null && request.headers().containsKey(RANGE);
    }

    private byte[] payloadAtResource(HTTPRequest request, URIProcessor uriProcessor) {
        return uriProcessor.read(request.uri().uri());
    }

    private Integer[] startAndEndIndexes(byte[] currentPayload, HTTPRequest request) {
        String[] startAndEndByteRange = parseByteRange(request);
        Integer[] startAndEndPositions;
        if (hasStartAndEndByteProvided(startAndEndByteRange)) {
            startAndEndPositions = startAndEndByteProvided(currentPayload, startAndEndByteRange);
        } else if (hasReversedStartByteProvided(startAndEndByteRange)) {
            startAndEndPositions = calculatedStartAndEndIndexes(currentPayload, startAndEndByteRange[1]);
        } else if (hasStartByteOnlyProvided(startAndEndByteRange)) {
            startAndEndPositions = providedStartAndCalculatedEndIndex(currentPayload, startAndEndByteRange[0]);
        } else {
            startAndEndPositions = createStartAndEndRange(0, 0);
        }
        return startAndEndPositions;
    }

    private String[] parseByteRange(HTTPRequest request) {
        String[] range = request.headers().get(RANGE).split(EQUAL);
        return range[1].split("-");
    }

    private boolean hasStartAndEndByteProvided(String[] startAndEndBytes) {
        return startAndEndBytes.length == 2 && !startAndEndBytes[0].equals("");
    }

    private boolean hasReversedStartByteProvided(String[] startAndEndBytes) {
        return startAndEndBytes.length == 2 && startAndEndBytes[0].equals("");
    }

    private boolean hasStartByteOnlyProvided(String[] startAndEndBytes) {
        return startAndEndBytes.length == 1 && convertToInt(startAndEndBytes[0]) != 0;
    }

    private Integer[] startAndEndByteProvided(byte[] currentPayload, String[] startAndEndByteRange) {
        Integer[] startAndEndPositions;
        int startIndex = convertToInt(startAndEndByteRange[0]);
        int endIndex = endPosition(payloadEndPosition(currentPayload), convertToInt(startAndEndByteRange[1]));
        startAndEndPositions = createStartAndEndRange(startIndex, endIndex);
        return startAndEndPositions;
    }

    private Integer[] providedStartAndCalculatedEndIndex(byte[] currentPayload, String value) {
        Integer[] startAndEndPositions;
        int startIndex = convertToInt(value);
        int endIndex = payloadEndPosition(currentPayload);
        startAndEndPositions = createStartAndEndRange(startIndex, endIndex);
        return startAndEndPositions;
    }

    private Integer[] calculatedStartAndEndIndexes(byte[] currentPayload, String startPosition) {
        Integer[] startAndEndPositions;
        int startIndex = reverseStartPosition(currentPayload, startPosition);
        int endIndex = payloadEndPosition(currentPayload);
        startAndEndPositions = createStartAndEndRange(startIndex, endIndex);
        return startAndEndPositions;
    }

    private HTTPResponse partialContentResponse(HTTPRequest request, byte[] partialContent, Integer[] startAndEndIndexes) {
        return new HTTPResponse(new ResponseHTTPMessageFormatter())
                .setStatusLine(request.version(), PARTIAL_CONTENT)
                .setEntityHeaders(contentHeaders(startAndEndIndexes, partialContent))
                .setBody(partialContent);
    }

    private Map<EntityHeaderFields, List<String>> contentHeaders(Integer[] startAndEndRange, byte[] partialPayload) {
        Map<EntityHeaderFields, List<String>> headers = new HashMap<>();
        headers.put(CONTENT_LENGTH, asList(String.valueOf(partialPayload.length)));
        headers.put(CONTENT_TYPE, asList(CONTENT_TYPE_PLAIN.field()));
        headers.put(CONTENT_RANGE, asList(formatContentRangeValue(startAndEndRange)));
        return headers;
    }

    private String formatContentRangeValue(Integer[] startAndEndRange) {
        return String.format("bytes %d-%d", startAndEndRange[0], startAndEndRange[1]);
    }

    private Integer[] createStartAndEndRange(int startIndex, int endIndex) {
        return new Integer[]{startIndex, endIndex};
    }

    private byte[] getPartialContent(byte[] payload, Integer[] startAndEndIndexes) {
        return copyOfRange(payload, startAndEndIndexes[0], oneIndexed(startAndEndIndexes[1]));
    }

    private int reverseStartPosition(byte[] payload, String startPosition) {
        return payload.length - convertToInt(startPosition);
    }

    private int convertToInt(String value) {
        try {
            return Integer.valueOf(value);
        } catch (Exception e) {
            return 0;
        }
    }

    private int payloadEndPosition(byte[] currentPayload) {
        return currentPayload.length -1;
    }

    private int endPosition(int payloadLength, int endIndex) {
        return (payloadLength - endIndex) <= 0 ? payloadLength : endIndex;
    }

    private int oneIndexed(int zeroIndexPosition) {
        return zeroIndexPosition + 1;
    }
}
