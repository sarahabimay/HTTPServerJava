package response;

public enum EntityHeaderFields {
    ALLOW("Allow"),
    CONTENT_LENGTH("Content-Length"),
    CONTENT_RANGE("Content-Range"),
    CONTENT_LOCATION("Content-Location"),
    ETAG("Etag"),
    IF_MATCH("If-Match"),
    LOCATION("Location"),
    RANGE("Range"),
    INVALID_HEADER("INVALID_HEADER");

    private final String field;

    EntityHeaderFields(String field){
        this.field = field;
    }

    public String field(){
        return field;
    }

    static public EntityHeaderFields lookupHeaderField(String field){
        EntityHeaderFields[] fields = EntityHeaderFields.values();
        for (EntityHeaderFields aField : fields) {
            if (aField.toString().equals(field)) {
                return aField;
            }
        }
        return INVALID_HEADER;
    }

    @Override
    public String toString() {
        return field;
    }
}
