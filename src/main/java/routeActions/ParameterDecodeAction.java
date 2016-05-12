package routeActions;

import request.HTTPRequest;
import response.HTTPResponse;
import response.ResponseHTTPMessageFormatter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static java.nio.charset.StandardCharsets.UTF_8;
import static response.HTTPStatusCode.OK;

public class ParameterDecodeAction implements RouteAction {
    @Override
    public boolean isAppropriate(HTTPRequest request) {
        return request.queryParameters() != null && decodeQueryParameters(request.queryParameters()) != "";
    }

    @Override
    public HTTPResponse generateResponse(HTTPRequest request) {
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
            decodedParams = URLDecoder.decode(queryParameters, UTF_8.toString());
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
