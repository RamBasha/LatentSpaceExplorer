package state;

import commands.Command;
import java.util.Stack;

public class CommandHistory {
    private final Stack<Command> history = new Stack<>();
    private final Stack<Command> redoStack = new Stack<>();

    public void executeCommand(Command cmd) {
        try {
            cmd.execute();
            history.push(cmd);
            redoStack.clear();
        } catch (Exception e) {
            System.err.println("Command execution failed: " + e.getMessage());
        }
    }

    public void undo() {
        if (!history.isEmpty()) {
            Command cmd = history.pop();
            cmd.undo();
            redoStack.push(cmd);
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            Command cmd = redoStack.pop();
            try {
                cmd.execute();
                history.push(cmd);
            } catch (Exception e) {
                System.err.println("Redo failed: " + e.getMessage());
            }
        }
    }
}