package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

import java.util.List;

public class Bomber extends Entity {

    public int currentBombs = 0;
    private int animMoveCount = 0;
    private int deadAnim = 99; //~ 1.5s
    private boolean bombed = false;

    public boolean flamePass = false;
    private int flamePassTime = 625; // 10s

    public static boolean champion = false;
    private int championTime = 625; // 10s

    private boolean isAlive = true;

    public Bomber(double x, double y, Image img) { super( x, y, img); }

    public boolean isDead() {
        return (!isAlive);
    }

    public static boolean checkStillObject(Entity object) {
        List<Entity> entityList = BombermanGame.stillObjects;
        for (Entity entity : entityList) {
            if (Math.round(entity.x) == Math.round(object.x) && Math.round(entity.y) == Math.round(object.y)) {
                if (entity instanceof Wall) return true;
                if (entity instanceof Brick) {
                    return true;
                }
                /*if(entity instanceof Fire) {
                    return true;
                }*/
            }
        }
        /*if(object instanceof Bomber) {
            for(int i = 0; i < BombermanGame.entities.size() - 1; i++) {
                if (Math.round(BombermanGame.entities.get(i).x) == Math.round(object.x)
                        && Math.round(BombermanGame.entities.get(i).y) == Math.round(object.y)) {
                    if(BombermanGame.entities.get(i) instanceof Bomb) {
                        Bomb bomb = (Bomb) BombermanGame.entities.get(i);
                        if(Math.abs(object.x - bomb.getX()) < 0.4 && Math.abs(object.y - bomb.getY()) < 0.4) {
                            return false;
                        }
                        return true;
                    }
                }
            }
        }*/
        return false;
    }

    public static boolean checkCollisionBomb(Entity object, String type) {
        for(int i = 0; i < BombermanGame.entities.size() - 1; i++) {
            if(BombermanGame.entities.get(i) instanceof Bomb) {
                Bomb bomb = (Bomb) BombermanGame.entities.get(i);
                if(Math.abs(object.x - bomb.getX()) < 0.8 && Math.abs(object.y - bomb.getY()) < 0.8) {
                    return false;
                } else {

                }
            }
        }
        return false;
    }

