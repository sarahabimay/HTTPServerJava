package response;

public enum HTTPStatusCode {
    OK(200, "OK"),
    NOT_FOUND(404, "Not Found");

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
