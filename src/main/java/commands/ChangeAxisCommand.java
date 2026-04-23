package commands;

import state.AppState;
import view3d.Space3D;
import exceptions.InvalidAxisException;

public class ChangeAxisCommand implements Command {
    private final Space3D space3D;
    private final int newX, newY, newZ;
    private int oldX, oldY, oldZ;

    public ChangeAxisCommand(Space3D space3D, int newX, int newY, int newZ) {
        this.space3D = space3D;
        this.newX = newX;
        this.newY = newY;
        this.newZ = newZ;
    }

    @Override
    public void execute() throws InvalidAxisException {
        AppState state = AppState.getInstance();
        if (newX >= 50 || newY >= 50 || newZ >= 50) {
            throw new InvalidAxisException(Math.max(newX, Math.max(newY, newZ)), 50);
        }
        oldX = state.getXAxisIndex();
        oldY = state.getYAxisIndex();
        oldZ = state.getZAxisIndex();

        state.setXAxisIndex(newX);
        state.setYAxisIndex(newY);
        state.setZAxisIndex(newZ);
        space3D.refreshPositions();
    }

    @Override
    public void undo() {
        AppState state = AppState.getInstance();
        state.setXAxisIndex(oldX);
        state.setYAxisIndex(oldY);
        state.setZAxisIndex(oldZ);
        space3D.refreshPositions();
    }
}