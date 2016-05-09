package response;

public enum HTTPStatusCode {
    OK(200, "OK"),
    NO_CONTENT(204, "No Content"),
    PARTIAL_CONTENT(206, "Partial Content"),
    FOUND(302, "Found"),
    UNAUTHORIZED(401, "Unauthorized"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    PRECONDITION_FAILED(412, "Precondition Failed"),
    FOUR_EIGHTEEN(418, "I'm a teapot"),
    SERVER_ERROR(500, "Internal Server Error");

    private final int statusCode;
    private final String reason;

    HTTPStatusCode(int statusCode, String reason) {
        this.statusCode = statusCode;
        this.reason = reason;
    }

    public int statusCode() {
        return statusCode;
    }

    public String reason(){
        return reason;
    }
}
