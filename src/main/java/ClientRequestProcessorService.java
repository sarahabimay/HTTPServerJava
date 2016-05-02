import java.util.Optional;

public class ClientRequestProcessorService {
    private Router router;
    private RequestParser requestParser;
    private HttpClientSocket clientSocket;

    public ClientRequestProcessorService(HttpClientSocket clientSocket, RequestParser requestParser, Router router) {
        this.clientSocket = clientSocket;
        this.requestParser = requestParser;
        this.router = router;
    }

    public void process() {
        HTTPRequest request = requestParser.parseRequest(clientSocket);
        Optional<Route> route = router.findRoute(request);
        HTTPResponse response;
        if (route.isPresent()) {
            response = route.get().createStatusOKResponseNoBody(request);
        } else {
            response = new HTTPResponse();
            response.setStatusLine(HTTPVersion.HTTP_1_1, HTTPStatusCode.NOT_FOUND);
        }

        clientSocket.sendResponse(response);
        clientSocket.close();
    }
}
