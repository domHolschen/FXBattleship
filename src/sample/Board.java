package sample;
import java.util.ArrayList;
import java.util.Random;

public class Board {
    private int[][] arr = new int[10][10];
    private ArrayList<Ship> shipArrayList = new ArrayList<>();
    private int missiles = 45, shipCount = 0, shipSunkCount = 0, level = 0;
    private boolean isActive = true;
    String levelString = "";
    Random randy = new Random();

    public void clearBoard() {
        for (int i=0;i<10;i++) {
            for (int j=0;j<10;j++) {
                arr[i][j] = 0;
            }
        }
        shipArrayList.clear();
        shipSunkCount = 0;
        shipCount = 0;
    }

    public int getTileId(int tileX,int tileY) {
        return arr[tileX][tileY];
    }

    public void setTileId(int tileId, int tileX, int tileY) {
        this.arr[tileX][tileY] = tileId;
    }

    public int getMissiles() {
        return this.missiles;
    }

    public void setMissiles(int missiles) {
        this.missiles = missiles;
    }

    public void addMissiles(int missiles) {
        this.missiles += missiles;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public ArrayList<Ship> getShipList() {
        return shipArrayList;
    }

    public int getShipCount() {
        return shipCount;
    }

    public int getShipSunkCount() {
        return shipSunkCount;
    }

    public void setShipSunkCount(int shipSunkCount) {
        this.shipSunkCount = shipSunkCount;
    }

    public void setLevelString (String levelString) {
        this.levelString = levelString;
    }

    public String getLevelString () {
        return levelString;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setTileLine(int startX, int startY, int length, boolean isHorizontal, int tileId) {
        if (isHorizontal) {
            for (int i=0; i<length; i++) {
                arr[startX+i][startY] = tileId;
            }
        } else {
            for (int i=0; i<length; i++) {
                arr[startX][startY+i] = tileId;
            }
        }
    }

    public void placeShip(int length) {
        boolean isHorizontal = randy.nextBoolean(), invalidLocation = false;
        int shipX, shipY;
        if (isHorizontal) {
            do {
                invalidLocation = false;
                shipX = randy.nextInt(11-length);
                shipY = randy.nextInt(10);
                for (int i=0; i<length; i++) {
                    if (arr[shipX+i][shipY] == 1) {
                        invalidLocation = true;
                    }
                }
            } while (invalidLocation);
            setTileLine(shipX, shipY, length, true, 1);
        } else {
            do {
                invalidLocation = false;
                shipX = randy.nextInt(10);
                shipY = randy.nextInt(11-length);
                for (int i=0; i<length; i++) {
                    if (arr[shipX][shipY+i] == 1) {
                        invalidLocation = true;
                    }
                }
            } while (invalidLocation);
            setTileLine(shipX, shipY, length, false, 1);
        }
        Ship activeShip = new Ship(shipX, shipY, length, isHorizontal);
        shipArrayList.add(activeShip);
        this.shipCount++;
    }
}
