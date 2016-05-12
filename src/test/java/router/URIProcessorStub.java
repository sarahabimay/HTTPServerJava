package router;

import routeActions.URIProcessor;

public class URIProcessorStub extends URIProcessor {
    public URIProcessorStub() {
        super("some/path");
    }

    @Override
    public void create(String resource, String newContent) {
    }

    @Override
    public void delete(String resource) {
    }

    @Override
    public byte[] read(String resource) {
        return new byte[0];
    }
}
