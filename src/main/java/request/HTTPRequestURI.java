package request;

public enum HTTPRequestURI {
    INDEX("/"),
    FOOBAR("/foobar"),
    FORM("/form"),
    OPTIONS_ONE("/method_options"),
    OPTIONS_TWO("/method_options2"),
    FILE1("/file1"),
    IMAGEJPEG("/image.jpeg"),
    IMAGEPNG("/image.png"),
    IMAGEGIF("/image.gif"),
    PARAMETERS("/parameters"),
    UNRECOGNIZED(""), ;

    private final String uri;


    HTTPRequestURI(String uri) {
        this.uri = uri;
    }

    public String uri() {
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
