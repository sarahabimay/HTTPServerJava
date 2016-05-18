package request;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum HTTPMethod {
    GET("GET"),
    PUT("PUT"),
    POST("POST"),
    HEAD("HEAD"),
    OPTIONS("OPTIONS"),
    DELETE("DELETE"),
    PATCH("PATCH"),
    UNDEFINED("UNDEFINED"),
    ERROR("ERROR");

    private final String method;

    HTTPMethod(String method) {
        this.method = method;
    }

    public String method() {
        return method;
    }

    public static HTTPMethod lookupMethod(String method) {
        HTTPMethod[] methods = values();
        for (HTTPMethod methodName : methods) {
            if (methodName.toString().equals(method)) {
                return methodName;
            }
        }
        return UNDEFINED;
    }

    public static List<String> httpMethods() {
        return Arrays.asList(values())
                .stream()
                .filter(httpMethod -> isValidHTTPMethod(httpMethod))
                .map(HTTPMethod::method)
                .collect(Collectors.toList());
    }

    private static boolean isValidHTTPMethod(HTTPMethod httpMethod) {
        return httpMethod != ERROR && httpMethod != UNDEFINED;
    }
}
