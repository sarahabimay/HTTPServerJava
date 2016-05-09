package exceptions;

public enum RequestErrors {
    UNAVAILABLE_INPUT_READER("Unavailable Input Stream");

    private final String errorMessage;

    RequestErrors(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String errorMessage() {
        return errorMessage;
    }
}
