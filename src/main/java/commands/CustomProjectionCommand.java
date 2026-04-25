package commands;

import models.WordVector;
import state.AppState;
import view3d.Space3D;

import java.util.Optional;

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

        space3D.refreshPositions();

        space3D.drawDistanceLineAndDim(word1, word2);
    }

    @Override
    public void undo() {
        space3D.refreshPositions();
        space3D.clearVectors();
    }
}