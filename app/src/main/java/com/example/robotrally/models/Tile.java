package com.example.robotrally.models;

import com.example.robotrally.enumerations.ObstacleTypes;

public class Tile {

    private final boolean isObstacle;
    private final boolean isGoalTile;
    private final ObstacleTypes obstacleType;

    private boolean hasCollectable;
    private boolean isRoombaHere;

    public Tile(boolean isObstacle, boolean isGoalTile, boolean hasCollectable, ObstacleTypes obstacleType) {
        this.isObstacle = isObstacle;
        this.isGoalTile = isGoalTile;
        this.hasCollectable = hasCollectable;
        this.obstacleType = obstacleType;
        this.isRoombaHere = false;
    }

    public Tile(boolean isObstacle, boolean isGoalTile, boolean hasCollectable) {
        this(isObstacle, isGoalTile, hasCollectable, ObstacleTypes.None);
    }

    public boolean CanMoveTo() {
        return isObstacle;
    }

    public boolean IsGoalTile() {
        return isGoalTile;
    }

    public void SetIsRoombaHere(boolean newValue) throws Exception {
        if (newValue == isRoombaHere)
        {
            throw new Exception("Cannot set to what value already is");
        }

        isRoombaHere = newValue;
    }

    public boolean IsRoombaHere() {
        return isRoombaHere;
    }

    public void Collect() throws Exception {
        if (!hasCollectable)
        {
            throw new Exception("There is no item to collect");
        }
        hasCollectable = false;
    }

    public ObstacleTypes GetObstacleType() {
        return obstacleType;
    }

}