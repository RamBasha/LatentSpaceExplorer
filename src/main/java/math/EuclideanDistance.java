package math;
import exceptions.DimensionMismatchException;

public class EuclideanDistance implements DistanceStrategy {
    @Override
    public double calculate(double[] vec1, double[] vec2) throws DimensionMismatchException {
        if (vec1.length != vec2.length) throw new DimensionMismatchException(vec1.length, vec2.length);
        double sum = 0.0;
        for (int i = 0; i < vec1.length; i++) {
            sum += Math.pow(vec1[i] - vec2[i], 2);
        }
        return Math.sqrt(sum);
    }
}