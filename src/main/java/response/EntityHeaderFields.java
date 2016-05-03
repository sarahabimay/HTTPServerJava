package response;

public enum EntityHeaderFields {
    Allow("Allow");

    private final String field;

    EntityHeaderFields(String field){
        this.field = field;
    }

    public String field(){
        return field;
    }
}
