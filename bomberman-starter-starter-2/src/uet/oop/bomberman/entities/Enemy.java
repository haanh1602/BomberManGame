package uet.oop.bomberman.entities;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

import java.util.List;

import static uet.oop.bomberman.entities.Ballom.random;

public class Enemy extends Entity{
    public boolean left = false;
    public boolean right = false;
    public boolean up = false;
    public boolean down = false;

    private int timeLeft = 100;  // ~1.5s
    protected boolean dying = false;
    protected String type;
    //protected boolean isAlive = true;
    protected int speed;
    protected int direct;
    protected String _direct;

    private int animMoveCount = 0;
    protected int point;

    public Enemy(double x, double y, Image img) {
        super(x, y, img);
        setNewDirect();
    }

    public Enemy(double x, double y, Image img, int speed) {
        super(x, y, img);
        this.speed = speed;
    }

    public String get_direct(int direct) {
        switch (direct % 4 + 1) {
            case 1: _direct = "left"; break;
            case 2: _direct = "right"; break;
            case 3: _direct = "down"; break;
            case 4: _direct = "up"; break;
        }
        return _direct;
    }

    protected void setImg(String type) {
        int frames = (20 - speed > 0)? (20 - speed) : 1;
        if(animMoveCount == 3 * frames) animMoveCount = 0;
        int k = animMoveCount++ / frames;
        try {
            switch (k % 3) {
                case 0: this.img = Sprite.sprite(type + "1").getFxImage(); return;
                case 1: this.img = Sprite.sprite(type + "2").getFxImage(); return;
                case 2: this.img = Sprite.sprite(type + "3").getFxImage(); return;
            }
        } catch (Exception e) {
            System.out.println("Error: setEnemyImg / Enemy.java");
        }
    }

    protected void resetDirect() {
        this.left = false;
        this.right = false;
        this.up = false;
        this.down = false;
    }

    public void setNewDirect() {
        this.resetDirect();
        direct = Math.abs(random.nextInt());
        switch (direct % 4 + 1) {
            case 1: this.left = true; break;
            case 2: this.right = true; break;
            case 3: this.down = true; break;
            case 4: this.up = true; break;
        }
    }

    protected void deadAnim() {
        int k = timeLeft / 20;
        if(k == 0) return;
        if(k == 4) this.img = Sprite.sprite(type + "_dead").getFxImage();
        else this.img = Sprite.sprite("mob_dead" + (4 - k)).getFxImage();
    }

    protected void deadHandle() {
        timeLeft--;
        deadAnim();
        if(timeLeft == 0) {
            BombermanGame.stillObjects.remove(this);
            BombermanGame.score += point;
        }
    }

    @Override
    public void update() { }

    @Override
    public void destroy() {
        BombermanGame.damagesObjects.remove(this);
        BombermanGame.stillObjects.add(this);
        dying = true;
    }
}
