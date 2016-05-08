package routeActions;

import request.HTTPRequest;
import response.HTTPResponse;
import response.ResponseHTTPMessageFormatter;
import router.Router;

import java.util.List;

import static request.HTTPMethod.GET;
import static response.HTTPStatusCode.OK;

public class DirectoryContentsAction implements RouteAction {
    @Override
    public boolean isAppropriate(HTTPRequest request) {
        return request.method() == GET;
    }

    @Override
    public HTTPResponse generateResponse(HTTPRequest request, Router router, URIProcessor uriProcessor) {
        return createDirectoryLinksResponse(request, uriProcessor);
    }

    private HTTPResponse createDirectoryLinksResponse(HTTPRequest request, URIProcessor uriProcessor) {
        return new HTTPResponse(new ResponseHTTPMessageFormatter())
                .setStatusLine(request.version(), OK)
                .setBody(formatHTMLLinks(uriProcessor));
    }

    private byte[] formatHTMLLinks(URIProcessor uriProcessor) {
        List<String> directoryContents = uriProcessor.links();
        return createHTMLLinks(directoryContents).getBytes();
    }

    private String createHTMLLinks(List<String> directoryContents) {
       return directoryContents.stream()
               .reduce("", (acc, fileName) -> acc + anchorTag(fileName));
    }
    private String anchorTag(String fileName) {
        return String.format("<a href='/%s'>%s</a><br>", fileName, fileName);
    }
}
