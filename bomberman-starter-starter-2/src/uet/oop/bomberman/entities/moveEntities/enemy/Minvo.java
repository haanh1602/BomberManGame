package uet.oop.bomberman.entities.moveEntities.enemy;

import javafx.scene.image.Image;

public class Minvo extends Enemy {
    // TODO: xuyên tường, speed trung
    public Minvo(double x, double y, Image img) {
        super(x, y, img);
        this.type = "minvo";
        this.point = 800;
        this.speed = 3;
        brickPass = true;
    }

    @Override
    public void moveHandle() {
        setDifOpsDirect();
        super.moveHandle();
    }
}
