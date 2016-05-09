package exceptions;

public class RequestParsingException extends RuntimeException {
    public RequestParsingException(String message) {
        super(exceptionMessage(message));
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    private static String exceptionMessage(String otherExceptionMessage) {
        return String.format("Error Parsing the RAW HTTP Request: \n%s", otherExceptionMessage);
    }
}
