package dant.blindtest.dbt;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainMenuController extends ApplicationFXController {
    @FXML
    private Button exitButton;

    public MainMenuController() {
        super();
    }

    @FXML
    protected void newGameButtonAction() {
        System.out.println(getClass().getName() + " - User wants to start a new game!");
        GCS.changeSAT(GIS.getProp("NEW_GAME"), GIS.getProp("NEW_GAME_TITLE"));
    }

    @FXML
    protected void exitButtonAction() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}
