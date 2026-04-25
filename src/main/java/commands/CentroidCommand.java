package commands;

import math.DistanceStrategy;
import math.VectorOperations;
import models.WordVector;
import state.AppState;
import view3d.Space3D;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CentroidCommand implements Command {
    private final Space3D space3D;
    private final String[] words;
    private final int k;
    private final DistanceStrategy strategy;

    public CentroidCommand(Space3D space3D, String[] words, int k, DistanceStrategy strategy) {
        this.space3D = space3D;
        this.words = words;
        this.k = k;
        this.strategy = strategy;
    }

    @Override
    public void execute() throws Exception {
        List<double[]> vectors = new ArrayList<>();
        Set<String> searchWords = new HashSet<>();

        for (String w : words) {
            String cleanWord = w.trim();
            AppState.getInstance().getFullVector(cleanWord).ifPresent(wv -> {
                vectors.add(wv.coordinates());
                searchWords.add(cleanWord);
            });
        }

        if (vectors.isEmpty()) throw new Exception("None of the words were found.");

        double[] center = VectorOperations.centroid(vectors);
        List<WordVector> allFull = new ArrayList<>(AppState.getInstance().getAllFullVectors());
        List<WordVector> knn = VectorOperations.findKNearest(center, allFull, strategy, k, searchWords);

        space3D.clearVectors();
        space3D.drawKnnVectors(words[0], knn.stream().map(WordVector::word).toList());
    }

    @Override
    public void undo() {
        space3D.clearVectors();
    }
}