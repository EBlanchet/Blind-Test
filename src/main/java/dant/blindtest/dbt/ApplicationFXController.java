package dant.blindtest.dbt;

public class ApplicationFXController {
    public GameInstanceSingleton GIS;
    public GUIControlService GCS;

    public ApplicationFXController() {
        try {
            GIS = GameInstanceSingleton.getInstance();
        } catch (Exception e) {
            System.out.println("Tried getting a GameInstanceSingleton before it got set up!");
            throw new RuntimeException(e);
        }
        GCS = new GUIControlService();
    }
}
