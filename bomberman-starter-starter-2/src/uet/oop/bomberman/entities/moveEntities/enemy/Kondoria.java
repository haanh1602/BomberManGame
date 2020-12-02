package uet.oop.bomberman.entities.moveEntities.enemy;

import javafx.scene.image.Image;

public class Kondoria extends Enemy {
    public Kondoria(double x, double y, Image img) {
        super(x, y, img);
        this.speed = 1;
        this.type = "kondoria";
        this.point = 1000;
    }
}
