public enum HTTPVersion {
    HTTP_1_1("HTTP/1.1"),
    UNDEFINED("");

    private final String version;

    HTTPVersion(String version) {
        this.version = version;
    }

    String version() {
        return version;
    }

    static public HTTPVersion lookupVersionName(String version){
        HTTPVersion[] versions = HTTPVersion.values();
        for (HTTPVersion aVersion : versions) {
            if (aVersion.toString().equals(version)) {
                return aVersion;
            }
        }
        return UNDEFINED;
    }

    @Override
    public String toString() {
        return version;
    }
}