    public static boolean checkInstanceDamages(Entity object) {
        for(int i = 0; i < BombermanGame.damagesObjects.size(); i++) {
            if((int) Math.round(object.x) == (int) Math.round(BombermanGame.damagesObjects.get(i).x)
                    && (int) Math.round(object.y) == (int) Math.round(BombermanGame.damagesObjects.get(i).y)) {
                if(object instanceof Bomber) {
                    if(BombermanGame.damagesObjects.get(i) instanceof Fire || BombermanGame.damagesObjects.get(i) instanceof Bomb) {
                        if(((Bomber) object).flamePass) return true;
                    }
                    object.destroy();
                } else if(object instanceof Enemy){
                    if((BombermanGame.damagesObjects.get(i) instanceof Fire || BombermanGame.damagesObjects.get(i) instanceof Bomb)) {
                        object.destroy();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void flamePassRestart() {
        flamePassTime = 625;
    }

    private void setPlayerImg(String type) {
        int frames = (9 - BombermanGame.speed > 0)? (9 - BombermanGame.speed) : 1;
        if(animMoveCount == 3 * frames) animMoveCount = 0;
        int k = animMoveCount++ / frames;
        switch (k % 3) {
            case 0:
                this.img = Sprite.sprite(type).getFxImage();
                return;
            case 1:
                this.img = Sprite.sprite(type + "_1").getFxImage();
                return;
            case 2:
                this.img = Sprite.sprite(type + "_2").getFxImage();
                return;
        }
    }

    public void deadAnimation() {
        //if(deadAnim == 0) BombermanGame.entities.remove(this);
        if(deadAnim > 0) {
            int k = deadAnim-- / 33;
            switch (k % 3) {
                case 2:
                    this.img = Sprite.player_dead1.getFxImage();
                    return;
                case 1:
                    this.img = Sprite.player_dead2.getFxImage();
                    return;
                case 0:
                    this.img = Sprite.player_dead3.getFxImage();
            }
        }
    }

    public void reborn() {
        isAlive = true;
    }

    @Override
    public void render(GraphicsContext gc) {
        /*SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        ImageView iv = new ImageView(img);
        Image base = iv.snapshot(params, null);*/
        gc.drawImage(img, x * Sprite.SCALED_SIZE, y * Sprite.SCALED_SIZE);
    }

    // version 1
    public static boolean checkInstanceStill_Bomber1(Entity object, String type) {
        int kx, ky;
        double _x, _y;
        boolean vertical = false;
        switch (type) {
            case "up":
                vertical = true;
                _x = object.x;
                _y = Math.ceil(object.y);
                kx = 0;
                ky = -1;
                break;
            case "down":
                vertical = true;
                _x = object.x;
                _y = Math.floor(object.y);
                kx = 0;
                ky = 1;
                break;
            case "left":
                //vertical = true;    // test
                _x = Math.ceil(object.x);
                _y = object.y;
                kx = -1;
                ky = 0;
                break;
            case "right":
                //vertical = true;    //test
                _x = Math.floor(object.x);
                _y = object.y;
                kx = 1;
                ky = 0;
                break;
            default:
                return false;
        }
        vertical = (vertical | (!checkStillObject(new Bomber(_x + kx - 0.3 * ky, _y + ky - 0.3 * kx, null)) && !checkStillObject(new Bomber(_x + kx + 0.3 * ky, _y + ky + 0.3 * kx, null))));
        boolean res = (!checkStillObject(new Bomber(_x + kx, _y + ky, null))
                && !checkStillObject(new Bomber(_x + kx - 0.1 * ky, _y + ky - 0.1 * kx, null)) && !checkStillObject(new Bomber(_x + kx + 0.1 * ky, _y + ky + 0.1 * kx, null))
                && !checkStillObject(new Bomber(_x + kx - 0.2 * ky, _y + ky - 0.2 * kx, null)) && !checkStillObject(new Bomber(_x + kx + 0.2 * ky, _y + ky + 0.2 * kx, null))
                && vertical);
                //&& !check(new Bomber(x + kx - 0.3 * ky, Math.ceil(y) + ky - 0.3 * kx, null)) && !check(new Bomber(x + kx + 0.3 * ky, Math.ceil(y) + ky + 0.3 * kx, null)));
        return res;
    }

    public static boolean checkInstanceStill(Entity object, String type) {
        boolean res = true;
        double space = (object instanceof Bomber)? 0.9 : 1.0;
        switch (type) {
            case "up":
                for(int i = 0; i < BombermanGame.stillObjects.size(); i++) {
                    Entity still = BombermanGame.stillObjects.get(i);
                    if(round1(Math.abs(still.x - round1(object.x))) < space
                            && round1(object.y) - still.y > 0.7 && round1(object.y - still.y) <= round1(space)) {
                        if(still instanceof Wall || still instanceof Brick) {
                            if (object instanceof Bomber)
                                System.out.println(still.toString() + " x: " + still.x + " , y: " + still.y);
                            res = false;
                        }
                        else if(still instanceof Bomb) {
                            res = false;
                        }
                    }
                }
                break;
            case "down":
                for(int i = 0; i < BombermanGame.stillObjects.size(); i++) {
                    Entity still = BombermanGame.stillObjects.get(i);
                    if(round1(Math.abs(still.x - round1(object.x))) < space
                            && still.y - round1(object.y) > 0.7 && round1(still.y - object.y) <= round1(space)) {
                        if(still instanceof Wall || still instanceof Brick) {
                            if(object instanceof Bomber)
                                System.out.println(still.toString() + " x: " + still.x + " , y: " + still.y);
                            res = false;
                        }
                        else if(still instanceof Bomb) {
                            res = false;
                        }
                    }
                }
                break;
            case "left":
                for(int i = 0; i < BombermanGame.stillObjects.size(); i++) {
                    Entity still = BombermanGame.stillObjects.get(i);
                    if(round1(object.x) - still.x > 0.7 && round1(object.x - still.x) <= round1(space)
                            && round1(Math.abs(still.y - round1(object.y))) < space) {
                        if(still instanceof Wall || still instanceof Brick) {
                            if(object instanceof Bomber) System.out.println(still.toString() + " x: " + still.x + " , y: " + still.y);
                            res = false;
                        }
                        else if(still instanceof Bomb) {
                            res = false;
                        }
                    }
                }
                break;
            case "right":
                for(int i = 0; i < BombermanGame.stillObjects.size(); i++) {
                    Entity still = BombermanGame.stillObjects.get(i);
                    if(still.x - round1(object.x) > 0.7 && round1(still.x - object.x) <= round1(space)
                            && round1(Math.abs(still.y - round1(object.y))) < space) {
                        if(still instanceof Wall || still instanceof Brick) {
                            if(object instanceof Bomber)
                                System.out.println(still.toString() + " x: " + still.x + " , y: " + still.y);
                            res = false;
                        }
                        else if(still instanceof Bomb) {
                            res = false;
                        }
                    }
                }
                break;
            default:
                break;
        }
        if(object instanceof Bomber) System.out.println(type);
        return res;
    }

    @Override
    public void update() {
        if(bombed) {
            deadAnimation();
            if(deadAnim == 0) isAlive = false;
            return;
        }
        if(BombermanGame.input.up) {
            if(y > 1/* + (BombermanGame.speed - 1) * 0.05*/) {
                if(checkInstanceStill(this ,"up")) {
                    this.y = y - 0.05 - 0.025 * (BombermanGame.speed - 1);
                    if(y < 1) y = 1;
                }
            }
            setPlayerImg("player_up");
            System.out.println("Bomber: x: " + (double) Math.round(x * 10) / 10 + ", y = " + (double) Math.round(y * 10) / 10);
        }
        if(BombermanGame.input.down) {
            if(y < BombermanGame.HEIGHT - 2 /*- (BombermanGame.speed - 1) * 0.05*/) {
                if(checkInstanceStill(this, "down")) {
                    this.y = this.y + 0.05 + 0.025 * (BombermanGame.speed - 1);
                    if(y > BombermanGame.HEIGHT - 2) y = BombermanGame.HEIGHT - 2;
                }
            }
            setPlayerImg("player_down");
            System.out.println("Bomber: x: " + (double) Math.round(x * 10) / 10 + ", y = " + (double) Math.round(y * 10) / 10);
        }
        if(BombermanGame.input.left) {
            if(x > 1/* + (BombermanGame.speed - 1) * 0.05*/) {
                if(checkInstanceStill(this, "left")) {
                    this.x = this.x - 0.05 - 0.025 * (BombermanGame.speed - 1);
                    if(x < 1) x = 1;
                }
            }
            setPlayerImg("player_left");
            System.out.println("Bomber: x: " + (double) Math.round(x * 10) / 10 + ", y = " + (double) Math.round(y * 10) / 10);
        }
        if(BombermanGame.input.right) {
            if(x < BombermanGame.WIDTH - 2 /*- (BombermanGame.speed - 1) * 0.05*/) {
                if(checkInstanceStill(this, "right")) {
                    this.x = this.x + 0.05 + 0.025 * (BombermanGame.speed - 1);
                    if(x > BombermanGame.WIDTH - 2) x = BombermanGame.WIDTH - 2;
                }
            }
            setPlayerImg("player_right");
            System.out.println("Bomber: x: " + (double) Math.round(x * 10) / 10 + ", y = " + (double) Math.round(y * 10) / 10);
        }
        if(BombermanGame.input.space) {
            if(currentBombs == BombermanGame.bombs) return; // max of bombs
            if(!(Math.abs(x - (int)x - 0.5) <= 0.075 || Math.abs(y - (int)y - 0.5) <= 0.075)) {
                int bomb_x = (int) Math.round(x);
                int bomb_y = (int) Math.round(y);
                Bomb bomb = new Bomb(bomb_x, bomb_y, Sprite.bomb.getFxImage());
                if(!Bomb.checkInstanceBomb(bomb)) {
                    BombermanGame.stillObjects.add(bomb);
                    currentBombs++;
                }
            }
        }
        if(flamePass) {
            flamePassTime--;
            if((double) flamePassTime / 62 == flamePassTime / 62) {
                System.out.println(flamePassTime / 62);
            }
            if(flamePassTime == 0) {
                flamePass = false;
                flamePassTime = 625;
                System.out.println("Flame-pass expired");
            }
        }
        if(!champion) checkInstanceDamages(BombermanGame.getBomber());
        else {
            championTime--;
            if(championTime == 0) {
                champion = false;
            }
        }
    }

    public void restart() {
        this.x = 1;
        this.y = 1;
    }

    @Override
    public void destroy() {
        BombermanGame.life -= 1;
        if(BombermanGame.life == 0) {
            bombed = true;
            return;
        }
        BombermanGame.getBomber().restart();
    }

}
