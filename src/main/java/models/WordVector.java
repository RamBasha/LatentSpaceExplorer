package models;


public record WordVector(String word, double[] coordinates, VectorType type) {


    public double getCoordinate(int axisIndex) {
        if (axisIndex < 0 || axisIndex >= coordinates.length) {
            return 0.0;
        }
        return coordinates[axisIndex];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WordVector that)) return false;
        return word.equals(that.word) && type == that.type;
    }

    @Override
    public int hashCode() {
        return word.hashCode() + type.hashCode();
    }
}