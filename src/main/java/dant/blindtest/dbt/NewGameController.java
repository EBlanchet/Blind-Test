package dant.blindtest.dbt;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class NewGameController extends ApplicationFXController {
    @FXML
    private ComboBox<String> gameTypeComboBox;
    @FXML
    private TextField numberTurn, maxTurnTime;

    public NewGameController() {
        super();

        Platform.runLater(() -> {
            numberTurn.setText(String.valueOf(GIS.numberOfTurns));
            maxTurnTime.setText(String.valueOf(GIS.turnTimeout));
        });
    }

    @FXML
    protected void startGameButtonAction() {
        String gameString = gameTypeComboBox.getValue();
        if (gameString == null) {
            GCS.showAlert(GIS.getProp("INVALID_GAME_TYPE_MSG"));
            return;
        }

        GIS.gameType = switch (gameString) {
            case "Personnes connues" -> GameTypes.PEOPLE;
            case "Musiques connues" -> GameTypes.MUSIC;
            default -> null;
        };

        assert GIS.gameType != null;
        System.out.println(getClass().getName() + " - User started a game of type " + GIS.gameType);

        new DataParser(GIS.gameType);

        String NTText = numberTurn.getText();
        String MTTText = maxTurnTime.getText();

        if (!NTText.matches("[0-9]+") || !MTTText.matches("[0-9]+")) {
            GCS.showAlert(GIS.getProp("INVALID_INPUT_MSG"));
            return;
        }

        GIS.numberOfTurns = Integer.valueOf(NTText);
        GIS.turnTimeout = Integer.valueOf(MTTText);
        GIS.currentTurn = 0;

        if (GIS.numberOfTurns > DataParser.getSize() || GIS.numberOfTurns < 1) {
            GCS.showAlert(GIS.getProp("INVALID_TURNS_MSG") + DataParser.getSize() + " !");
            return;
        }

        GIS.score = 0;
        GIS.bakeAnswers();

        switch (GIS.gameType) {
            case PEOPLE -> GCS.changeSAT(GIS.getProp("GAME_MENU"), GIS.getProp("GAME_MENU_TITLE"));
            case MUSIC -> GCS.changeSAT(GIS.getProp("GAME_MENU_MUSIC"), GIS.getProp("GAME_MENU_TITLE"));
        }

        System.out.println(getClass().getName() + " - Started for " + GIS.numberOfTurns + " turns of " + GIS.turnTimeout + "s");

        GCS.updateProgressBar();
    }

    @FXML
    protected void discardButtonAction() {
        System.out.println(getClass().getName() +  " - User returned to main menu.");
        GCS.changeSAT(GIS.getProp("MAIN_MENU"), GIS.getProp("MAIN_MENU_TITLE"));
    }
}
