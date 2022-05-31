package pack.battleshipsfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;

public class MainApp extends Application {

    private boolean running = false;
    private Board enemyBoard,playerBoard;

    private  int shipToPlace = 5;

    private boolean enemyTurn = false;

    private final Random random = new Random();

    private Parent createScene(){
        BorderPane root = new BorderPane();
        root.setPrefSize(600,800);
        root.setRight(new Text("RIGHT SIDEBAR - CONTROLS"));

        enemyBoard = new Board(true,event -> {
           if(!running){
               return;
           }

           Tile tile = (Tile) event.getSource();
           if(tile.wasShot){
               return;
           }

           enemyTurn = !tile.shoot();
           if(enemyBoard.shipsCount == 0){
               System.out.println("YOU WON");
               System.exit(0);
           }

           if(enemyTurn){
               enemyMove();
           }
        });

        playerBoard = new Board(false,event -> {
            if(running){
                return;
            }
            Tile tile = (Tile) event.getSource();
            if(playerBoard.placeShip(new Ship(shipToPlace,event.getButton() == MouseButton.PRIMARY),tile.x,tile.y)){
                if(--shipToPlace == 0){
                    startGame();
                }
            }
        });
        VBox vbox = new VBox(50,enemyBoard,playerBoard);
        vbox.setAlignment(Pos.CENTER);

        root.setCenter(vbox);

        return root;
    }

    private void enemyMove(){
        while(enemyTurn){
            int x = random.nextInt(10);
            int y = random.nextInt(10);

            Tile tile = playerBoard.getTile(x,y);
            if(tile.wasShot){
                continue;
            }

            enemyTurn = tile.shoot();

            if(playerBoard.shipsCount == 0){
                System.out.println("YOU LOST");
                System.exit(0);
            }
        }
    }

    private void startGame(){
        //place enemy ships
        int type = 5;

        while(type > 0){
            int x = random.nextInt(10);
            int y = random.nextInt(10);

            if(enemyBoard.placeShip(new Ship(type,Math.random() < 0.5),x,y)){
                type--;
            }
        }

        running = true;
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