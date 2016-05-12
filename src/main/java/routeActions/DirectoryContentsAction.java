package routeActions;

import request.HTTPRequest;
import request.HTTPResource;
import response.EntityHeaderFields;
import response.HTTPResponse;
import response.ResponseHTTPMessageFormatter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static request.HTTPMethod.GET;
import static request.HTTPResource.INDEX;
import static response.EntityHeaderFields.*;
import static response.HTTPStatusCode.OK;

public class DirectoryContentsAction implements RouteAction {
    private final URIProcessor uriProcessor;
    private final HTTPResource contentsResource;

    public DirectoryContentsAction(URIProcessor uriProcessor) {
        this.uriProcessor = uriProcessor;
        this.contentsResource = INDEX;
    }

    @Override
    public boolean isAppropriate(HTTPRequest request) {
        return request.method() == GET && contentsResource == request.uri();
    }

    @Override
    public HTTPResponse generateResponse(HTTPRequest request) {
        return createDirectoryLinksResponse(request, formatHTMLLinks(uriProcessor.directoryContents()));
    }

    private HTTPResponse createDirectoryLinksResponse(HTTPRequest request, byte[] directoryContents) {
        return new HTTPResponse(new ResponseHTTPMessageFormatter())
                .setStatusLine(request.version(), OK)
                .setEntityHeaders(contentHeaders(directoryContents))
                .setBody(directoryContents);
    }

    private Map<EntityHeaderFields, List<String>> contentHeaders(byte[] payload) {
        Map<EntityHeaderFields, List<String>> headers = new HashMap<>();
        headers.put(CONTENT_LENGTH, asList(Integer.toString(payload.length)));
        headers.put(CONTENT_TYPE, asList(CONTENT_TYPE_HTML.field()));
        return headers;
    }

    private byte[] formatHTMLLinks(List<String> directoryContents) {
        return createHTMLLinks(directoryContents).getBytes();
    }

    private String createHTMLLinks(List<String> directoryContents) {
       return directoryContents.stream().reduce("", (acc, fileName) -> acc + anchorTag(fileName));
    }

    private String anchorTag(String fileName) {
        return String.format("<a href='/%s'>%s</a><br>", fileName, fileName);
    }
}
