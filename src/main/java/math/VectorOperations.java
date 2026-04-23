package math;

import exceptions.DimensionMismatchException;
import models.WordVector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class VectorOperations {
    private VectorOperations() {}

    public static double[] add(double[] v1, double[] v2) throws DimensionMismatchException {
        if (v1.length != v2.length) throw new DimensionMismatchException(v1.length, v2.length);
        double[] result = new double[v1.length];
        for (int i = 0; i < v1.length; i++) result[i] = v1[i] + v2[i];
        return result;
    }

    public static double[] subtract(double[] v1, double[] v2) throws DimensionMismatchException {
        if (v1.length != v2.length) throw new DimensionMismatchException(v1.length, v2.length);
        double[] result = new double[v1.length];
        for (int i = 0; i < v1.length; i++) result[i] = v1[i] - v2[i];
        return result;
    }

    public static List<WordVector> findKNearest(double[] targetVector, List<WordVector> allVectors, DistanceStrategy strategy, int k, Set<String> excludeWords) {
        List<WordVector> sorted = new ArrayList<>(allVectors);

        if (excludeWords != null) {
            sorted.removeIf(v -> excludeWords.contains(v.word()));
        }

        sorted.sort((v1, v2) -> {
            try {
                double dist1 = strategy.calculate(targetVector, v1.coordinates());
                double dist2 = strategy.calculate(targetVector, v2.coordinates());
                return Double.compare(dist1, dist2);
            } catch (DimensionMismatchException e) {
                throw new RuntimeException("Dimension mismatch during distance calculation", e);
            }
        });

        return sorted.subList(0, Math.min(k, sorted.size()));
    }

    public static double[] centroid(List<double[]> vectors) throws DimensionMismatchException {
        if (vectors == null || vectors.isEmpty()) return new double[0];

        int dim = vectors.get(0).length;
        double[] center = new double[dim];

        for (double[] v : vectors) {
            if (v.length != dim) throw new DimensionMismatchException(dim, v.length);
            for (int i = 0; i < dim; i++) {
                center[i] += v[i];
            }
        }
        for (int i = 0; i < dim; i++) {
            center[i] /= vectors.size();
        }

        return center;
    }

    public static List<WordVector> solveAnalogy(double[] target1, double[] minus2, double[] plus3,
                                                List<WordVector> allVectors, DistanceStrategy strategy,
                                                int k, Set<String> excludeWords) throws DimensionMismatchException {
        double[] tempResult = subtract(target1, minus2);

        double[] finalVector = add(tempResult, plus3);

        return findKNearest(finalVector, allVectors, strategy, k, excludeWords);
    }
    public static double dotProduct(double[] v1, double[] v2) {
        double sum = 0;
        for (int i = 0; i < v1.length; i++) sum += v1[i] * v2[i];
        return sum;
    }

    public static double[] projectVector(double[] target, double[] v1, double[] v2) {
        try {
            double[] direction = subtract(v2, v1);
            double dotProductVD = dotProduct(target, direction);
            double dotProductDD = dotProduct(direction, direction);

            if (dotProductDD == 0) return new double[direction.length];

            double scalar = dotProductVD / dotProductDD;
            double[] projection = new double[direction.length];
            for (int i = 0; i < direction.length; i++) {
                projection[i] = scalar * direction[i];
            }
            return projection;
        } catch (DimensionMismatchException e) {
            return new double[target.length];
        }
    }
}