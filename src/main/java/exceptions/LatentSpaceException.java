package exceptions;

public abstract class LatentSpaceException extends Exception {
    public LatentSpaceException(String message) {
        super(message);
    }
    public LatentSpaceException(String message, Throwable cause) {
        super(message, cause);
    }
}