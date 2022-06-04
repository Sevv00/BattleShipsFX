package pack.battleshipsfx;

import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

import java.util.Random;

public class Game {
    public boolean running = false;
    public Board enemyBoard,playerBoard;
    private boolean enemyTurn = false;;
    private  int shipToPlace = 5;
    private final Random random = new Random();

    public Game(){
        enemyBoard = new Board(true,event -> {
            if(!running){
                return;
            }

            Tile tile = (Tile) event.getSource();
            if(tile.wasShot){
                return;
            }

            enemyTurn = !shoot(enemyBoard,tile);
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
                    placeEnemyShips();
                }
            }
        });
    }

    public boolean placeShip(Ship ship, int x, int y){
        if(validateShipPlacement(ship,x,y)){
            int length = ship.getShipType();
            boolean vertical = ship.isInVerticalPosition();
            if(vertical){
                for (int i = y; i < y+length;i++){
                    Tile tile = getTile(x,i);
                    tile.ship = ship;
                    if(!enemy){
                        tile.setFill(Color.GRAY);
                        tile.setStroke(Color.DARKGRAY);
                    }
                }
            }
            else{
                for (int i = x; i < x+length;i++){
                    Tile tile = getTile(i,y);
                    tile.ship = ship;
                    if(!enemy){
                        tile.setFill(Color.GRAY);
                        tile.setStroke(Color.DARKGRAY);
                    }
                }
            }
            return true;
        }
        return false;
    }

    private void enemyMove(){
        while(enemyTurn){
            int x = random.nextInt(10);
            int y = random.nextInt(10);

            Tile tile = playerBoard.getTile(x,y);
            if(tile.wasShot){
                continue;
            }

            enemyTurn = Game.shoot(playerBoard,tile);

            if(playerBoard.shipsCount == 0){
                System.out.println("YOU LOST");
                System.exit(0);
            }
        }
    }

    private void placeEnemyShips(){
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
    public boolean shoot(Board board, Tile tile){
        tile.wasShot = true;
        if(tile.ship != null){
            tile.ship.hit();
            tile.setFill(Color.ORANGERED);
            tile.setStroke(Color.DARKGRAY);
            if(!tile.ship.isOperational()){
                board.shipsCount--;
            }
            return true;
        }
        tile.setFill(Color.MEDIUMAQUAMARINE);
        tile.setStroke(Color.DARKGRAY);
        return false;
    }
}
