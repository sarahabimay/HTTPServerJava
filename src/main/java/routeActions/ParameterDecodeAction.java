package routeActions;

import request.HTTPRequest;
import response.HTTPResponse;
import router.Router;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static response.HTTPStatusCode.OK;

public class ParameterDecodeAction implements RouteAction {
    @Override
    public HTTPResponse generateResponse(HTTPRequest request, Router router, URIProcessor uriProcessor) {
        HTTPResponse response = new HTTPResponse();
        response.setStatusLine(request.version(), OK);
        response.setBody(decodeQueryParameters(formatQueryParameters(request.queryParameters())).getBytes());
        return response;
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
