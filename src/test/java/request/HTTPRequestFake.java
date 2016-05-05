package request;

import static request.HTTPMethod.GET;
import static request.HTTPRequestURI.INDEX;
import static request.HTTPVersion.HTTP_1_1;

public class HTTPRequestFake extends HTTPRequest {
    public HTTPMethod method() {
        return GET;
    }

    public HTTPRequestURI uri() {
        return INDEX;
    }

    public HTTPVersion version() {
        return HTTP_1_1;
    }

    public String body(){
        return "";
    }
}
