package request;

public enum HTTPMethod {
    GET("GET"),
    PUT("PUT"),
    POST("POST"),
    HEAD("HEAD"),
    OPTIONS("OPTIONS"),
    UNDEFINED("");

    private final String method;

    HTTPMethod(String method) {
        this.method = method;
    }

    public String method() {
        return method;
    }

    public static HTTPMethod lookupMethod(String method) {
        HTTPMethod[] methods = HTTPMethod.values();
        for (HTTPMethod methodName : methods) {
            if (methodName.toString().equals(method)) {
                return methodName;
            }
        }
        return UNDEFINED;
    }
}
