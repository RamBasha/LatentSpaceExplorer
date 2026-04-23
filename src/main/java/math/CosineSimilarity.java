package math;
import exceptions.DimensionMismatchException;

public class CosineSimilarity implements DistanceStrategy {
    @Override
    public double calculate(double[] vec1, double[] vec2) throws DimensionMismatchException {
        if (vec1.length != vec2.length) throw new DimensionMismatchException(vec1.length, vec2.length);
        double dotProduct = 0.0, normA = 0.0, normB = 0.0;
        for (int i = 0; i < vec1.length; i++) {
            dotProduct += vec1[i] * vec2[i];
            normA += Math.pow(vec1[i], 2);
            normB += Math.pow(vec2[i], 2);
        }
        if (normA == 0 || normB == 0) return 0;
        // Cosine similarity is 1 for identical, -1 for opposite. Convert to a "distance" metric (0 is closest)
        return 1.0 - (dotProduct / (Math.sqrt(normA) * Math.sqrt(normB)));
    }
}