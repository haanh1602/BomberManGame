package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;

public class Effect extends Entity{

    private int animCount = 0;
    private String type;

    //private boolean test = false;

    public Effect(double x, double y, Image img, String type) {
        super(x, y, img);
        this.type = type;
        try {
            this.img = new Image("sprites/" + type + "1.png");
        }
        catch (Exception e) {
            System.out.println("Cannot load " + type + " image(s)");
        }
    }

    /*public Effect(double x, double y, Image img, String type, boolean testAnim) {
        super(x, y, img);
        this.type = type;
        this.test = testAnim;
        try {
            this.img = new Image("sprites/" + type + "1.png");
        }
        catch (Exception e) {
            System.out.println("Cannot load " + type + " image(s)");
        }
    }*/

    private void effectAnim() {
        int k = animCount++ / 10;
        this.img = new Image("sprites/" + type + (k % 3 + 1) + ".png");
        //System.out.println("sprites/" + type + (k%2 + 1) + ".png");
        if(animCount == 99900) animCount = 0;
    }

    @Override
    public void update() {
        switch (type) {
            case "FlamePass":
                if(BombermanGame.getBomber().flamePass /*|| test*/) {
                    effectAnim();
                    //if(test) return;
                    x = BombermanGame.getBomber().x;
                    y = BombermanGame.getBomber().y;
                } else {
                    destroy();
                }
                break;
            case "Champion":
                if(BombermanGame.getBomber().champion) {
                    effectAnim();
                    x = BombermanGame.getBomber().x;
                    y = BombermanGame.getBomber().y;
                } else {
                    destroy();
                }
                break;
        }
    }

    @Override
    public void destroy() {
        BombermanGame.effects.remove(this);
    }
}
