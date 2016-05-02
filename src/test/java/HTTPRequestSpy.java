import request.HTTPMethod;
import request.HTTPRequest;
import request.HTTPRequestURI;
import request.HTTPVersion;

public class HTTPRequestSpy extends HTTPRequest {
    public HTTPMethod method() {
        return HTTPMethod.GET;
    }

    public HTTPRequestURI uri() {
        return HTTPRequestURI.INDEX;
    }

    public HTTPVersion version() {
        return HTTPVersion.HTTP_1_1;
    }

    public String body(){
        return "";
    }
}
