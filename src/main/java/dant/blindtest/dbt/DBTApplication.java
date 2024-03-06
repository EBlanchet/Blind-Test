package dant.blindtest.dbt;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class DBTApplication extends Application {
    Properties props;
    public static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        DBTApplication.stage = stage;

        Image icon = new Image(Objects.requireNonNull(DBTApplication.class.getResourceAsStream("assets/icon.png")));
        stage.getIcons().add(icon);

        GameInstanceSingleton GIS;
        String configFile = "config.properties";
        props = new Properties();

        try (InputStream resourceStream = DBTApplication.class.getResourceAsStream(configFile)) {
            props.load(resourceStream);
            GIS = GameInstanceSingleton.getInstance(props);
            System.out.println(getClass().getName() + " - Instantiated a GIS: " + GIS);
        }

        GUIControlService GCS = new GUIControlService();
        GCS.changeSAT(GIS.getProp("MAIN_MENU"), GIS.getProp("MAIN_MENU_TITLE"));
    }

    public static void main(String[] args) {
        launch();
    }
}