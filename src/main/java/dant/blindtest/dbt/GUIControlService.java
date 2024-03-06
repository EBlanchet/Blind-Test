package dant.blindtest.dbt;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class GUIControlService {
    private final String applicationName;
    private final String applicationVersion;
    private final GameInstanceSingleton GIS;

    public GUIControlService() {
        this.GIS = GameInstanceSingleton.getInstance();
        this.applicationName = GIS.getProp("APP_NAME");
        this.applicationVersion = GIS.getProp("APP_VERSION");
    }

    protected void changeScene(String sourceFileName) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(sourceFileName));
        Parent root;

        String cssFileName = sourceFileName.substring(0, sourceFileName.length() - 5) + ".css";
        String cssFilePath = String.valueOf(getClass().getResource(cssFileName));

        try {
            root = fxmlLoader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(cssFilePath);
            //System.out.println("CSS - Trying to set a new scene!");
            Platform.runLater(() -> {
                DBTApplication.stage.setScene(scene);
                DBTApplication.stage.show();
                //System.out.println("CSS - Set a new scene!");
            });
        } catch (IOException ioe) {
            System.out.println("An error occurred trying to load file: " + sourceFileName);
            ioe.printStackTrace();
        }
    }

    public void setTitle(String title) {
        String trueTitle = applicationName + " v" + applicationVersion + " - " + title;
        Platform.runLater(() -> DBTApplication.stage.setTitle(trueTitle));
    }

    public void updateProgressBar() {
        Platform.runLater(() -> {
            ProgressBar bar = (ProgressBar) DBTApplication.stage.getScene().lookup("#timeLeftProgressBar");
            Label l = (Label) DBTApplication.stage.getScene().lookup("#timeLeftLabel");
            if (bar == null) return;

            Double current = Double.valueOf(GIS.currentTime);
            Double timeout = Double.valueOf(GIS.turnTimeout);
            double progress = current/timeout;
            bar.setProgress(progress);
            l.setText(String.valueOf(GIS.currentTime));
        });
    }

    public void changeSAT(String fileName, String title) {
        this.changeScene(fileName);
        this.setTitle(title);
    }

    public void updateIV (ImageView target, String resPath) {
        if (target == null) throw new IllegalArgumentException("Cannot update null image.");
        if (GIS.currentTurn >= GIS.numberOfTurns) return;
        Tuple currentAnswer = GIS.answerTable.get(GIS.currentTurn);

        Platform.runLater(() -> {
            try {
                target.setImage(this.loadImage(resPath, currentAnswer.getValue()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void updateIV(String nodeName, String resPath) {
        Platform.runLater(() -> {
            ImageView targetIV = (ImageView) DBTApplication.stage.getScene().lookup(nodeName);
            updateIV(targetIV, resPath);
        });
    }

    public Image loadImage(String resPath, String name) throws IOException {
        try (InputStream resourceStream = getClass().getResourceAsStream(resPath + name)) {
            //System.out.println("Tried loading image: " + name);
            assert resourceStream != null;
            return new Image(resourceStream);
        }
    }

    public void updateMV(MediaView target, String resPath) {
        if (target == null) throw new IllegalArgumentException("Cannot update null media view.");
        Tuple currentAnswer = GIS.answerTable.get(GIS.currentTurn);

        System.out.println(getClass().getName() + " - Updating MV to play " + currentAnswer.getValue());

        Platform.runLater(() -> {
            target.getMediaPlayer().stop();
            MediaPlayer mediaPlayer = new MediaPlayer(loadMedia(resPath, currentAnswer.getValue()));
            mediaPlayer.setStartTime(Duration.ZERO);
            mediaPlayer.setStopTime(new Duration(GIS.turnTimeout * 1000));
            mediaPlayer.setAutoPlay(true);
            target.setMediaPlayer(mediaPlayer);
            mediaPlayer.play();
        });
    }

    public void updateMV(String nodeName, String resPath) {
        Platform.runLater(() -> {
            MediaView targetMV = (MediaView) DBTApplication.stage.getScene().lookup(nodeName);
            updateMV(targetMV, resPath);
        });
    }
    public Media loadMedia(String resPath, String name) {
        try {
            return new Media(Objects.requireNonNull(getClass().getResource(resPath + name)).toExternalForm());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void showAlert(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(GIS.getProp("APP_NAME") + " v" + GIS.getProp("APP_VERSION") + " - Alerte");
            alert.setHeaderText(message);
            alert.setContentText("");

            Image icon = new Image(Objects.requireNonNull(DBTApplication.class.getResourceAsStream("assets/icon.png")));
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(icon);

            alert.showAndWait();
        });
    }
}
