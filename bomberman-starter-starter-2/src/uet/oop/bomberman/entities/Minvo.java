package uet.oop.bomberman.entities;

import javafx.scene.image.Image;

public class Minvo extends Enemy {
    public Minvo(double x, double y, Image img) {
        super(x, y, img);
        this.type = "minvo";
        this.point = 800;
        this.speed = 1;
    }

    @Override
    public void update() {
        if(dying) deadHandle();
        else {
            moveHandle();
            Bomber.checkInstanceDamages(this);
        }
    }
}
