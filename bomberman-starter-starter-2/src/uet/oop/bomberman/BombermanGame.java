package uet.oop.bomberman;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
//import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import uet.oop.bomberman.command.KeyInput;
import uet.oop.bomberman.command.Message;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.graphics.Sprite;

import java.awt.*;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;

public class BombermanGame extends Application {
    
    public static int WIDTH = 31;
    public static int HEIGHT = 13;
    
    private GraphicsContext gc;
    private Canvas canvas;
    public static List<Entity> entities = new ArrayList<>();
    public static List<Entity> stillObjects = new ArrayList<>();
    public static List<Entity> damagesObjects = new ArrayList<>();
    private String level;
    public static KeyInput input = new KeyInput();

    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage stage) {
        // Mac dinh level dau tien
        this.level = "2";

        // Tao Canvas
        createMap();
        canvas = new Canvas(Sprite.SCALED_SIZE * WIDTH, Sprite.SCALED_SIZE * HEIGHT);
        gc = canvas.getGraphicsContext2D();

        // Tao root container
        Group root = new Group();
        root.getChildren().add(canvas);             // index 0
        root.getChildren().add(Message.pause());    // index 1

        // Tao scene
        Scene scene = new Scene(root);

        // Them scene vao stage
        stage.setScene(scene);
        stage.setTitle("Bomberman_AK47");
        InputStream is;
        Image icon = new Image("/sprites/bomberman_icon.png");
        stage.getIcons().add(icon);
        stage.show();

        Entity bomberman = new Bomber(1, 1, Sprite.player_right.getFxImage());
        entities.add(bomberman);
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if(entities.get(entities.size() - 1) instanceof Bomber) {
                    if(((Bomber) entities.get(entities.size() - 1)).isDead()) {
                        System.exit(0);
                    }
                }
                render();
                scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        switch (event.getCode()) {
                            case UP:
                                input.up = true; break;
                            case DOWN:
                                input.down = true; break;
                            case LEFT:
                                input.left = true; break;
                            case RIGHT:
                                input.right = true; break;
                            case SPACE:
                                if(input.space) {
                                    input.space = false;
                                    break;
                                }
                                input.space = true; break;
                            case P: case SHIFT:
                                if(input.pause) {
                                    input.pause = false;
                                    break;
                                }
                                input.pause = true; break;
                        }
                    }
                });
                scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
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
                    }
                });
                if(!input.pause) {
                    update();
                    root.getChildren().get(1).setVisible(false);
                }
                else {
                    root.getChildren().get(1).setVisible(true);
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
                this.WIDTH = Column;
                this.HEIGHT = Row;
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
                        default:
                            object = new Grass(j, i, Sprite.grass.getFxImage());
                            break;
                    }
                    if(!item) stillObjects.add(object);
                    else item = false;
                }
            }

            br.close();
            fr.close();
        } catch (Exception e) {
            System.out.println("File error: Cannot found " + levelFilePath);
            e.getMessage();
        }
    }

    public void update() {
        //_input.update();
        for(int i = 0; i < entities.size(); i++) {
            entities.get(i).update();
        }
        for(int i = 0; i < damagesObjects.size(); i++) {
            damagesObjects.get(i).update();
        }
        for(int i = 0; i < stillObjects.size(); i++) {
            stillObjects.get(i).update();
        }
    }

    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        stillObjects.forEach(g -> g.render(gc));
        entities.forEach(g -> g.render(gc));
        damagesObjects.forEach(g -> g.render(gc));
    }
}
