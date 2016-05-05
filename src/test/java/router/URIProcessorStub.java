package router;

import routeActions.URIProcessor;

public class URIProcessorStub extends URIProcessor {
    public URIProcessorStub() {
        super(null);
    }

    @Override
    public void create(String resource, String data) {
    }

    @Override
    public void delete(String resource) {
    }

    @Override
    public String read(String resource) {
        return "";
    }
}
