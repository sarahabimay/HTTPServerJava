public enum HTTPStatusCode {
    OK(200, "OK"),
    NOT_FOUND(404, "Not Found");

    private final int statusCode;
    private final String reason;

    HTTPStatusCode(int statusCode, String reason) {
        this.statusCode = statusCode;
        this.reason = reason;
    }

    int statusCode() {
        return statusCode;
    }

    String reason(){
        return reason;
    }
}
