public enum HTTPRequestURI {
    INDEX("/"),
    FOOBAR("/foobar"),
    FORM("/form"),
    UNRECOGNIZED("");

    private final String uri;


    HTTPRequestURI(String uri) {
        this.uri = uri;
    }

    String uri() {
        return uri;
    }

    static public HTTPRequestURI lookupURIName(String uri){
        HTTPRequestURI[] uris = HTTPRequestURI.values();
        for (HTTPRequestURI aURI : uris) {
            if (aURI.toString().equals(uri)) {
                return aURI;
            }
        }
        return UNRECOGNIZED;
    }

    @Override
    public String toString() {
        return uri;
    }
}
