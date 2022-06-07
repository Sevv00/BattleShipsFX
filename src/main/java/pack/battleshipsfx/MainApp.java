package pack.battleshipsfx;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainApp extends Application {
    private Game game;
    private Parent createScene(){
        BorderPane root = new BorderPane();
        root.setPrefSize(600,800);
        root.setRight(new Text("ALPHA 1.0 "));
        game = new Game();
        VBox vbox = new VBox(50,game.playerTwoBoard,game.playerOneBoard);
        vbox.setAlignment(Pos.CENTER);

        root.setCenter(vbox);

        return root;
    }


    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(createScene());
        stage.setTitle("BattleShipsFX");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}