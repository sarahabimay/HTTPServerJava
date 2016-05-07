package routeActions;

import request.HTTPRequest;
import response.EntityHeaderFields;
import response.HTTPResponse;
import response.ResponseHTTPMessageFormatter;
import router.Router;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;
import static response.EntityHeaderFields.CONTENT_RANGE;
import static response.EntityHeaderFields.RANGE;
import static response.HTTPStatusCode.PARTIAL_CONTENT;

public class PartialContentAction implements RouteAction {
    @Override
    public boolean isAppropriate(HTTPRequest request) {
        return getPartialContentRequest(request);
    }

    @Override
    public HTTPResponse generateResponse(HTTPRequest request, Router router, URIProcessor uriProcessor) {
        byte[] payload = uriProcessor.read(request.uri().uri());
        return new HTTPResponse(new ResponseHTTPMessageFormatter())
                .setStatusLine(request.version(), PARTIAL_CONTENT)
                .setBody(getPartialContent(payload, getStartAndEndIndexes(payload, request)));
    }

    private boolean getPartialContentRequest(HTTPRequest request) {
        return request.headers() != null && request.headers().containsKey(RANGE);
    }

    private String[] parseByteRange(HTTPRequest request) {
        String[] range = request.headers().get(RANGE).split("=");
        return range[1].split("-");
    }

    private Integer[] getStartAndEndIndexes(byte[] entirePayload, HTTPRequest request) {
        String[] startAndEndByteRange = parseByteRange(request);
        Integer[] startAndEndPositions;
        if (startAndEndByteProvided(startAndEndByteRange)) {
            int startIndex = convertToInt(startAndEndByteRange[0]);
            int endIndex = oneIndexed(convertToInt(startAndEndByteRange[1]));
            startAndEndPositions = createStartAndEndRange(startIndex, endIndex);
        } else if (reversedStartByteProvided(startAndEndByteRange)) {
            startAndEndPositions = createStartAndEndRange(reverseStartPosition(entirePayload, startAndEndByteRange[1]), entirePayload.length);
        } else if (startByteOnlyProvided(startAndEndByteRange)) {
            startAndEndPositions = createStartAndEndRange(convertToInt(startAndEndByteRange[0]), entirePayload.length);
        } else {
            startAndEndPositions = createStartAndEndRange(0, 0);
        }
        return startAndEndPositions;
    }

    private Integer[] createStartAndEndRange(int startIndex, int endIndex) {
        return new Integer[]{startIndex, endIndex};
    }

    private byte[] getPartialContent(byte[] payload, Integer[] startAndEndIndexes) {
        return Arrays.copyOfRange(payload, startAndEndIndexes[0], endPosition(payload.length, startAndEndIndexes[1]));
    }

    private int oneIndexed(int zeroIndexPosition) {
        return zeroIndexPosition + 1;
    }

    private int reverseStartPosition(byte[] payload, String startPosition) {
        return payload.length - convertToInt(startPosition);
    }

    private boolean startAndEndByteProvided(String[] startAndEndBytes) {
        return startAndEndBytes.length == 2 && !startAndEndBytes[0].equals("");
    }

    private boolean reversedStartByteProvided(String[] startAndEndBytes) {
        return startAndEndBytes.length == 2 && startAndEndBytes[0].equals("");
    }

    private boolean startByteOnlyProvided(String[] startAndEndBytes) {
        return startAndEndBytes.length == 1 && convertToInt(startAndEndBytes[0]) != 0;
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
