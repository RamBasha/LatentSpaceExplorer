package math;
import exceptions.DimensionMismatchException;

public interface DistanceStrategy {
    double calculate(double[] vec1, double[] vec2) throws DimensionMismatchException;
}