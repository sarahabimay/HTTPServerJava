package exceptions;

public class ResourceManagementException extends RuntimeException {
    public ResourceManagementException(String message) {
        super(exceptionMessage(message));
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    private static String exceptionMessage(String otherExceptionMessage) {
        return String.format("Error Occurred Managing a Resource: \n%s", otherExceptionMessage);
    }
}
