package messages;

import configuration.Configuration;
import org.junit.Test;
import request.HTTPResource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static messages.EntityHeaderFields.ALLOW;
import static org.junit.Assert.assertEquals;
import static request.HTTPMethod.*;

public class EntityHeaderBuilderTest {
    @Test
    public void getNoALLOWHeaderValues() {
        Map<EntityHeaderFields, List<String>> headers = new EntityHeaderBuilder(new Configuration())
                .createALLOWHeader(UNDEFINED, HTTPResource.FILE1)
                .buildAllHeaders();

        assertEquals(new HashMap<>(), headers);
    }

    @Test
    public void getALLOWHeaderValues() {
        Map<EntityHeaderFields, List<String>> headers = new EntityHeaderBuilder(new Configuration())
                .createALLOWHeader(PUT, HTTPResource.FILE1)
                .buildAllHeaders();

        assertEquals(expectedALLOWHeader(), headers.get(ALLOW));
    }

    private List<String> expectedALLOWHeader() {
        return asList(
                GET.method(),
                POST.method(),
                HEAD.method(),
                OPTIONS.method(),
                DELETE.method(),
                PATCH.method());
    }
}
