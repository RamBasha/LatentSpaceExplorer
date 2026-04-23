package view2d;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class MetricsPanel extends HBox {
    private final Label statusLabel = new Label("System Ready");

    public MetricsPanel() {
        this.setPadding(new Insets(10));
        this.setStyle("-fx-background-color: #1e1e1e; -fx-border-color: #444; -fx-border-width: 1 0 0 0;");
        statusLabel.setTextFill(javafx.scene.paint.Color.LIGHTGREEN);
        this.getChildren().add(statusLabel);
    }

    public void updateStatus(String message) {
        statusLabel.setText(message);
    }
}