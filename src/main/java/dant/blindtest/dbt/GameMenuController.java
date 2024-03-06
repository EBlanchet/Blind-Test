package dant.blindtest.dbt;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class GameMenuController extends ApplicationFXController {
    @FXML
    private ImageView targetImage;
    @FXML
    private TextField answerField;

    public GameMenuController() {
        super();
        this.updateImage();
        GIS.startTimer();
    }

    @FXML
    protected void checkAnswerAction() {
        String answer = answerField.getText().toLowerCase();
        answerField.setText("");

        GIS.checkAnswer(answer);
        GIS.skipTurn();
    }

    public void updateImage() {
        Tuple t = GIS.answerTable.get(GIS.currentTurn);
        Platform.runLater(() -> {
            try {
                targetImage.setImage(GCS.loadImage("assets/people/", t.getValue()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
