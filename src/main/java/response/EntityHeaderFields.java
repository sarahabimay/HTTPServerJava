package response;

public enum EntityHeaderFields {
    ALLOW("Allow"),
    RANGE("Range"),
    CONTENT_LENGTH("Content-Length"),
    CONTENT_RANGE("Content-Range"),
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
