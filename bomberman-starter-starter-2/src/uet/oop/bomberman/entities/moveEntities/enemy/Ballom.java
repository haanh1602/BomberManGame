package uet.oop.bomberman.entities.moveEntities.enemy;

import javafx.scene.image.Image;

public class Ballom extends Enemy {
    // TODO: đổi hướng khi chạm tường, speed chậm
    public Ballom(double x, double y, Image img) {
        super(x, y, img);
        this.speed = 1;
        this.point = 100;
        this.type = "balloom";
    }

    @Override
    public void moveHandle() {
        super.moveHandle();
    }
}
