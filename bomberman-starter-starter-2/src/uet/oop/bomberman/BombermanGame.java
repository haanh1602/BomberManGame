package uet.oop.bomberman;

/*
    SDK 1.8 java version 1.8.0_261
 */

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import uet.oop.bomberman.command.*;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.entities.moveEntities.enemy.*;
import uet.oop.bomberman.entities.moveEntities.Bomber;
import uet.oop.bomberman.entities.moveEntities.Effect;
import uet.oop.bomberman.entities.stillEntities.mortal.item.*;
import uet.oop.bomberman.entities.stillEntities.mortal.Brick;
import uet.oop.bomberman.entities.stillEntities.immortal.Grass;
import uet.oop.bomberman.entities.stillEntities.immortal.Portal;
import uet.oop.bomberman.entities.stillEntities.immortal.Wall;
import uet.oop.bomberman.graphics.*;
import uet.oop.bomberman.sound.Sound;
import uet.oop.bomberman.sound.SoundController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//import javafx.scene.canvas.Canvas;

public class BombermanGame extends Application {
    
    public static int WIDTH = 31;
    public static int HEIGHT = 13;
    public static int score = 0;
    public static String level;
    public static KeyInput input = new KeyInput();
    
    private GraphicsContext gc;
    private Canvas canvas;
    private Group root = new Group();
    private Scene scene = new Scene(root);
    private Stage stage = new Stage();
    public static Sound sound = new Sound();
    public static List<Entity> entities = new ArrayList<>();
    public static List<Entity> stillObjects = new ArrayList<>();
    public static List<Entity> damagesObjects = new ArrayList<>();
    public static List<Effect> effects = new ArrayList<>();

    AnchorPane scoreBoard = null;
    ScoreBoardController scoreBoardController = null;
    AnchorPane levelScene = null;
    LevelSceneController levelSceneController = null;

    // Bomber status
    public static int life = 3;
    public static final int MAX_LIFE = 3;
    public static int flame = 1;
    public static final int MAX_FLAME = 5;
    public static int speed = 1;
    public static final int MAX_SPEED = 3;
    public static int bombs = 1;
    public static final int MAX_BOMBS = 10;
    public static int levelMaxScore = 0;

