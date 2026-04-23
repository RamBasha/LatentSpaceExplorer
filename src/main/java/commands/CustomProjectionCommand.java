package commands;

import models.WordVector;
import state.AppState;
import view3d.Space3D;
import view3d.WordNode;
import math.VectorOperations;
import java.util.Optional;
import java.util.Map;

public class CustomProjectionCommand implements Command {
    private final String word1;
    private final String word2;
    private final Space3D space3D;

    public CustomProjectionCommand(String word1, String word2, Space3D space3D) {
        this.word1 = word1.toLowerCase();
        this.word2 = word2.toLowerCase();
        this.space3D = space3D;
    }

    @Override
    public void execute() throws Exception {
        AppState state = AppState.getInstance();
        Optional<WordVector> opt1 = state.getFullVector(word1);
        Optional<WordVector> opt2 = state.getFullVector(word2);

        if (opt1.isEmpty() || opt2.isEmpty()) {
            throw new Exception("One or both words for projection not found.");
        }

        double[] vec1 = opt1.get().coordinates();
        double[] vec2 = opt2.get().coordinates();
        Map<String, WordNode> nodes = space3D.getNodes();

        for (WordVector vec : state.getAllFullVectors()) {
            WordNode node = nodes.get(vec.word());
            if (node != null) {
                double[] projected = VectorOperations.projectVector(vec.coordinates(), vec1, vec2);
                node.setTranslateX(projected[0] * 100);
                node.setTranslateY(projected.length > 1 ? projected[1] * 100 : 0);
                node.setTranslateZ(projected.length > 2 ? projected[2] * 100 : 0);
            }
        }

        space3D.clearVectors();
        space3D.addHighlight(word1);
        space3D.addHighlight(word2);
    }

    @Override
    public void undo() {
        space3D.refreshPositions();
        space3D.clearVectors();
    }
}