package request;

import server.HttpClientSocket;

public class RequestParserSpy extends RequestParser {
    private HTTPRequest fakeHTTPRequest;
    private boolean hasRequestBeenParsed = false;

    public RequestParserSpy(HTTPRequest fakeHTTPRequest) {
        super(null);
        this.fakeHTTPRequest = fakeHTTPRequest;
    }

    @Override
    public HTTPRequest parseRequest(HttpClientSocket clientSocket) {
        hasRequestBeenParsed = true;
        clientSocket.getInputReader();
        return fakeHTTPRequest;
    }

    public boolean hasRequestBeenParsed() {
        return hasRequestBeenParsed;
    }
}
