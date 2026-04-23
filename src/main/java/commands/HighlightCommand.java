package commands;

import view3d.Space3D;

public class HighlightCommand implements Command {
    private final Space3D space3D;
    private final String word;

    public HighlightCommand(Space3D space3D, String word) {
        this.space3D = space3D;
        this.word = word;
    }

    @Override
    public void execute() {
        space3D.highlightWord(word);
    }

    @Override
    public void undo() {
        space3D.resetHighlights();
    }
}