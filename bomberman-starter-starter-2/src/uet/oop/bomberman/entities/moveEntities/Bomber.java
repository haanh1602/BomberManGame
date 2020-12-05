package uet.oop.bomberman.entities.moveEntities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.util.Duration;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.stillEntities.mortal.Bomb;
import uet.oop.bomberman.entities.stillEntities.immortal.Wall;
import uet.oop.bomberman.entities.stillEntities.mortal.item.BombsItem;
import uet.oop.bomberman.entities.stillEntities.mortal.item.InvincibilityItem;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.sound.Sound;
import uet.oop.bomberman.sound.SoundController;

public class Bomber extends MoveEntities {
    public int currentBombs = 0;
    private int animMoveCount = 0;
    private int deadAnim = 99; //~ 1.5s
    private boolean bombed = false;

    public boolean flamePass = false;
    public int flamePassTime = 625; // 10s

    public boolean invincibility = false;
    public int invincibilityTime = 3000/16;

    private boolean isAlive = true;

    public Bomber(double x, double y, Image img) {
        super( x, y, img);
        BombermanGame.sound.repeat(BombermanGame.sound.walking);
    }

    public boolean isDead() {
        return (!isAlive);
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

    private void deadAnimation() {
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
        flamePass = false;
        flamePassTime = 10000/16;
        invincibility = false;
        invincibilityTime = 3000/16;
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

    @Override
    public void update() {
        if(bombed) {
            deadAnimation();
            if(deadAnim == 0) isAlive = false;
            return;
        }
        if(!(BombermanGame.input.up) && !(BombermanGame.input.down) && !(BombermanGame.input.left) && !(BombermanGame.input.right) || !BombermanGame.getBomber().isAlive) {
            BombermanGame.sound.walking.stop();
        } else BombermanGame.sound.walking.play();
        if(BombermanGame.input.up) {
            if(y > 1/* + (BombermanGame.speed - 1) * 0.05*/) {
                if(checkInstanceStill(this ,"up")) {
                    this.y = y - 0.05 - 0.025 * (BombermanGame.speed - 1);
                    if(y < 1) y = 1;
                }
            }
            setPlayerImg("player_up");
            //System.out.println("Bomber: x: " + (double) Math.round(x * 10) / 10 + ", y = " + (double) Math.round(y * 10) / 10);
        }
        if(BombermanGame.input.down) {
            if(y < BombermanGame.HEIGHT - 2 /*- (BombermanGame.speed - 1) * 0.05*/) {
                if(checkInstanceStill(this, "down")) {
                    this.y = this.y + 0.05 + 0.025 * (BombermanGame.speed - 1);
                    if(y > BombermanGame.HEIGHT - 2) y = BombermanGame.HEIGHT - 2;
                }
            }
            setPlayerImg("player_down");
            //System.out.println("Bomber: x: " + (double) Math.round(x * 10) / 10 + ", y = " + (double) Math.round(y * 10) / 10);
        }
        if(BombermanGame.input.left) {
            if(x > 1/* + (BombermanGame.speed - 1) * 0.05*/) {
                if(checkInstanceStill(this, "left")) {
                    this.x = this.x - 0.05 - 0.025 * (BombermanGame.speed - 1);
                    if(x < 1) x = 1;
                }
            }
            setPlayerImg("player_left");
            //System.out.println("Bomber: x: " + (double) Math.round(x * 10) / 10 + ", y = " + (double) Math.round(y * 10) / 10);
        }
        if(BombermanGame.input.right) {
            if(x < BombermanGame.WIDTH - 2 /*- (BombermanGame.speed - 1) * 0.05*/) {
                if(checkInstanceStill(this, "right")) {
                    this.x = this.x + 0.05 + 0.025 * (BombermanGame.speed - 1);
                    if(x > BombermanGame.WIDTH - 2) x = BombermanGame.WIDTH - 2;
                }
            }
            setPlayerImg("player_right");
            //System.out.println("Bomber: x: " + (double) Math.round(x * 10) / 10 + ", y = " + (double) Math.round(y * 10) / 10);
        }
        if(BombermanGame.input.space) {
            if(currentBombs == BombermanGame.bombs) return; // max of bombs
            if(!(Math.abs(x - (int)x - 0.5) <= 0.05 || Math.abs(y - (int)y - 0.5) <= 0.05)) {
                int bomb_x = (int) Math.round(x);
                int bomb_y = (int) Math.round(y);
                if(!Bomb.checkInstanceBomb(new Wall(bomb_x, bomb_y, null))) {
                    Bomb bomb = new Bomb(bomb_x, bomb_y, Sprite.bomb.getFxImage());
                    BombermanGame.stillObjects.add(bomb);
                    currentBombs++;
                }
            }
        }
        if(flamePass) {
            flamePassTime--;
//            if((double) flamePassTime / 62 == flamePassTime / 62) {
//                System.out.println(flamePassTime / 62);
//            }
            if(flamePassTime == 0) {
                flamePass = false;
                flamePassTime = 625;
                System.out.println("Flame-pass expired!");
            }
        }
        if(!invincibility) checkInstanceDamages(BombermanGame.getBomber());
        else {
            invincibilityTime--;
//            if(invincibilityTime % (1000/16) == 0) {
//                System.out.println(invincibilityTime / (1000/16));
//            }
            if(invincibilityTime == 0) {
                invincibility = false;
                invincibilityTime = 3000/16;
                System.out.println("Invincibility expired!");
            }
        }
    }

    public void restart() {
        this.x = 1;
        this.y = 1;
        new InvincibilityItem(x, y, null, 3000/16);
    }

    @Override
    public void destroy() {
        if(!bombed && !this.invincibility) {
            BombermanGame.life -= 1;
            if(BombermanGame.life == 0) {
                bombed = true;
                BombermanGame.sound.stopAll();
                BombermanGame.sound.death.play();
                return;
            }
            //new Sound().getDamage.play();
            BombermanGame.sound.playSound("Get_Damage.mp3");
            BombermanGame.getBomber().restart();
        }
    }
}
