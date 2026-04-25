package commands;

import math.DistanceStrategy;
import math.VectorOperations;
import models.WordVector;
import state.AppState;
import view3d.Space3D;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class KnnCommand implements Command {
    private final Space3D space3D;
    private final String centerWord;
    private final int k;
    private final DistanceStrategy strategy;

    public KnnCommand(Space3D space3D, String centerWord, int k, DistanceStrategy strategy) {
        this.space3D = space3D;
        this.centerWord = centerWord;
        this.k = k;
        this.strategy = strategy;
    }

    @Override
    public void execute() throws Exception {
        AppState state = AppState.getInstance();
        WordVector target = state.getFullVector(centerWord)
                .orElseThrow(() -> new Exception("Word not found: " + centerWord));

        List<WordVector> allFull = new ArrayList<>(state.getAllFullVectors());
        List<WordVector> knn = VectorOperations.findKNearest(
                target.coordinates(), allFull, strategy, k, Set.of(centerWord)
        );

        space3D.drawKnnVectors(centerWord, knn.stream().map(WordVector::word).toList());
    }

    @Override
    public void undo() {
        space3D.clearVectors();
    }
}