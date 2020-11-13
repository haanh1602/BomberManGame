package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

import java.util.List;

public class Bomber extends Entity {

    public static int currentBombs = 0;
    private int animMoveCount = 0;
    private int deadAnim = 99; //~ 1.5s
    private boolean bombed = false;

    public boolean flamePass = false;
    private int flamePassTime = 625; // 10s

    public static boolean champion = false;
    private int championTime = 625; // 10s

    private boolean isAlive = true;

    public Bomber(double x, double y, Image img) {
        super( x, y, img);
    }

    public boolean isDead() {
        return (!isAlive);
    }

    public static boolean checkStillObject(Bomber object) {
        List<Entity> entityList = BombermanGame.stillObjects;
        for (Entity entity : entityList) {
            if (Math.round(entity.x) == Math.round(object.x) && Math.round(entity.y) == Math.round(object.y)) {
                if (entity instanceof Wall) return true;
                if (entity instanceof Brick) {
                    return true;
                }
                if(entity instanceof Fire) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean checkCollisionBomb(Entity object, String direction) {
        for(int i = 0; i < BombermanGame.entities.size() - 1; i++) {
            if(BombermanGame.entities.get(i) instanceof Bomb) {
                Bomb bomb = (Bomb) BombermanGame.entities.get(i);
                if(Math.abs(object.x - bomb.getX()) < 0.4 && Math.abs(object.y - bomb.getY()) < 0.4) {
                    return false;
                }
                else {

                }
            }
        }
        return true;
    }

    public static void checkInstanceDamages(Entity object) {
        for(int i = 0; i < BombermanGame.damagesObjects.size(); i++) {
            if((int) Math.round(object.x) == (int) Math.round(BombermanGame.damagesObjects.get(i).x)
                    && (int) Math.round(object.y) == (int) Math.round(BombermanGame.damagesObjects.get(i).y)) {
                if(object instanceof Bomber) {
                    if(BombermanGame.damagesObjects.get(i) instanceof Fire || BombermanGame.damagesObjects.get(i) instanceof Bomb) {
                        if(((Bomber) object).flamePass) return;
                    }
                    object.destroy();
                }
                return;
            }
        }
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

    private boolean checkInstanceStill_Bomber(String type) {
        int kx, ky;
        double _x, _y;
        boolean vertical = false;
        switch (type) {
            case "up":
                vertical = true;
                _x = x;
                _y = Math.ceil(y);
                kx = 0;
                ky = -1;
                break;
            case "down":
                vertical = true;
                _x = x;
                _y = Math.floor(y);
                kx = 0;
                ky = 1;
                break;
            case "left":
                //vertical = true;    // test
                _x = Math.ceil(x);
                _y = y;
                kx = -1;
                ky = 0;
                break;
            case "right":
                //vertical = true;    //test
                _x = Math.floor(x);
                _y = y;
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

    @Override
    public void update() {
        if(bombed) {
            deadAnimation();
            if(deadAnim == 0) isAlive = false;
            return;
        }
        if(BombermanGame.input.up) {
            if(y > 1/* + (BombermanGame.speed - 1) * 0.05*/) {
                if(checkInstanceStill_Bomber("up")) {
                    this.y = y - 0.05 - 0.025 * (BombermanGame.speed - 1);
                    if(y < 1) y = 1;
                }
                /*if(!check(new Bomber(x, Math.ceil(y) - 1, null))
                        && !check(new Bomber(x - 0.1, Math.ceil(y) - 1, null)) && !check(new Bomber(x + 0.1, Math.ceil(y) - 1, null))
                        && !check(new Bomber(x - 0.2, Math.ceil(y) - 1, null)) && !check(new Bomber(x + 0.2, Math.ceil(y) - 1, null)))
                        //&& !check(new Bomber(x - 0.3, Math.ceil(y) - 1, null)) && !check(new Bomber(x + 0.3, Math.ceil(y) - 1, null)))
                        //&& !check(new Bomber(x - 0.4, Math.ceil(y) - 1, null)) && !check(new Bomber(x + 0.4, Math.ceil(y) - 1, null)))
                    this.y = y - 0.05 * BombermanGame.speed;
                if(y < 1) y = 1;*/
            }
            setPlayerImg("player_up");
        }
        if(BombermanGame.input.down) {
            if(y < BombermanGame.HEIGHT - 2 /*- (BombermanGame.speed - 1) * 0.05*/) {
                if(checkInstanceStill_Bomber("down")) {
                    this.y = this.y + 0.05 + 0.025 * (BombermanGame.speed - 1);
                    if(y > BombermanGame.HEIGHT - 2) y = BombermanGame.HEIGHT - 2;
                }
                /*if(!check(new Bomber(x, Math.floor(y) + 1, null))
                        && !check(new Bomber(x - 0.1, Math.floor(y) + 1, null)) && !check(new Bomber(x + 0.1, Math.floor(y) + 1, null))
                        && !check(new Bomber(x - 0.2, Math.floor(y) + 1, null)) && !check(new Bomber(x + 0.2, Math.floor(y) + 1, null)))
                        //&& !check(new Bomber(x - 0.3, Math.floor(y) + 1, null)) && !check(new Bomber(x + 0.3, Math.floor(y) + 1, null)))
                    this.y = this.y + 0.05 * BombermanGame.speed;
                if(y > BombermanGame.HEIGHT - 2) y = BombermanGame.HEIGHT - 2;*/
            }
            setPlayerImg("player_down");
        }
        if(BombermanGame.input.left) {
            if(x > 1/* + (BombermanGame.speed - 1) * 0.05*/) {
                if(checkInstanceStill_Bomber("left")) {
                    this.x = this.x - 0.05 - 0.025 * (BombermanGame.speed - 1);
                    if(x < 1) x = 1;
                }
                /*if(!check(new Bomber(Math.ceil(x) - 1, y, null))
                        && !check(new Bomber(Math.ceil(x) - 1, y - 0.1, null)) && !check(new Bomber(Math.ceil(x) - 1, y + 0.1, null))
                        && !check(new Bomber(Math.ceil(x) - 1, y - 0.2, null)) && !check(new Bomber(Math.ceil(x) - 1, y + 0.2, null))
                        && !check(new Bomber(Math.ceil(x) - 1, y - 0.3, null)) && !check(new Bomber(Math.ceil(x) - 1, y + 0.3, null)))
                this.x = this.x - 0.05 * BombermanGame.speed;
                if(x < 1) x = 1;*/
            }
            setPlayerImg("player_left");
        }
        if(BombermanGame.input.right) {
            if(x < BombermanGame.WIDTH - 2 /*- (BombermanGame.speed - 1) * 0.05*/) {
                if(checkInstanceStill_Bomber("right")) {
                    this.x = this.x + 0.05 + 0.025 * (BombermanGame.speed - 1);
                    if(x > BombermanGame.WIDTH - 2) x = BombermanGame.WIDTH - 2;
                }
                /*if(!check(new Bomber(Math.floor(x) + 1, y, null))
                        && !check(new Bomber(Math.floor(x) + 1, y - 0.1, null)) && !check(new Bomber(Math.floor(x) + 1, y + 0.1, null))
                        && !check(new Bomber(Math.floor(x) + 1, y - 0.2, null)) && !check(new Bomber(Math.floor(x) + 1, y + 0.2, null))
                        && !check(new Bomber(Math.floor(x) + 1, y - 0.3, null)) && !check(new Bomber(Math.floor(x) + 1, y + 0.3, null)))
                this.x = this.x + 0.05 * BombermanGame.speed;
                if(x > BombermanGame.WIDTH - 2) x = BombermanGame.WIDTH - 2;*/
            }
            setPlayerImg("player_right");
        }
        if(BombermanGame.input.space) {
            if(currentBombs == BombermanGame.bombs) return; // max of bombs
            if(Math.abs(x - (int)x - 0.5) <= 0.1 || Math.abs(y - (int)y - 0.5) <= 0.1) {
                return;
            }
            int bomb_x = (int) Math.round(x);
            int bomb_y = (int) Math.round(y);
            Bomb bomb = new Bomb(bomb_x, bomb_y, Sprite.bomb.getFxImage());
            if(!Bomb.checkInstanceBomb(bomb)) {
                BombermanGame.entities.add(BombermanGame.entities.size() - 1, bomb);
                //BombermanGame.stillObjects.add(new Fire(bomb_x, bomb_y, Sprite.grass.getFxImage()));
                currentBombs++;
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
        checkInstanceDamages(BombermanGame.entities.get(BombermanGame.entities.size() - 1));
    }

    @Override
    public void destroy() {
        bombed = true;
    }

}
