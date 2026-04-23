package exceptions;

public class InvalidAxisException extends LatentSpaceException {
    public InvalidAxisException(int axis, int maxDim) {
        super("Axis index " + axis + " is out of bounds for max dimension " + maxDim);
    }
}