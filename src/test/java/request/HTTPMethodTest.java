package request;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static request.HTTPMethod.*;

public class HTTPMethodTest {
    @Test
    public void allHTTPMethods() {
        List<String> httpMethods = httpMethods();
        assertEquals(7, httpMethods.size());
        assertThat(httpMethods, hasItem(HEAD.method()));
        assertThat(httpMethods, hasItem(POST.method()));
        assertThat(httpMethods, hasItem(PUT.method()));
        assertThat(httpMethods, hasItem(GET.method()));
        assertThat(httpMethods, hasItem(OPTIONS.method()));
        assertThat(httpMethods, hasItem(PATCH.method()));
        assertThat(httpMethods, hasItem(DELETE.method()));
    }

    @Test
    public void lookupValidHTTPMethod() {
        assertEquals(GET, lookupMethod("GET"));
    }

    @Test
    public void inValidHTTPMethodIsUndefined() {
        assertEquals(UNDEFINED, lookupMethod("bogusrequest"));
    }
}