    private int time_levelScene = 3500/16;
    private boolean levelStart = false;

    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage stage) {
        Font.loadFont(getClass().getResourceAsStream("/Bomberman.ttf"), 16);
        this.stage = stage;
        createNewLevel(1);

        // Them scene vao stage
        stage.setScene(scene);
        stage.setTitle("Bomberman_AK47");
        Image icon = new Image("/sprites/bomberman_icon.png");
        stage.getIcons().add(icon);
        stage.show();

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
                                input.space = false; break;
                            }
                            input.space = true; break;
                        case SHIFT:
                            if(input.pause) {
                                input.pause = false; break;
                            }
                            input.pause = true; break;
                        case N:
                            createNewLevel(Integer.parseInt(level) + 1);
                            break;
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
                            //createNewLevel(Integer.parseInt(level) + 1);
                            System.exit(0);
                        }
                    });
                    if(root.getChildren().size() > 2) root.getChildren().remove(2);
                    root.getChildren().add(Message.lose());
                    //System.out.println(root.getChildren().size());
                    //root.getChildren().get(2).setVisible(true);
                    input.pause = true;
                } else {
                    if(!input.pause) {
                        if(root.getChildren().size() > 2) root.getChildren().remove(2);
                        //System.out.println(root.getChildren().size());
                        update();
                        portalHandle();
                    }
                    else {
                        if(root.getChildren().size() > 2) root.getChildren().remove(2);
                        root.getChildren().add(Message.pause());
                        root.getChildren().get(2).setVisible(true);
                        //System.out.println(root.getChildren().size());
                    }
                }
                /*Random random = new Random(1);
                System.out.println((int) (Math.random() * 100));*/
            }

        };
        timer.start();
    }

    private void createMap() {
        String levelFilePath = new File("").getAbsolutePath() + "/res/levels/Level" + level + ".txt";
        try {
            FileReader fr = new FileReader(new File(levelFilePath));
            BufferedReader br = new BufferedReader(fr);

            entities.clear();
            stillObjects.clear();
            damagesObjects.clear();
            effects.clear();

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
                        case 'p':
                            //object = new Portal(j, i, Sprite.portal.getFxImage());
                            stillObjects.add(0, new Portal(j, i, Sprite.portal.getFxImage()));
                            object = new Brick(j, i, Sprite.brick.getFxImage());
                            //stillObjects.add(new Brick(j, i, Sprite.brick.getFxImage()));
                            break;
                        case 'b':
                            object = new BombsItem(j, i, Sprite.powerup_bombs.getFxImage());
                            stillObjects.add(object);
                            stillObjects.add(new Brick(j, i, Sprite.brick.getFxImage()));
                            item = true;
                            break;
                        case 'f':
                            object = new FlameItem(j, i, Sprite.powerup_flames.getFxImage());
                            stillObjects.add(object);
                            stillObjects.add(new Brick(j, i, Sprite.brick.getFxImage()));
                            item = true;
                            break;
                        case 's':
                            object = new SpeedItem(j, i, Sprite.powerup_speed.getFxImage());
                            stillObjects.add(object);
                            stillObjects.add(new Brick(j, i, Sprite.brick.getFxImage()));
                            item = true;
                            break;
                        case 'l':
                            object = new FlamePassItem(j, i, Sprite.powerup_flamepass.getFxImage());
                            stillObjects.add(object);
                            stillObjects.add(new Brick(j, i, Sprite.brick.getFxImage()));
                            item = true;
                            break;
                        case 'i':
                            URL url = BombermanGame.class.getResource("/sprites/powerup_invincibility.png");
                            object = new InvincibilityItem(j, i, new Image(url.toString()));
                            stillObjects.add(object);
                            stillObjects.add(new Brick(j, i, Sprite.brick.getFxImage()));
                            item = true;
                            break;
                        case 'd':
                            object = new DetonatorItem(j, i, Sprite.powerup_detonator.getFxImage());
                            stillObjects.add(object);
                            stillObjects.add(new Brick(j, i, Sprite.brick.getFxImage()));
                            item = true;
                            break;
                        case '1':
                            object = new Grass(j, i, Sprite.grass.getFxImage());
                            Ballom ballom = new Ballom(j, i, null);
                            damagesObjects.add(ballom);
                            levelMaxScore += 100;
                            break;
                        case '2':
                            object = new Grass(j, i, Sprite.grass.getFxImage());
                            Oneal oneal = new Oneal(j, i, null);
                            damagesObjects.add(oneal);
                            levelMaxScore += 200;
                            break;
                        case '3':
                            object = new Grass(j, i, Sprite.grass.getFxImage());
                            Kondoria kondoria = new Kondoria(j, i, null);
                            damagesObjects.add(kondoria);
                            levelMaxScore += 1000;
                            break;
                        case '4':
                            object = new Grass(j, i, Sprite.grass.getFxImage());
                            Minvo minvo = new Minvo(j, i, null);
                            damagesObjects.add(minvo);
                            levelMaxScore += 800;
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
        scoreBoardController.update();
    }

    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        stillObjects.forEach(g -> g.render(gc));
        damagesObjects.forEach(g -> g.render(gc));
        entities.forEach(g -> g.render(gc));
        effects.forEach(g -> g.render(gc));
    }

    public void createNewLevel(int level) {
        sound.currentThemeSound = sound.findTheExit;
        SoundController.makeSound("Level_Start.mp3").play();
        sound.replay(sound.currentThemeSound);
        sound.repeat(sound.currentThemeSound);
        this.level = String.valueOf(level);
        createMap();
        Entity bomberman = new Bomber(1, 1, Sprite.player_right.getFxImage());
        entities.add(bomberman);
        canvas = new Canvas(Sprite.SCALED_SIZE * WIDTH, Sprite.SCALED_SIZE * HEIGHT);
        gc = canvas.getGraphicsContext2D();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../../../panes/ScoreBoard.fxml"));
            scoreBoard = fxmlLoader.load();
            scoreBoardController = fxmlLoader.getController();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        scoreBoard.relocate(Sprite.SCALED_SIZE * WIDTH, 0);
        scoreBoard.setPrefHeight(Sprite.SCALED_SIZE * HEIGHT);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../../../panes/LevelScene.fxml"));
            levelScene = fxmlLoader.load();
            System.out.println(levelScene.getPrefWidth() + ", " + levelScene.getPrefHeight());
            levelSceneController = fxmlLoader.getController();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        levelScene.relocate(0, 0);
        levelScene.setPrefSize(canvas.getWidth(), canvas.getHeight());
        System.out.println(levelScene.getPrefWidth() + ", " + levelScene.getPrefHeight());
        System.out.println(canvas.getWidth() + ", " + canvas.getHeight());
        root.getChildren().clear();
        root.getChildren().add(canvas);         // index 0
        root.getChildren().add(scoreBoard);     // index 1
        //root.getChildren().add(levelScene);     // index 2
        scene.setRoot(root);
        stage.sizeToScene();
    }

    public static Bomber getBomber() {
        if(BombermanGame.entities.get(BombermanGame.entities.size() - 1) instanceof Bomber) {
            return (Bomber) BombermanGame.entities.get(BombermanGame.entities.size() - 1);
        } else {
            return null;
        }
    }

    public static Portal getPortal() {
        for(int i = 0; i < stillObjects.size(); i++) {
            if(stillObjects.get(i) instanceof Portal) {
                return (Portal) stillObjects.get(i);
            }
        }
        return null;
    }

    public void portalHandle() {
        if((int) Math.round(getBomber().getX()) == (int) BombermanGame.getPortal().getX()
                && (int) Math.round(getBomber().getY()) == (int) BombermanGame.getPortal().getY()) {
            if(BombermanGame.getPortal().isOpen) {
                System.out.println("Create new level " + (Integer.parseInt(level) + 1));
                createNewLevel(Integer.parseInt(level) + 1);
            }
        }
    }

    public static void destroyEnemy() {
        for(int i = 0; i < damagesObjects.size();) {
            if(damagesObjects.get(i) instanceof Enemy) {
                ((Enemy)damagesObjects.get(i)).destroy();
                i = 0;
            }
            else break;
        }
    }
}
