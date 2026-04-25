package view2d;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ControlPanel extends VBox {
    private final ComboBox<Integer> comboX = new ComboBox<>();
    private final ComboBox<Integer> comboY = new ComboBox<>();
    private final ComboBox<Integer> comboZ = new ComboBox<>();
    private final ComboBox<String> comboDistance = new ComboBox<>();
    private Button btn2D = btn2D = new Button("2D View");
    private Button btn3D = btn3D = new Button("3D View");

    private final TextField searchField = new TextField();
    private final Spinner<Integer> kSpinner = new Spinner<>(1, 50, 5);
    private final Button btnSearch = new Button("Search & Find KNN");
    private final Button btnCentroid = new Button("Calculate Centroid");

    private final TextField equationField = new TextField();
    private final Button btnEquation = new Button("Solve Equation");

    private final TextField projWord1Field = new TextField();
    private final TextField projWord2Field = new TextField();
    private final Button btnProjection = new Button("Project on Axis");
    private final Button btnDistance = new Button("Distance");


    private final Button btnUndo = new Button("Undo");
    private final Button btnRedo = new Button("Redo");

    public ControlPanel() {
        this.setPadding(new Insets(15));
        this.setSpacing(10);
        this.setStyle("-fx-background-color: #2b2b2b;");

        List<Integer> axes = IntStream.range(0, 50).boxed().collect(Collectors.toList());
        comboX.getItems().addAll(axes); comboX.setValue(0);
        comboY.getItems().addAll(axes); comboY.setValue(1);
        comboZ.getItems().addAll(axes); comboZ.setValue(2);

        comboDistance.getItems().addAll("Cosine Similarity", "Euclidean Distance");
        comboDistance.setValue("Cosine Similarity");

        searchField.setPromptText("Enter word...");
        equationField.setPromptText("e.g. king - man + woman");
        projWord1Field.setPromptText("Word 1 (e.g. poor)");
        projWord2Field.setPromptText("Word 2 (e.g. rich)");

        this.getChildren().addAll(
                createWhiteLabel("PCA Axes Selection"),
                new Label("X Axis:"), comboX,
                new Label("Y Axis:"), comboY,
                new Label("Z Axis:"), comboZ,
                btn2D,
                btn3D,
                new Separator(),

                createWhiteLabel("Search & Grouping (K-NN)"),
                searchField,
                new Label("Number of Neighbors (K):"), kSpinner,
                btnSearch,
                btnCentroid,
                new Separator(),

                createWhiteLabel("Vector Arithmetic"),
                new Label("Use spaces (+ / -):"),
                equationField,
                btnEquation,
                new Separator(),

                createWhiteLabel("Custom Projection & Distance"),
                projWord1Field,
                projWord2Field,
                new Label("Distance Metric:"), comboDistance,
                new HBox(10, btnProjection, btnDistance),
                new Separator(),

                btnUndo,
                btnRedo
        );

        for (javafx.scene.Node node : this.getChildren()) {
            if (node instanceof Label) {
                ((Label) node).setTextFill(Color.WHITE);
            }
        }
    }

    private Label createWhiteLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        return label;
    }

    public TextField getSearchField() { return searchField; }
    public Button getBtnSearch() { return btnSearch; }
    public Button getBtnCentroid() { return btnCentroid; }
    public Spinner<Integer> getKSpinner() { return kSpinner; }

    public TextField getEquationField() { return equationField; }
    public Button getBtnEquation() { return btnEquation; }

    public ComboBox<Integer> getComboX() { return comboX; }
    public ComboBox<Integer> getComboY() { return comboY; }
    public ComboBox<Integer> getComboZ() { return comboZ; }

    public TextField getProjWord1Field() { return projWord1Field; }
    public TextField getProjWord2Field() { return projWord2Field; }
    public Button getBtnProjection() { return btnProjection; }
    public Button getBtnDistance() { return btnDistance; }
    public ComboBox<String> getComboDistance() { return comboDistance; }

    public Button getBtn2D() { return btn2D; }
    public Button getBtn3D() { return btn3D; }
    public Button getBtnUndo() { return btnUndo; }
    public Button getBtnRedo() { return btnRedo; }
}