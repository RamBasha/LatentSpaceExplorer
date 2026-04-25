package view3d;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;

public class Axes3D extends Group {
    private final Cylinder zAxis;

    public Axes3D(double length) {
        Cylinder xAxis = new Cylinder(1, length);
        xAxis.setMaterial(new PhongMaterial(Color.RED));
        xAxis.setRotationAxis(Rotate.Z_AXIS);
        xAxis.setRotate(90);

        Cylinder yAxis = new Cylinder(1, length);
        yAxis.setMaterial(new PhongMaterial(Color.GREEN));

        zAxis = new Cylinder(1, length);
        zAxis.setMaterial(new PhongMaterial(Color.BLUE));
        zAxis.setRotationAxis(Rotate.X_AXIS);
        zAxis.setRotate(90);

        this.getChildren().addAll(xAxis, yAxis, zAxis);
    }

    public void set2DMode(boolean is2D) {
        zAxis.setVisible(!is2D);
    }
}