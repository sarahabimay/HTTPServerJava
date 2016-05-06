package request;

import static request.HTTPMethod.GET;
import static request.HTTPResource.INDEX;
import static request.HTTPVersion.HTTP_1_1;

public class HTTPRequestFake extends HTTPRequest {
    public HTTPMethod method() {
        return GET;
    }

    public HTTPResource uri() {
        return INDEX;
    }

    public HTTPVersion version() {
        return HTTP_1_1;
    }

    public String body(){
        return "";
    }
}
