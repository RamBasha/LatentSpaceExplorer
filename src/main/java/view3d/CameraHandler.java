package view3d;

import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class CameraHandler {
    private final PerspectiveCamera camera;
    private final Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    private final Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
    private final Translate translate = new Translate(0, 0, -800);
    private double mousePosX, mousePosY;

    public CameraHandler(SubScene scene) {
        camera = new PerspectiveCamera(true);
        camera.getTransforms().addAll(rotateX, rotateY, translate);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        scene.setCamera(camera);
        setupMouseEvents(scene);
    }

    private void setupMouseEvents(SubScene scene) {
        scene.setOnMousePressed(me -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
        });

        scene.setOnMouseDragged(me -> {
            double dx = me.getSceneX() - mousePosX;
            double dy = me.getSceneY() - mousePosY;
            rotateY.setAngle(rotateY.getAngle() + dx * 0.2);
            rotateX.setAngle(rotateX.getAngle() - dy * 0.2);
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
        });

        scene.setOnScroll(se -> {
            translate.setZ(translate.getZ() + se.getDeltaY() * 2);
        });
    }
    public void centerOn(double x, double y, double z) {
        translate.setX(x);
        translate.setY(y);
        translate.setZ(z - 500);
        rotateX.setAngle(0);
        rotateY.setAngle(0);
    }
    public void resetCamera() {
        if (camera != null) {
            camera.getTransforms().clear();
        }
    }
}