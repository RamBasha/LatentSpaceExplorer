package commands;

import math.DistanceStrategy;
import math.VectorOperations;
import models.WordVector;
import state.AppState;
import exceptions.WordNotFoundException;
import view3d.Space3D;

public class AnalogyCommand implements Command {
    private final String w1, w2, w3;
    private final DistanceStrategy strategy;
    private final Space3D space3D;
    private String resultWord;

    public AnalogyCommand(String w1, String w2, String w3, DistanceStrategy strategy, Space3D space3D) {
        this.w1 = w1; this.w2 = w2; this.w3 = w3;
        this.strategy = strategy;
        this.space3D = space3D;
    }

    @Override
    public void execute() throws Exception {
        AppState state = AppState.getInstance();
        WordVector vec1 = state.getFullVector(w1).orElseThrow(() -> new WordNotFoundException(w1));
        WordVector vec2 = state.getFullVector(w2).orElseThrow(() -> new WordNotFoundException(w2));
        WordVector vec3 = state.getFullVector(w3).orElseThrow(() -> new WordNotFoundException(w3));

        // Analogy formula: vec2 - vec1 + vec3
        double[] diff = VectorOperations.subtract(vec2.coordinates(), vec1.coordinates());
        double[] targetCoords = VectorOperations.add(diff, vec3.coordinates());

        // Find closest in full space
        double minDistance = Double.MAX_VALUE;
        for (WordVector candidate : state.getAllFullVectors()) {
            if (candidate.word().equals(w1) || candidate.word().equals(w2) || candidate.word().equals(w3)) continue;
            double dist = strategy.calculate(targetCoords, candidate.coordinates());
            if (dist < minDistance) {
                minDistance = dist;
                resultWord = candidate.word();
            }
        }

        space3D.drawAnalogyVector(w1, w2, w3, resultWord);
    }

    @Override
    public void undo() {
        space3D.clearVectors();
    }
}