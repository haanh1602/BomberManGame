package uet.oop.bomberman.entities.moveEntities.enemy;

import javafx.scene.image.Image;

public class Doll extends Enemy{
    // TODO: đổi hướng ngẫu nhiên khi gặp ngã 3, ngã 4, speed nhanh
    public Doll(double x, double y, Image img) {
        super(x, y, img);
        this.type = "doll";
        this.speed = 5;
        this.point = 1200;
    }

    public Doll(double x, double y, Image img, int speed) {
        super(x, y, img, speed);
        this.type = "doll";
        this.speed = speed;
        this.point = 1200;
    }

    @Override
    public void moveHandle() {
        setDifOpsDirect();
        super.moveHandle();
    }
}
