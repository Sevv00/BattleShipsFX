package pack.battleshipsfx;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Board extends Parent {
    private VBox rows = new VBox();
    private final boolean enemy;
    public int shipsCount = 5;

    public Board(boolean enemy, EventHandler <? super MouseEvent> handler){
        this.enemy = enemy;
        for (int y = 0; y < 10; y++){
            HBox row = new HBox();
            for (int x = 0; x < 10; x++){
                Tile c = new Tile(x,y,this);
                c.setOnMouseClicked(handler);
                row.getChildren().add(c);
            }
            rows.getChildren().add(row);
        }
        getChildren().add(rows);
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

    public Tile getTile(int x, int y){
        return (Tile)((HBox)rows.getChildren().get(y)).getChildren().get(x);
    }

    private Tile[] getNeighbours(int x, int y){
        Point2D[] points = new Point2D[] {
          new Point2D(x-1,y),
          new Point2D(x+1,y),
          new Point2D(x,y-1),
          new Point2D(x,y+1)
        };
        List<Tile> neighbours = new ArrayList<>();

        for (Point2D p : points){
            if(isPointValid(p)){
                neighbours.add(getTile((int)p.getX(), (int)p.getY()));
            }
        }
        return neighbours.toArray(new Tile[0]);
    }

    private boolean validateShipPlacement(Ship ship,int x, int y){
        int length = ship.getShipType();
        if(ship.isInVerticalPosition()){
            for (int i = y; i < y + length; i++){
                if(!isPointValid(x,i)){
                    return false;
                }
                Tile tile = getTile(x,i);
                if(tile.ship != null){
                    return false;
                }

                for(Tile neighbour: getNeighbours(x,i)){
                    if(!isPointValid(x,i)){
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
                if(!isPointValid(i,y)){
                    return false;
                }
                Tile tile = getTile(i,y);
                if(tile.ship != null){
                    return false;
                }

                for(Tile neighbour: getNeighbours(i,y)){
                    if(!isPointValid(i,y)){
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

    private boolean isPointValid(Point2D point){
        return isPointValid(point.getX(),point.getY());
    }

    private boolean isPointValid(double x, double y){
        return (x >= 0) && (x < 10) && (y >= 0) && (y < 10);
    }

}
