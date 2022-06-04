package pack.battleshipsfx;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends Rectangle {
    public int x,y;
    public Ship ship = null;
    public boolean wasShot = false;
    private final Board board;
    public Tile(int x,int y,Board board){
        super(30,30);
        this.x = x;
        this.y = y;
        this.board = board;
        setFill(Color.AQUA);
        setStroke(Color.DARKGREY);
    }

}
