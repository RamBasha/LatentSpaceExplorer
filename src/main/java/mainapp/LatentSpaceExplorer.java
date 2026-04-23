package mainapp;

import controllers.AppController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import view2d.ControlPanel;
import view2d.MetricsPanel;
import view3d.Space3D;

public class LatentSpaceExplorer extends Application {

    @Override
    public void start(Stage primaryStage) {
        // UI Initialization
        Space3D space3D = new Space3D(800, 600);
        ControlPanel controlPanel = new ControlPanel();
        MetricsPanel metricsPanel = new MetricsPanel();

        AppController controller = new AppController(space3D, controlPanel, metricsPanel);

        BorderPane root = new BorderPane();
        javafx.scene.layout.StackPane centerContainer = new javafx.scene.layout.StackPane(space3D);

        space3D.widthProperty().bind(centerContainer.widthProperty());
        space3D.heightProperty().bind(centerContainer.heightProperty());

        root.setCenter(centerContainer);
        javafx.scene.control.ScrollPane scrollPane = new javafx.scene.control.ScrollPane(controlPanel);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #2b2b2b; -fx-border-color: transparent;"); // שומר על צבע הרקע הכהה
        root.setLeft(scrollPane);
        root.setBottom(metricsPanel);

        Scene scene = new Scene(root, 1050, 650);
        primaryStage.setTitle("LatentSpace Explorer");
        primaryStage.setScene(scene);
        primaryStage.show();


        controller.loadInitialData();

    }

    public static void main(String[] args) {
        launch(args);
    }
}