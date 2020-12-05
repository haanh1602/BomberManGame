package uet.oop.bomberman.entities.moveEntities.enemy;

import javafx.scene.image.Image;

public class Ballom extends Enemy {
    public Ballom(double x, double y, Image img) {
        super(x, y, img);
        this.speed = 1;
        this.point = 100;
        this.type = "balloom";
    }

    @Override
    public void moveHandle() {
        setDifOpsDirect();
        super.moveHandle();
    }
}
