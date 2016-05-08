package routeActions;

import request.HTTPRequest;
import response.HTTPResponse;
import response.ResponseHTTPMessageFormatter;
import router.Router;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static response.HTTPStatusCode.OK;

public class ParameterDecodeAction implements RouteAction {
    @Override
    public boolean isAppropriate(HTTPRequest request) {
        return true;
    }

    @Override
    public HTTPResponse generateResponse(HTTPRequest request, Router router, URIProcessor uriProcessor) {
        return createDecodedParametersResponse(request);
    }

    private HTTPResponse createDecodedParametersResponse(HTTPRequest request) {
        return new HTTPResponse(new ResponseHTTPMessageFormatter())
                .setStatusLine(request.version(), OK)
                .setBody(decodeQueryParameters(formatQueryParameters(request.queryParameters())).getBytes());
    }

    private String decodeQueryParameters(String queryParameters) {
        String decodedParams = "";
        try {
            decodedParams = URLDecoder.decode(queryParameters, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decodedParams;
    }

    private String formatQueryParameters(String queryParameters) {
        CharSequence s1 = "=";
        CharSequence s2 = " = ";
        return queryParameters.replace(s1, s2).replace('&', '\n');
    }
}
