package view3d;

import javafx.scene.Group;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.geometry.Point3D;
import javafx.scene.transform.Rotate;
import state.AppState;
import models.WordVector;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Space3D extends SubScene {
    private final Group rootGroup;
    private final Map<String, WordNode> nodes = new HashMap<>();
    private final Group vectorGroup = new Group();
    private final double SCALE = 1200.0;
    private final Axes3D axes;
    private final CameraHandler cameraHandler;

    public Space3D(double width, double height) {
        super(new Group(), width, height, true, SceneAntialiasing.BALANCED);
        this.setFill(Color.web("#1e1e1e"));
        this.rootGroup = (Group) this.getRoot();

        this.axes = new Axes3D(600);
        this.rootGroup.getChildren().add(axes);
        this.rootGroup.getChildren().add(vectorGroup);
        this.cameraHandler = new CameraHandler(this);
    }
    public void populateSpace() {
        AppState state = AppState.getInstance();
        for (WordVector vec : state.getAllPcaVectors()) {
            WordNode node = new WordNode(vec.word());
            nodes.put(vec.word(), node);
            rootGroup.getChildren().add(node);
        }
        refreshPositions();
    }
    public void bindNodeClicks(java.util.function.Consumer<String> onNodeClicked) {
        for (Map.Entry<String, WordNode> entry : nodes.entrySet()) {
            String word = entry.getKey();
            WordNode node = entry.getValue();
            node.setOnMousePressed(event -> {
                onNodeClicked.accept(word);
                event.consume();
            });
        }
    }

    public void refreshPositions() {
        AppState state = AppState.getInstance();
        double bound = Math.max(Math.abs(state.getPcaBounds().getMaxBound()), Math.abs(state.getPcaBounds().getMinBound()));
        double dynamicScale = bound > 0 ? SCALE / bound : SCALE;

        for (WordVector vec : state.getAllPcaVectors()) {
            WordNode node = nodes.get(vec.word());
            if (node != null) {
                node.setTranslateX(vec.getCoordinate(state.getXAxisIndex()) * dynamicScale);
                node.setTranslateY(vec.getCoordinate(state.getYAxisIndex()) * dynamicScale);
                node.setTranslateZ(vec.getCoordinate(state.getZAxisIndex()) * dynamicScale);
            }
        }
    }

    public void highlightWord(String word) {
        if (nodes.containsKey(word)) {
            nodes.get(word).setHighlighted(true);
        }
    }

    public void drawKnnVectors(String centerWord, List<String> neighbors) {
        clearVectors();
        axes.setVisible(false);
        nodes.values().forEach(n -> n.setDimmed(true));

        WordNode centerNode = nodes.get(centerWord);
        if (centerNode != null) centerNode.setHighlighted(true);

        for (String neighbor : neighbors) {
            WordNode neighborNode = nodes.get(neighbor);
            if (neighborNode != null) {
                neighborNode.setHighlighted(true);
                if (centerNode != null) {
                    vectorGroup.getChildren().add(createLine3D(centerNode, neighborNode, Color.YELLOW));
                }
            }
        }
    }

    public void drawEquationPath(List<String> equationWords, List<String> resultWords) {
        clearVectors();
        axes.setVisible(false);
        nodes.values().forEach(n -> n.setDimmed(true));

        WordNode prevNode = null;
        for (String w : equationWords) {
            WordNode n = nodes.get(w);
            if (n != null) {
                n.setHighlighted(true);
                if (prevNode != null) {
                    vectorGroup.getChildren().add(createLine3D(prevNode, n, Color.MAGENTA));
                }
                prevNode = n;
            }
        }

        for (String res : resultWords) {
            WordNode resNode = nodes.get(res);
            if (resNode != null) {
                resNode.setHighlighted(true);
                if (prevNode != null) {
                    vectorGroup.getChildren().add(createLine3D(prevNode, resNode, Color.CYAN));
                }
            }
        }
    }

    public void clearVectors() {
        vectorGroup.getChildren().clear();
        nodes.values().forEach(n -> n.setDimmed(false));
        nodes.values().forEach(n -> n.setHighlighted(false));
        axes.setVisible(true);
    }

    private Cylinder createLine3D(WordNode start, WordNode end, Color color) {
        Point3D p1 = new Point3D(start.getTranslateX(), start.getTranslateY(), start.getTranslateZ());
        Point3D p2 = new Point3D(end.getTranslateX(), end.getTranslateY(), end.getTranslateZ());
        Point3D diff = p2.subtract(p1);

        Cylinder line = new Cylinder(1.5, diff.magnitude());
        line.setMaterial(new PhongMaterial(color));

        Point3D mid = p1.midpoint(p2);
        line.setTranslateX(mid.getX());
        line.setTranslateY(mid.getY());
        line.setTranslateZ(mid.getZ());

        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D axisOfRotation = diff.crossProduct(yAxis);
        double angle = Math.acos(diff.normalize().dotProduct(yAxis));
        if (axisOfRotation.magnitude() > 0) {
            line.getTransforms().add(new Rotate(-Math.toDegrees(angle), axisOfRotation));
        }
        return line;
    }
    public void resetHighlights() {
        nodes.values().forEach(n -> n.setHighlighted(false));
    }

    public void addHighlight(String word) {
        if (nodes.containsKey(word)) {
            nodes.get(word).setHighlighted(true);
        }
    }
    public void drawAnalogyVector(String w1, String w2, String w3, String result) {
        clearVectors();
        axes.setVisible(false);
        nodes.values().forEach(n -> n.setDimmed(true));

        WordNode n1 = nodes.get(w1);
        WordNode n2 = nodes.get(w2);
        WordNode n3 = nodes.get(w3);
        WordNode nResult = nodes.get(result);

        if (n1 != null) n1.setHighlighted(true);
        if (n2 != null) n2.setHighlighted(true);
        if (n3 != null) n3.setHighlighted(true);
        if (nResult != null) nResult.setHighlighted(true);

        if (n1 != null && n2 != null) {
            vectorGroup.getChildren().add(createLine3D(n1, n2, javafx.scene.paint.Color.MAGENTA));
        }
        if (n3 != null && nResult != null) {
            vectorGroup.getChildren().add(createLine3D(n3, nResult, javafx.scene.paint.Color.CYAN));
        }
    }
    public Map<String, WordNode> getNodes() {
        return nodes;
    }

    public void centerCameraOnNode(String word) {
        WordNode node = nodes.get(word);
        if (node != null) {
            cameraHandler.centerOn(node.getTranslateX(), node.getTranslateY(), node.getTranslateZ());
        }
    }
}