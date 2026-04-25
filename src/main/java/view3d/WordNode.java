package view3d;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Text;

public class WordNode extends Group {
    private final Sphere sphere;
    private final Text label;
    private final String word;

    private final PhongMaterial defaultMaterial = new PhongMaterial(Color.LIGHTCYAN);
    private final PhongMaterial highlightMaterial = new PhongMaterial(Color.LIMEGREEN);
    private final PhongMaterial dimmedMaterial = new PhongMaterial(Color.DARKGRAY);

    public WordNode(String word) {
        this.word = word;
        this.sphere = new Sphere(2.5);
        this.sphere.setMaterial(defaultMaterial);

        this.label = new Text(word);
        this.label.setFill(Color.LIMEGREEN);
        this.label.setTranslateX(4);
        this.label.setTranslateY(4);
        this.label.setVisible(false);

        this.getChildren().addAll(sphere, label);


        this.setOnMouseClicked(e -> {
            boolean isCurrentlyVisible = label.isVisible();
            setHighlighted(!isCurrentlyVisible);
        });
    }

    public void setHighlighted(boolean active) {
        sphere.setMaterial(active ? highlightMaterial : defaultMaterial);
        label.setVisible(active);
    }

    public void setDimmed(boolean dimmed) {
        if (dimmed) {
            sphere.setMaterial(dimmedMaterial);
            label.setVisible(false);
        } else {
            sphere.setMaterial(defaultMaterial);
        }
    }

    public String getWord() { return word; }
}