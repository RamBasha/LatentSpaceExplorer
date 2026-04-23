package exceptions;

public class DimensionMismatchException extends LatentSpaceException {
    public DimensionMismatchException(int expected, int actual) {
        super("Dimension mismatch: expected " + expected + " but got " + actual + ".");
    }
}