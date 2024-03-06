package dant.blindtest.dbt;

import javafx.application.Platform;
import javafx.scene.media.MediaView;

import java.util.*;

public class GameInstanceSingleton {
    private static GameInstanceSingleton instance;

    private final Properties props;
    public Double prefWidth;
    public Double prefHeight;

    public GameTypes gameType;
    public Integer numberOfTurns;
    public Integer turnTimeout;

    public List<Tuple> answerTable;

    public Integer score = 0;
    public Integer currentTurn = 0;
    public Integer currentTime;

    private Timer timer;
    private TimerTask task;

    private GameInstanceSingleton(Properties p) {
        props = p;

        numberOfTurns = Integer.valueOf(p.getProperty("DEFAULT_TURN_NUMBER"));
        turnTimeout = Integer.valueOf(p.getProperty("DEFAULT_TURN_TIMEOUT"));
        prefWidth = Double.valueOf(p.getProperty("P_WIDTH"));
        prefHeight = Double.valueOf(p.getProperty("P_HEIGHT"));

        answerTable = new ArrayList<>(numberOfTurns);
        instance = this;
    }

    public void startTimer() {
        GUIControlService GCS = new GUIControlService();
        getInstance().timer = new Timer(true);
        this.currentTime = this.turnTimeout;
        GCS.updateProgressBar();

        //System.out.println("Started timer " + timer + "!");

        getInstance().task = new TimerTask() {
            @Override
            public void run() {
                GameInstanceSingleton GIS = GameInstanceSingleton.getInstance();

                if (GIS.currentTime > 0) {
                    if (GIS.currentTime <= 1) {
                        // 1 sec left, going to 0 so timeout
                        System.out.println(getClass().getName() + " - Timeout! Skipping turn.");
                        GIS.skipTurn();
                    } else {
                        // There is time left
                        GIS.currentTime--;
                        //System.out.println("New time: " + GameInstanceSingleton.currentTime);
                        GCS.updateProgressBar();
                    }
                } else {
                    // No time left, not last turn
                    System.out.println(getClass().getName() + " - Timeout! Skipping turn.");
                    GIS.skipTurn();
                }
            }
        };

        getInstance().timer.schedule(getInstance().task, 1000, 1000);
    }

    public void stopTimer() {
        GameInstanceSingleton GIS = getInstance();
        GIS.task.cancel();
        GIS.timer.cancel();
        GIS.timer.purge();
        GIS.timer = new Timer(true);
    }

    public void restartTimer() {
        stopTimer();
        startTimer();
    }

    public void bakeAnswers() {
        answerTable = new ArrayList<>(numberOfTurns);

        List<String> keys = DataParser.keys();
        Collections.shuffle(keys);
        keys.forEach(name -> {
            String picture = DataParser.get(name);
            answerTable.add(new Tuple(name, picture));
        });
    }

    public static GameInstanceSingleton getInstance(Properties p) {
        return (instance == null) ? new GameInstanceSingleton(p) : instance;
    }

    public static GameInstanceSingleton getInstance() {
        return instance;
    }

    public String getProp(String key) {
        return this.props.getProperty(key);
    }

    public void skipTurn() {
        currentTurn++;
        GUIControlService GCS = new GUIControlService();

        if (currentTurn >= numberOfTurns) {
            int scoreMultiplier = Integer.parseInt(getInstance().getProp("SCORE_MULTI"));

            String msg = getInstance().getProp("GAME_OVER_MSG");
            msg += getInstance().score + "/" + numberOfTurns * scoreMultiplier;

            stopTimer();

            GCS.changeSAT(getInstance().getProp("MAIN_MENU"), getInstance().getProp("MAIN_MENU_TITLE"));
            GCS.showAlert(msg);
        } else {
            switch (gameType) {
                case PEOPLE -> GCS.updateIV("#targetImage", "assets/people/");
                case MUSIC -> GCS.updateMV("#targetMediaView", "assets/music/");
            }
            restartTimer();
        }
    }

    public void checkAnswer(String answer) {
        Tuple t = this.answerTable.get(this.currentTurn);
        String name = t.toString().toLowerCase();

        System.out.println(getClass().getName() + " - User typed \"" + answer + "\" answer was " + name);

        int scoreMultiplier = Integer.parseInt(this.getProp("SCORE_MULTI"));
        getInstance().score += (name.equals(answer)) ? scoreMultiplier : 0;
    }
}
