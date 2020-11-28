package uet.oop.bomberman.entities.moveEntities.enemy;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.moveEntities.Bomber;

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
