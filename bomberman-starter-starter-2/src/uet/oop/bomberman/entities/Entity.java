package uet.oop.bomberman.entities;

import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

import java.util.List;

public abstract class Entity {
    protected double x;
    protected double y;
    protected Image img;

    public Entity( double x, double y, Image img) {
        this.x = x;
        this.y = y;
        this.img = img;
    }

    public void render(GraphicsContext gc) {
        /*SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        ImageView iv = new ImageView(img);
        Image base = iv.snapshot(params, null);*/

        gc.drawImage(img, x * Sprite.SCALED_SIZE, y * Sprite.SCALED_SIZE);
    }
    public abstract void update();

    public abstract void update(Scene scene, KeyEvent event, List<Entity> entities);

    public abstract boolean isAlive();

    /*public void update(KeyEvent event) {
        switch (event.getCode()) {
            case UP:
                if(y > 1) this.y -= 1;
                break;
            case DOWN:
                if(y < new BombermanGame().HEIGHT - 1) this.y += 1;
                break;
            case LEFT:
                if(x > 1) this.x -= 1;
                break;
            case RIGHT:
                if(x < new BombermanGame().WIDTH - 1) this.x += 1;
                break;
        }
    }*/
}
