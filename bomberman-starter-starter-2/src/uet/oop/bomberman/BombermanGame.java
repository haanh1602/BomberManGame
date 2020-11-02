package uet.oop.bomberman;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.graphics.Sprite;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class BombermanGame extends Application {
    
    public static int WIDTH = 20;
    public static int HEIGHT = 15;
    
    private GraphicsContext gc;
    private Canvas canvas;
    public static List<Entity> entities = new ArrayList<>();
    public static List<Entity> stillObjects = new ArrayList<>();
    public static List<Entity> damagesObjects = new ArrayList<>();
    private String level;

    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage stage) {
        // Mac dinh level dau tien
        this.level = "1";

        // Tao Canvas
        createMap();
        canvas = new Canvas(Sprite.SCALED_SIZE * WIDTH, Sprite.SCALED_SIZE * HEIGHT);
        gc = canvas.getGraphicsContext2D();

        // Tao root container
        Group root = new Group();
        root.getChildren().add(canvas);

        // Tao scene
        Scene scene = new Scene(root);

        // Them scene vao stage
        stage.setScene(scene);
        stage.show();

        Entity bomberman = new Bomber(1, 1, Sprite.player_right.getFxImage());
        entities.add(bomberman);
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                //Bomber bomber = (Bomber) entities.get(entities.size() - 1);
                render();
                scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        entities.get(entities.size() - 1).update(scene, event, entities);
                        /*if(event.getCode() == KeyCode.H) {
                            bomber.isDead();
                        }
                        if(!entities.get(entities.size() - 1).isAlive()) {
                            System.exit(0);
                        }*/
                    }
                });
                update();
            }
        };
        timer.start();
    }

    public void createMap() {
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
                    Entity object;
                    switch (line.charAt(j)) {
                        case '#':
                            object = new Wall(j, i, Sprite.wall.getFxImage());
                            break;
                        case '*':
                            object = new Brick(j, i, Sprite.brick.getFxImage());
                            break;
                        default:
                            object = new Grass(j, i, Sprite.grass.getFxImage());
                            break;
                    }
                    stillObjects.add(object);
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
        for(int i = 0; i < damagesObjects.size(); i++) {
            damagesObjects.get(i).update();
        }
        for(int i = 0; i < entities.size(); i++) {
            entities.get(i).update();
        }
    }

    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        stillObjects.forEach(g -> g.render(gc));
        entities.forEach(g -> g.render(gc));
        damagesObjects.forEach(g -> g.render(gc));
    }
}
