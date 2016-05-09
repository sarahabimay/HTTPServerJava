package routeActions;

import request.HTTPRequest;
import response.EntityHeaderFields;
import response.HTTPResponse;
import response.ResponseHTTPMessageFormatter;
import router.Router;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.copyOfRange;
import static response.EntityHeaderFields.RANGE;
import static response.HTTPStatusCode.PARTIAL_CONTENT;

public class PartialContentAction implements RouteAction {
    @Override
    public boolean isAppropriate(HTTPRequest request) {
        return isPartialContentRequest(request);
    }

    @Override
    public HTTPResponse generateResponse(HTTPRequest request, Router router, URIProcessor uriProcessor) {
        return partialContentResponse(request, payloadAtResource(request, uriProcessor));
    }

    private boolean isPartialContentRequest(HTTPRequest request) {
        return request.headers() != null && request.headers().containsKey(RANGE);
    }

    private byte[] payloadAtResource(HTTPRequest request, URIProcessor uriProcessor) {
        return uriProcessor.read(request.uri().uri());
    }

    private HTTPResponse partialContentResponse(HTTPRequest request, byte[] currentPayload) {
        byte[] partialContent = getPartialContent(currentPayload, startAndEndIndexes(currentPayload, request));
        return new HTTPResponse(new ResponseHTTPMessageFormatter())
                .setStatusLine(request.version(), PARTIAL_CONTENT)
                .setBody(partialContent);
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
        String[] range = request.headers().get(RANGE).split("=");
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
        int endIndex = endPosition(currentPayload.length, oneIndexed(convertToInt(startAndEndByteRange[1])));
        startAndEndPositions = createStartAndEndRange(startIndex, endIndex);
        return startAndEndPositions;
    }

    private Integer[] providedStartAndCalculatedEndIndex(byte[] currentPayload, String value) {
        Integer[] startAndEndPositions;
        int startIndex = convertToInt(value);
        int endIndex = currentPayload.length;
        startAndEndPositions = createStartAndEndRange(startIndex, endIndex);
        return startAndEndPositions;
    }

    private Integer[] calculatedStartAndEndIndexes(byte[] currentPayload, String startPosition) {
        Integer[] startAndEndPositions;
        int startIndex = reverseStartPosition(currentPayload, startPosition);
        int endIndex = currentPayload.length;
        startAndEndPositions = createStartAndEndRange(startIndex, endIndex);
        return startAndEndPositions;
    }

    private Integer[] createStartAndEndRange(int startIndex, int endIndex) {
        return new Integer[]{startIndex, endIndex};
    }

    private byte[] getPartialContent(byte[] payload, Integer[] startAndEndIndexes) {
        return copyOfRange(payload, startAndEndIndexes[0], endPosition(payload.length, startAndEndIndexes[1]));
    }

    private int oneIndexed(int zeroIndexPosition) {
        return zeroIndexPosition + 1;
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

    private int endPosition(int payloadLength, int endPosition) {
        return (payloadLength - endPosition) <= 0 ? payloadLength : endPosition;
    }
}
