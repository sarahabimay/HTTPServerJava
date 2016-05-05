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
    public byte[] read(String resource) {
        return new byte[0];
    }

    @Override
    public String links() {
        return "";
    }
}
