package routeActions;

import request.HTTPRequest;
import response.EntityHeaderFields;
import response.HTTPResponse;
import router.Router;

import java.util.*;

import static response.EntityHeaderFields.CONTENT_RANGE;
import static response.EntityHeaderFields.RANGE;
import static response.HTTPStatusCode.OK;
import static response.HTTPStatusCode.PARTIAL_CONTENT;

public class GETResourceAction implements RouteAction {
    @Override
    public HTTPResponse generateResponse(HTTPRequest request, Router router, URIProcessor uriProcessor) {
        HTTPResponse response = new HTTPResponse();
        setHeadersAndBody(response, request, uriProcessor);
        return response;
    }

    private HTTPResponse setHeadersAndBody(HTTPResponse response, HTTPRequest request, URIProcessor uriProcessor) {
        byte[] payload = uriProcessor.read(request.uri().uri());
        if (getPartialContentRequest(request)) {
            response.setStatusLine(request.version(), PARTIAL_CONTENT);
            Integer[] startAndEndIndexes = getStartAndEndIndexes(payload, request);
            response.setBody(getPartialContent(payload, startAndEndIndexes));
            return response;
        }
        else {
            response.setStatusLine(request.version(), OK);
            response.setBody(payload);
            return response;
        }
    }

    private byte[] getPartialContent(byte[] payload, Integer[] startAndEndIndexes) {
        return Arrays.copyOfRange(payload, startAndEndIndexes[0], endPosition(payload.length, startAndEndIndexes[1]));
    }

    private String[] parseByteRange(HTTPRequest request) {
        String[] range = request.headers().get(RANGE).split("=");
        return range[1].split("-");
    }

    private Integer[] getStartAndEndIndexes(byte[] entirePayload, HTTPRequest request) {
        String[] startAndEndByteRange = parseByteRange(request);
        Integer[] startAndEndPositions;
        if (startAndEndByteProvided(startAndEndByteRange)) {
            startAndEndPositions = new Integer[]{convertToInt(startAndEndByteRange[0]), oneIndexed(convertToInt(startAndEndByteRange[1]))};
        } else if (reversedStartByteProvided(startAndEndByteRange)) {
            startAndEndPositions = new Integer[]{reverseStartPosition(entirePayload, startAndEndByteRange[1]), entirePayload.length};
        } else if (startByteOnlyProvided(startAndEndByteRange)) {
            startAndEndPositions = new Integer[]{convertToInt(startAndEndByteRange[0]), entirePayload.length};
        } else {
            startAndEndPositions = new Integer[]{0, 0};
        }
        return startAndEndPositions;
    }

    private byte[] extractPartialContent(byte[] entirePayload, String[] startAndEndBytes) {
        Integer[] startAndEndPositions;
        if (startAndEndByteProvided(startAndEndBytes)) {
            startAndEndPositions = new Integer[]{convertToInt(startAndEndBytes[0]), oneIndexed(convertToInt(startAndEndBytes[1]))};
        } else if (reversedStartByteProvided(startAndEndBytes)) {
            startAndEndPositions = new Integer[]{reverseStartPosition(entirePayload, startAndEndBytes[1]), entirePayload.length};
        } else if (startByteOnlyProvided(startAndEndBytes)) {
            startAndEndPositions = new Integer[]{convertToInt(startAndEndBytes[0]), entirePayload.length};
        } else {
            startAndEndPositions = new Integer[]{0, 0};
        }
        return Arrays.copyOfRange(entirePayload, startAndEndPositions[0], endPosition(entirePayload.length, startAndEndPositions[1]));
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

    private boolean getPartialContentRequest(HTTPRequest request) {
        return request.headers() != null && request.headers().containsKey(RANGE);
    }
}
