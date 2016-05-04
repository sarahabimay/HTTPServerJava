import org.junit.Test;
import request.HTTPRequest;
import response.HTTPResponse;
import routeActions.AllowGETAndOPTIONSAction;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static request.HTTPMethod.OPTIONS;
import static request.HTTPRequestURI.OPTIONS_TWO;
import static request.HTTPVersion.HTTP_1_1;
import static response.EntityHeaderFields.ALLOW;

public class AllowGETAndOPTIONSActionTest {
    @Test
    public void createOptionResponse() {
        URIProcessorStub uriProcessorStub = new URIProcessorStub();
        HTTPResponse response = new AllowGETAndOPTIONSAction()
                                    .generateResponse(createOptionsRequest(), uriProcessorStub);
        assertEquals("HTTP/1.1 200 OK", response.getStatusLine());
        assertEquals(ALLOW, response.getEntityHeaders().keySet().toArray()[0]);
        assertEquals(2, response.getEntityHeaders().values().toString().split(",").length);
    }

    private HTTPRequest createOptionsRequest() {
        return new HTTPRequest().addRequestLine(optionsRequestLine());
    }

    private ArrayList<String> optionsRequestLine() {
        return new ArrayList<>(Arrays.asList(OPTIONS.method(), OPTIONS_TWO.uri(), HTTP_1_1.version()));
    }
}
