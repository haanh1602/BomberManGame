package uet.oop.bomberman;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import uet.oop.bomberman.command.KeyInput;
import uet.oop.bomberman.command.Message;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.graphics.Sprite;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

//import javafx.scene.canvas.Canvas;

public class BombermanGame extends Application {
    
    public static int WIDTH = 31;
    public static int HEIGHT = 13;
    
    private GraphicsContext gc;
    private Canvas canvas;
    private String level;
    public static List<Entity> entities = new ArrayList<>();
    public static List<Entity> stillObjects = new ArrayList<>();
    public static List<Entity> damagesObjects = new ArrayList<>();
    public static List<Effect> effects = new ArrayList<>();
    public static KeyInput input = new KeyInput();

    // Bomber status
    public static int life = 3;
    public static int flame = 1;
    public static int speed = 1;
    public static final int MAX_SPEED = 6;
    public static int bombs = 1;

    private Image preImg;

    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage stage) {
        // Mac dinh level dau tien
        this.level = "1";

        // Tao Canvas
        createMap();
        canvas = new Canvas(Sprite.SCALED_SIZE * WIDTH, Sprite.SCALED_SIZE * (HEIGHT + 1));
        canvas.setStyle("-fx-background-color: black");
        gc = canvas.getGraphicsContext2D();

        // Tao root container
        Group root = new Group();

        root.getChildren().add(canvas); // index 0

        // Tao scene
        Scene scene = new Scene(root);

        // Them scene vao stage
        stage.setScene(scene);
        stage.setTitle("Bomberman_AK47");
        Image icon = new Image("/sprites/bomberman_icon.png");
        stage.getIcons().add(icon);
        stage.show();

        Entity bomberman = new Bomber(1, 1, Sprite.player_right.getFxImage());
        entities.add(bomberman);

        //Test effect animation

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                render();
                scene.setOnKeyPressed(event -> {
                    switch (event.getCode()) {
                        case UP:
                            input.up = true;
                            input.admin = false; break;
                        case DOWN:
                            input.down = true;
                            input.admin = false; break;
                        case LEFT:
                            input.left = true;
                            input.admin = false; break;
                        case RIGHT:
                            input.right = true;
                            input.admin = false; break;
                        case SPACE:
                            input.admin = false;
                            if(input.space) {
                                input.space = false;
                                break;
                            }
                            input.space = true; break;
                        case SHIFT:
                            if(input.pause) {
                                input.pause = false;
                                break;
                            }
                            input.pause = true; break;
                        default:
                            char key = Character.toLowerCase(event.getText().charAt(0));
                            System.out.println(key);
                            if(key == 'a') {
                                input.admin = true;
                                input.resetAdminProcedure();
                            }
                            if(input.admin) input.adminProcedureHandle(key);
                    }
                });
                scene.setOnKeyReleased(event -> {
                    switch (event.getCode()) {
                        case UP:
                            input.up = false; break;
                        case DOWN:
                            input.down = false; break;
                        case LEFT:
                            input.left = false; break;
                        case RIGHT:
                            input.right = false; break;
                        case SPACE:
                            input.space = false; break;
                    }
                });
                if(((Bomber) entities.get(entities.size() - 1)).isDead()) {
                    //((Bomber) entities.get(entities.size() - 1)).reborn();
                    scene.setOnKeyPressed(event->{
                        if(event.getCode() == KeyCode.X) {
                            System.exit(0);
                        }
                    });
                    root.getChildren().add(Message.lose());
                    root.getChildren().get(1).setVisible(true);
                    input.pause = true;
                } else {
                    if(!input.pause) {
                        update();
                        if(root.getChildren().size() > 1) root.getChildren().remove(1);

                    }
                    else {
                    /*if(((Bomber) entities.get(entities.size() - 1)).isDead()) {
                        ((Bomber) entities.get(entities.size() - 1)).reborn();
                        root.getChildren().add(Message.lose());
                        //root.getChildren().get(2).setVisible(true);
                        //input.pause = true;
                    }
                    else*/ root.getChildren().add(Message.pause());
                        root.getChildren().get(1).setVisible(true);
                    }
                }

            }

        };
        timer.start();
    }

    private void createMap() {
        String levelFilePath = new File("").getAbsolutePath() + "/res/levels/Level" + level + ".txt";
        try {
            FileReader fr = new FileReader(new File(levelFilePath));
            BufferedReader br = new BufferedReader(fr);

            int Level, Row, Column;
            String[] levelInfo = br.readLine().split(" ");
            if(levelInfo.length >= 3) {
                Level = Integer.parseInt(levelInfo[0]);
                Row = Integer.parseInt(levelInfo[1]);
                Column = Integer.parseInt(levelInfo[2]);
                WIDTH = Column;
                HEIGHT = Row;
                this.level = String.valueOf(Level);
            } else {
                System.out.println("Cannot read level info!");
            }
            for (int i = 0; i < HEIGHT; i++) {
                String line = br.readLine();
                for (int j = 0; j < WIDTH; j++) {
                    boolean item = false;
                    Entity object;
                    switch (line.charAt(j)) {
                        case '#':
                            object = new Wall(j, i, Sprite.wall.getFxImage());
                            break;
                        case '*':
                            object = new Brick(j, i, Sprite.brick.getFxImage());
                            break;
                        case 'b':
                            object = new Item(j, i, null).Bombs();
                            stillObjects.add(0, object);
                            stillObjects.add(new Brick(j, i, Sprite.brick.getFxImage()));
                            item = true;
                            break;
                        case 'f':
                            object = new Item(j, i, null).Flame();
                            stillObjects.add(0, object);
                            stillObjects.add(new Brick(j, i, Sprite.brick.getFxImage()));
                            item = true;
                            break;
                        case 's':
                            object = new Item(j, i, null).Speed();
                            stillObjects.add(0, object);
                            stillObjects.add(new Brick(j, i, Sprite.brick.getFxImage()));
                            item = true;
                            break;
                        case 'l':
                            object = new Item(j, i, null).FlamePass();
                            stillObjects.add(0, object);
                            stillObjects.add(new Brick(j, i, Sprite.brick.getFxImage()));
                            item = true;
                            break;
                        default:
                            object = new Grass(j, i, Sprite.grass.getFxImage());
                            break;
                    }
                    if(!item) stillObjects.add(object);
                }
            }

            br.close();
            fr.close();
        } catch (Exception e) {
            System.out.println("File error: Cannot found " + levelFilePath);
            e.getMessage();
        }
    }

    /*private void setChampionAnim() {
        if(champion) {
            if(championTime == 0) champion = false;
            else {
                int k = championTime-- / 25;
                if(k % 2 == 0) {
                    preImg = entities.get(entities.size() - 1).getImg();
                    entities.get(entities.size() - 1).setImg(null);
                }
                else {
                    entities.get(entities.size() - 1).setImg(preImg);
                }
            }
        }
    }*/

    public void update() {
        //_input.update();
        for(int i = 0; i < entities.size(); i++) { entities.get(i).update(); }
        for(int i = 0; i < effects.size(); i++) { effects.get(i).update(); }
        for(int i = 0; i < damagesObjects.size(); i++) { damagesObjects.get(i).update(); }
        for(int i = 0; i < stillObjects.size(); i++) { stillObjects.get(i).update(); }
    }

    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        stillObjects.forEach(g -> g.render(gc));
        damagesObjects.forEach(g -> g.render(gc));
        entities.forEach(g -> g.render(gc));
        effects.forEach(g -> g.render(gc));
    }

    public static Bomber getBomber() {
        if(BombermanGame.entities.get(BombermanGame.entities.size() - 1) instanceof Bomber) {
            return (Bomber) BombermanGame.entities.get(BombermanGame.entities.size() - 1);
        } else {
            return null;
        }
    }
}
