package request;

public enum HTTPResource {
    COFFEE("/coffee"),
    FILE1("/file1"),
    FOOBAR("/foobar"),
    FORM("/form"),
    INDEX("/"),
    IMAGEJPEG("/image.jpeg"),
    IMAGEPNG("/image.png"),
    IMAGEGIF("/image.gif"),
    OPTIONS_ONE("/method_options"),
    OPTIONS_TWO("/method_options2"),
    PARAMETERS("/parameters"),
    PARTIAL_CONTENT("/partial_content.txt"),
    PATCH_CONTENT("/patch-content.txt"),
    TEA("/tea"),
    TEXT_FILE("/text-file.txt"),
    UNRECOGNIZED("");

    private final String uri;


    HTTPResource(String uri) {
        this.uri = uri;
    }

    public String uri() {
        return uri;
    }

    static public HTTPResource lookupURIName(String uri){
        HTTPResource[] uris = HTTPResource.values();
        for (HTTPResource aURI : uris) {
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
