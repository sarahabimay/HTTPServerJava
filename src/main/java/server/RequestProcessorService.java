package server;

import request.RequestParser;
import router.RouteProcessor;

public class RequestProcessorService {
    private final RouteProcessor routeProcessor;
    private final RequestParser requestParser;
    private HttpClientSocket clientSocket;

    public RequestProcessorService(HttpClientSocket clientSocket, RequestParser requestParser, RouteProcessor routeProcessor) {
        this.clientSocket = clientSocket;
        this.requestParser = requestParser;
        this.routeProcessor = routeProcessor;
    }

    public void process() {
        clientSocket.sendResponse(routeProcessor.buildResponse(requestParser.parseRequest(clientSocket)));
        clientSocket.close();
    }
}
