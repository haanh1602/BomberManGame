package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.command.KeyInput;
import uet.oop.bomberman.graphics.Sprite;

import java.util.List;

public class Bomber extends Entity {
    public static int flame = 1;
    public static int speed = 1;
    public static int bombs = 1;
    public static int currentBombs = 0;
    public static int life = 3;
    private int animMoveCount = 0;
    private KeyInput input = new KeyInput();

    private boolean isAlive = true;

    public Bomber(double x, double y, Image img) {
        super( x, y, img);
    }

    public boolean isDead() {
        return (!isAlive);
    }

    public void destroy() {
        isAlive = false;
    }

    public static boolean check(Bomber object) {
        List<Entity> entityList = BombermanGame.stillObjects;
        for(int i = 0; i < entityList.size(); i++) {
            if(Math.round(entityList.get(i).x) == Math.round(object.x) && Math.round(entityList.get(i).y) == Math.round(object.y)) {
                if(entityList.get(i) instanceof Wall) return true;
                if(entityList.get(i) instanceof Brick) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void render(GraphicsContext gc) {
        /*SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        ImageView iv = new ImageView(img);
        Image base = iv.snapshot(params, null);*/

        gc.drawImage(img, x * Sprite.SCALED_SIZE, y * Sprite.SCALED_SIZE);
    }

    private boolean checkInstanceStill_Bomber(String type) {
        int kx, ky;
        switch (type) {
            case "up":
                kx = 0;
                ky = -1;
                break;
            case "down":
                kx = 0;
                ky = 1;
                break;
            case "left":
                kx = -1;
                ky = 0;
                break;
            case "right":
                kx = 1;
                ky = 0;
                break;
            default:
                return false;
        }
        boolean res = (!check(new Bomber(x + kx, Math.ceil(y) + ky, null))
                && !check(new Bomber(x + kx - 0.1 * ky, Math.ceil(y) + ky - 0.1 * kx, null)) && !check(new Bomber(x + kx + 0.1 * ky, Math.ceil(y) + ky + 0.1 * kx, null))
                && !check(new Bomber(x + kx - 0.2 * ky, Math.ceil(y) + ky - 0.2 * kx, null)) && !check(new Bomber(x + kx + 0.2 * ky, Math.ceil(y) + ky + 0.2 * kx, null))
                && !check(new Bomber(x + kx - 0.3 * ky, Math.ceil(y) + ky - 0.3 * kx, null)) && !check(new Bomber(x + kx + 0.3 * ky, Math.ceil(y) + ky + 0.3 * kx, null)));
        return res;
    }

    @Override
    public void update() {
        if(BombermanGame.input.up) {
            if(y > 1 + (Bomber.speed - 1) * 0.05) {
                if(!check(new Bomber(x, Math.ceil(y) - 1, null))
                        && !check(new Bomber(x - 0.1, Math.ceil(y) - 1, null)) && !check(new Bomber(x + 0.1, Math.ceil(y) - 1, null))
                        && !check(new Bomber(x - 0.2, Math.ceil(y) - 1, null)) && !check(new Bomber(x + 0.2, Math.ceil(y) - 1, null)))
                        //&& !check(new Bomber(x - 0.3, Math.ceil(y) - 1, null)) && !check(new Bomber(x + 0.3, Math.ceil(y) - 1, null)))
                        //&& !check(new Bomber(x - 0.4, Math.ceil(y) - 1, null)) && !check(new Bomber(x + 0.4, Math.ceil(y) - 1, null)))
                    this.y = y - 0.05 * Bomber.speed;
            }
            setPlayerImg("player_up");
        }
        if(BombermanGame.input.down) {
            if(y < BombermanGame.HEIGHT - 2 - (Bomber.speed - 1) * 0.05) {
                if(!check(new Bomber(x, Math.floor(y) + 1, null))
                        && !check(new Bomber(x - 0.1, Math.floor(y) + 1, null)) && !check(new Bomber(x + 0.1, Math.floor(y) + 1, null))
                        && !check(new Bomber(x - 0.2, Math.floor(y) + 1, null)) && !check(new Bomber(x + 0.2, Math.floor(y) + 1, null)))
                        //&& !check(new Bomber(x - 0.3, Math.floor(y) + 1, null)) && !check(new Bomber(x + 0.3, Math.floor(y) + 1, null)))
                    this.y = this.y + 0.05 * Bomber.speed;
            }
            setPlayerImg("player_down");
        }
        if(BombermanGame.input.left) {
            if(x > 1 + (Bomber.speed - 1) * 0.05) {
                if(!check(new Bomber(Math.ceil(x) - 1, y, null))
                        && !check(new Bomber(Math.ceil(x) - 1, y - 0.1, null)) && !check(new Bomber(Math.ceil(x) - 1, y + 0.1, null))
                        && !check(new Bomber(Math.ceil(x) - 1, y - 0.2, null)) && !check(new Bomber(Math.ceil(x) - 1, y + 0.2, null))
                        && !check(new Bomber(Math.ceil(x) - 1, y - 0.3, null)) && !check(new Bomber(Math.ceil(x) - 1, y + 0.3, null)))
                this.x = this.x - 0.05 * Bomber.speed;
            }
            setPlayerImg("player_left");
        }
        if(BombermanGame.input.right) {
            if(x < BombermanGame.WIDTH - 1.8 - (Bomber.speed - 1) * 0.05) {
                if(!check(new Bomber(Math.floor(x) + 1, y, null))
                        && !check(new Bomber(Math.floor(x) + 1, y - 0.1, null)) && !check(new Bomber(Math.floor(x) + 1, y + 0.1, null))
                        && !check(new Bomber(Math.floor(x) + 1, y - 0.2, null)) && !check(new Bomber(Math.floor(x) + 1, y + 0.2, null))
                        && !check(new Bomber(Math.floor(x) + 1, y - 0.3, null)) && !check(new Bomber(Math.floor(x) + 1, y + 0.3, null)))
                this.x = this.x + 0.05 * Bomber.speed;
            }
            setPlayerImg("player_right");
        }
        if(BombermanGame.input.space) {
            if(currentBombs == bombs) return;
            if(Math.abs(x - (int)x - 0.5) <= 0.1 || Math.abs(y - (int)y - 0.5) <= 0.1) {
                return;
            }
            int bomb_x = (int) Math.round(x);
            int bomb_y = (int) Math.round(y);
            Bomb bomb = new Bomb(bomb_x, bomb_y, Sprite.bomb.getFxImage());
            if(!Bomb.checkInstanceBomb(bomb)) {
                BombermanGame.entities.add(0, bomb);
                currentBombs++;
            }
        }
    }

    private void setPlayerImg(String type) {
        int frames = (9 - speed*2 > 0)? (9 - speed*2) : 1;
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

}
