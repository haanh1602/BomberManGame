package uet.oop.bomberman.entities.moveEntities.enemy;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.moveEntities.Bomber;

import java.util.Random;

public class Kondoria extends Enemy {
    public static Random random = new Random();
    public Kondoria(double x, double y, Image img) {
        super(x, y, img);
        this.speed = 1;
        this.type = "kondoria";
        this.point = 1000;
    }

    @Override
    public void update() {
        if (dying) {
            deadHandle();
        } else {
            moveHandle();
            Bomber.checkInstanceDamages(this);
        }
    }
}
