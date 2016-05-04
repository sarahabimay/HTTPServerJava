import request.HTTPMethod;
import request.HTTPRequest;
import request.HTTPRequestURI;
import request.HTTPVersion;

import static request.HTTPMethod.*;
import static request.HTTPRequestURI.*;
import static request.HTTPVersion.*;

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
