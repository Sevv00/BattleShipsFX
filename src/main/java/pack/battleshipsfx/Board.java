package pack.battleshipsfx;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


import java.util.ArrayList;
import java.util.List;

public class Board extends Parent {
    private final VBox rows = new VBox();
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

    public Tile getTile(int x, int y){
        return (Tile)((HBox)rows.getChildren().get(y)).getChildren().get(x);
    }

    public Tile[] getNeighbours(int x, int y){
        Point2D[] points = new Point2D[] {
                new Point2D(x-1,y),
                new Point2D(x+1,y),
                new Point2D(x,y-1),
                new Point2D(x,y+1),
                new Point2D(x+1,y+1),
                new Point2D(x-1,y-1),
                new Point2D(x+1,y-1),
                new Point2D(x-1,y+1)
        };
        List<Tile> neighbours = new ArrayList<>();

        for (Point2D p : points){
            if(isPointValid(p)){
                neighbours.add(getTile((int)p.getX(), (int)p.getY()));
            }
        }
        return neighbours.toArray(new Tile[0]);
    }
    public boolean isPointValid(Point2D point){
        return isPointValid(point.getX(),point.getY());
    }

    public boolean isPointValid(double x, double y){
        return (x >= 0) && (x < 10) && (y >= 0) && (y < 10);
    }

}
