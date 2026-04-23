package exceptions;

public class DataLoadException extends LatentSpaceException {
    public DataLoadException(String message, Throwable cause) {
        super("Failed to load vector data: " + message, cause);
    }
}