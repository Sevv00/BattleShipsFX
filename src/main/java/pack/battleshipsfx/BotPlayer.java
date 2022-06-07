package pack.battleshipsfx;

import java.util.Random;
public class BotPlayer {

    private final Board playerBoard;
    private Tile blindShot;
    private Tile target;

    private int hitInRow;
    private int randomTarget;
    private final boolean[] shipTypeSunk;
    private final int[] shipPlacementMap; // indexes: 0-up 1-down 2-left 3-right values: -1 -border 0 -miss 1=< -possible ship position
    private boolean targetLocked;

    private final Random random = new Random();

    public BotPlayer(Board board){
        playerBoard = board;
        targetLocked = false;
        hitInRow = 0;
        shipTypeSunk = new boolean[5];
        shipPlacementMap = new int[4];
        for (boolean ship : shipTypeSunk){
            ship = false;
        }
    }

    public Tile nextTargetPosition() {
        if(targetLocked){
            if(!target.ship.isOperational()){
                shipTypeSunk[hitInRow-1] = true;
                targetLocked = false;
            }
        }
        if(targetLocked){
            checkPossibleEnemyShipDirectionPhase2();
            chooseTarget();
            if(blindShot.ship != null){
                hitInRow++;
                checkPossibleEnemyShipDirectionPhase3(true);

            }
            else{
                checkPossibleEnemyShipDirectionPhase3(false);
            }
        }
        else{
            blindShot = chooseTargetNoLocked();
            if(blindShot.ship != null){
                hitInRow = 1;
                if(blindShot.ship.isOperational()){
                    targetLocked = true;
                    target = blindShot;
                    checkPossibleEnemyShipDirectionPhase1();
                }
            }

            return blindShot;
        }
        return blindShot;
    }
    private void chooseTarget(){
        randomTarget = random.nextInt(4);
        boolean targetNotReady = true;
        while (targetNotReady){
            if(shipPlacementMap[randomTarget] > 0){
                targetNotReady = false;
            }
            else{
                randomTarget++;
            }
            if(randomTarget == 4){
                randomTarget = 0;
            }
        }
        blindShot = returnTileFromTarget(randomTarget);
    }

    private void checkPossibleEnemyShipDirectionPhase3(boolean outcome){
        if(outcome){
            if(isShipLargeEnoughLeft(hitInRow+1)){
                shipPlacementMap[randomTarget]++;
                if(randomTarget < 2){
                    shipPlacementMap[2] = 0;
                    shipPlacementMap[3] = 0;
                }
                else{
                    shipPlacementMap[1] = 0;
                    shipPlacementMap[2] = 0;
                }
            }
            else {
                targetLocked = false;
            }
        }
        else{
            shipPlacementMap[randomTarget] = 0;
        }
    }

    private boolean isShipLargeEnoughLeft(int hits){
        int i = 1;
        for(boolean ship: shipTypeSunk){
             if(!ship && hits <= i){
                 return true;
             }
             i++;
        }
        return false;
    }
    private void checkPossibleEnemyShipDirectionPhase2(){
        int i =0;
        for (int map: shipPlacementMap){
            if(map > 0) {
                checkAvailableTiles(i);
            }
            i++;
        }
    }
    private void checkAvailableTiles(int index){
        Tile tile = returnTileFromTarget(index);
        if(tile.wasShot){
            shipPlacementMap[index] = 0;
        }
    }
    private Tile returnTileFromTarget(int index){
        int x = target.x;
        int y = target.y;
        switch(index){
            case 0:{
                y -= shipPlacementMap[index];
                break;
            }
            case 1:{
                y += shipPlacementMap[index];
                break;
            }
            case 2:{
                x -= shipPlacementMap[index];
                break;
            }
            case 3:{
                x += shipPlacementMap[index];
                break;
            }
        }
        if((x < 0 || x > 9) || (y < 0 || y > 9)){
            return target;
        }
        return playerBoard.getTile(x,y);
    }
    private void checkPossibleEnemyShipDirectionPhase1(){
        int x = target.x;
        int y = target.y;
        for(int i = 0;i<shipPlacementMap.length;i++){
            shipPlacementMap[i] = 1;
        }
        getPossiblePosition(2,x,0);
        getPossiblePosition(3,x,9);
        getPossiblePosition(0,y,0);
        getPossiblePosition(1,y,9);
    }

    private void getPossiblePosition(int position, int coordinate,int borderValue){
        if(coordinate == borderValue){
            shipPlacementMap[position] = -1;
        }
    }

    private Tile chooseTargetNoLocked(){
        while (true){
            int x = random.nextInt(10);
            int y = random.nextInt(10);
            Tile tile = playerBoard.getTile(x,y);
            if(!tile.wasShot){
                return tile;
            }
        }
    }
}
