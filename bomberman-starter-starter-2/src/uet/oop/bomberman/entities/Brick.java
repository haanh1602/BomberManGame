package uet.oop.bomberman.entities;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;

import java.util.List;

public class Brick extends Entity {
    public Brick(int x, int y, Image img) {
        super(x, y, img);
    }

    @Override
    public void update() {

    }

    @Override
    public void update(Scene scene, KeyEvent event, List<Entity> entities) {

    }

    @Override
    public boolean isAlive() {
        return false;
    }
}
