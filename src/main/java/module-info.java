module dant.blindtest.dbt {
    requires javafx.controls;
    requires javafx.media;
    requires javafx.fxml;
            
                            
    opens dant.blindtest.dbt to javafx.fxml;
    exports dant.blindtest.dbt;
}