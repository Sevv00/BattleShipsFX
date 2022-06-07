package pack.battleshipsfx;

import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

import java.util.Random;

public class Game {
    public boolean running = false;
    public Board playerOneBoard,playerTwoBoard;
    private boolean enemyTurn = false;
    private  int shipToPlace = 5;
    private final Random random = new Random();
    private final BotPlayer bot;

    public Game(){
        playerTwoBoard = new Board(true,event -> {
            if(!running){
                return;
            }

            Tile tile = (Tile) event.getSource();
            System.out.println(tile.x +" "+ tile.y);
            if(tile.wasShot){
                return;
            }

            enemyTurn = !shoot(playerTwoBoard,tile);

            //TO DO WIN SCREEN
            if(playerTwoBoard.shipsCount == 0){
                System.out.println("YOU WON");
                System.exit(0);
            }

            if(enemyTurn){
                enemyMove();
            }
        });
        playerOneBoard = new Board(false,event -> {
            if(running){
                return;
            }
            Tile tile = (Tile) event.getSource();
            if(placeShip(playerOneBoard,new Ship(shipToPlace,event.getButton() == MouseButton.PRIMARY),tile.x,tile.y)){
                if(--shipToPlace == 0){
                    placeEnemyShips();
                }
            }
        });
        bot = new BotPlayer(playerOneBoard);
    }

    public boolean placeShip(Board board,Ship ship, int x, int y){
        if(ShipPlacementValidation.validateShipPlacement(board,ship,x,y)){
            int length = ship.getShipType();
            boolean vertical = ship.isInVerticalPosition();
            if(vertical){
                for (int i = y; i < y+length;i++){
                    Tile tile = board.getTile(x,i);
                    tile.ship = ship;
                    if(!board.isEnemyBoard()){
                        tile.setFill(Color.GRAY);
                        tile.setStroke(Color.DARKGRAY);
                    }
                }
            }
            else{
                for (int i = x; i < x+length;i++){
                    Tile tile = board.getTile(i,y);
                    tile.ship = ship;
                    if(!board.isEnemyBoard()){
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
        while (enemyTurn) {
            Tile tile = bot.nextTargetPosition();
            enemyTurn = this.shoot(playerOneBoard, tile);
            if (playerOneBoard.shipsCount == 0) {
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

            if(placeShip(playerTwoBoard,new Ship(type,Math.random() < 0.5),x,y)){
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
                toggleNeighboursTile(board,tile);
                board.shipsCount--;
            }
            return true;
        }
        tile.setFill(Color.MEDIUMAQUAMARINE);
        tile.setStroke(Color.DARKGRAY);
        return false;
    }

    private void toggleNeighboursTile(Board board,Tile tile){
        Tile[] neighbours = board.getNeighbours(tile.x,tile.y);
        tile.setFill(Color.RED);
        for (Tile tiles : neighbours){
            if(tiles.getFill() == Color.AQUA){
                tiles.setFill(Color.MEDIUMAQUAMARINE);
                tiles.setStroke(Color.DARKGRAY);
                tiles.wasShot = true;
            }
            if(tiles.getFill() == Color.ORANGERED){
                tiles.setFill(Color.RED);
                toggleNeighboursTile(board,tiles);
            }
        }
    }
}
