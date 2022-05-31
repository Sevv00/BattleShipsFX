module pack.battleshipsfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens pack.battleshipsfx to javafx.fxml;
    exports pack.battleshipsfx;
}