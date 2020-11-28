package uet.oop.bomberman.command;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

import java.net.URL;
import java.util.ResourceBundle;

public class LevelSceneController implements Initializable {
    @FXML
    public Label level;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        level.relocate(BombermanGame.WIDTH * Sprite.SCALED_SIZE, BombermanGame.HEIGHT * Sprite.SCALED_SIZE);
        level.setText("Level " + BombermanGame.level);
    }
}
