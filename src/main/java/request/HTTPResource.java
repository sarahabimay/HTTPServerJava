package request;

public enum HTTPResource {
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
    TEXT_FILE("/text-file.txt"),
    PARTIAL_CONTENT("/partial_content.txt"),
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
