package dant.blindtest.dbt;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

public class GameMenuMusicController extends ApplicationFXController {
    @FXML
    private TextField answerField;
    @FXML
    private MediaView targetMediaView;

    public GameMenuMusicController() {
        super();
        updateMedia();
        GIS.startTimer();
    }

    @FXML
    protected void checkAnswerAction() {
        String answer = answerField.getText().toLowerCase();
        answerField.setText("");

        if (GIS.currentTurn + 1 >= GIS.numberOfTurns) targetMediaView.getMediaPlayer().dispose();

        GIS.checkAnswer(answer);
        GIS.skipTurn();
    }

    public void updateMedia() {
        Tuple t = GIS.answerTable.get(GIS.currentTurn);
        Platform.runLater(() -> {
            MediaPlayer mediaPlayer = new MediaPlayer(GCS.loadMedia("assets/music/", t.getValue()));
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.setStartTime(Duration.ZERO);
            mediaPlayer.setStopTime(new Duration(GIS.turnTimeout * 1000));
            targetMediaView.setMediaPlayer(mediaPlayer);
            mediaPlayer.play();
        });
    }
}
