package uet.oop.bomberman.command;


import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.graphics.Sprite;

public class KeyInput {
    public boolean up = false;
    public boolean down = false;
    public boolean left = false;
    public boolean right = false;
    public boolean space = false;
    public boolean pause = false;

    public boolean admin = false;
    private String adminProcedure = "";

    public void adminProcedureHandle(char c) {
        adminProcedure += c;
        if(adminProcedure.length() == 3) {
            switch (adminProcedure) {
                case "apf":
                    BombermanGame.flame++;
                    System.out.println("Power up flame: " + BombermanGame.flame);
                    break;
                case "aps":
                    if(!(BombermanGame.speed == BombermanGame.MAX_SPEED)) {
                        BombermanGame.speed++;
                        System.out.println("Power up speed: " + BombermanGame.speed);
                    } else {
                        System.out.println("Speed is too fast! (6)");
                    }
                    break;
                case "apb":
                    BombermanGame.bombs++;
                    System.out.println("Power up bombs: " + BombermanGame.bombs);
                    break;
                case "ars":
                    resetBomberStatus();
                    System.out.println("Bomber is reset");
                    break;
                case "acb":
                    for(int i = 0; i < BombermanGame.stillObjects.size(); i++) {
                        if(BombermanGame.stillObjects.get(i) instanceof Brick) {
                            Brick temp = (Brick) BombermanGame.stillObjects.get(i);
                            BombermanGame.stillObjects.remove(temp);
                            BombermanGame.stillObjects.add(0,
                                    new Grass((int) Math.round(temp.getX()), (int) Math.round(temp.getY()), Sprite.grass.getFxImage()));
                        }
                    }
                    System.out.println("Bricks are cleared");
                    break;
                case "acp":
                    System.out.println("Bomber being champion...");
                    Bomber.champion = true;
                    break;
                case "afp":
                    if(BombermanGame.entities.get(BombermanGame.entities.size() - 1) instanceof Bomber) {
                        if(BombermanGame.getBomber().flamePass) {
                            BombermanGame.getBomber().flamePassRestart();
                        } else {
                            try {
                                BombermanGame.getBomber().flamePass = true;
                                BombermanGame.effects.add(
                                        new Effect(BombermanGame.getBomber().getX(), BombermanGame.getBomber().getY(), null, "FlamePass"));
                            } catch (NullPointerException nullPointerException) {
                                System.out.println("Bomber cannot found!");
                            }
                        }
                        System.out.println("Flame-pass mode...");
                    }
                    break;
            }
            adminProcedure = "";
            admin = false;
        }
    }

    public void resetAdminProcedure() {
        this.adminProcedure = "";
    }

    public void resetBomberStatus() {
        BombermanGame.flame = 1;
        BombermanGame.speed = 1;
        BombermanGame.bombs = 1;
    }
}
