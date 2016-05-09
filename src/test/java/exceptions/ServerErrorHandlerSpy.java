package exceptions;

import request.HTTPRequest;

public class ServerErrorHandlerSpy extends ServerErrorHandler {
    private boolean hasInvalidRequestBeenBuilt = false;

    @Override
    public HTTPRequest buildInvalidRequest(String s) {
        hasInvalidRequestBeenBuilt = true;
        return new HTTPRequest();
    }

    public boolean hasInvalidRequestBeenBuilt() {
        return hasInvalidRequestBeenBuilt;
    }
}
