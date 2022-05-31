package pack.battleshipsfx;

import javafx.scene.Parent;

public class Ship extends Parent {
    private final int shipType;
    private int health;
    private final boolean verticalPosition;

    public Ship(int type, boolean position){
        this.shipType = type;
        this.health = type;
        this.verticalPosition = position;
    }

    public void hit(){
        health = health - 1;
    }

    public boolean isOperational(){
        return health > 0;
    }

    public int getShipType() {
        return shipType;
    }

    public boolean isInVerticalPosition() {
        return verticalPosition;
    }
}
