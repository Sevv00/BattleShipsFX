package pack.battleshipsfx;

public class ShipPlacementValidation {

    private Board board;

    public ShipPlacementValidation(Board board){
        this.board = board;
    }

    private boolean validateShipPlacement(Ship ship,int x, int y){
        int length = ship.getShipType();
        if(ship.isInVerticalPosition()){
            for (int i = y; i < y + length; i++){
                if(!board.isPointValid(x,i)){
                    return false;
                }
                Tile tile = board.getTile(x,i);
                if(tile.ship != null){
                    return false;
                }

                for(Tile neighbour: board.getNeighbours(x,i)){
                    if(!board.isPointValid(x,i)){
                        return false;
                    }
                    if(neighbour.ship != null){
                        return false;
                    }
                }
            }
        }
        else{
            for (int i = x; i < x + length; i++){
                if(!board.isPointValid(i,y)){
                    return false;
                }
                Tile tile = board.getTile(i,y);
                if(tile.ship != null){
                    return false;
                }

                for(Tile neighbour: board.getNeighbours(i,y)){
                    if(!board.isPointValid(i,y)){
                        return false;
                    }
                    if(neighbour.ship != null){
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
