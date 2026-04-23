package models;

public class VectorSpaceBounds {
    private double minBound = Double.MAX_VALUE;
    private double maxBound = Double.MIN_VALUE;

    public void updateBounds(double[] coordinates) {
        for (double val : coordinates) {
            if (val < minBound) minBound = val;
            if (val > maxBound) maxBound = val;
        }
    }

    public double getMinBound() { return minBound; }
    public double getMaxBound() { return maxBound; }
}