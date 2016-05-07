package response;

public enum HTTPStatusCode {
    OK(200, "OK"),
    NO_CONTENT(204, "No Content"),
    PARTIAL_CONTENT(206, "Partial Content"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    PRECONDITION_FAILED(412, "Precondition Failed");

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
