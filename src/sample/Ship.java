package sample;

public class Ship {
    private int tileX, tileY, length;
    private boolean isHorizontal;
    public Ship(int tileX, int tileY, int length, boolean isHorizontal) {
        this.tileX = tileX;
        this.tileY = tileY;
        this.length = length;
        this.isHorizontal = isHorizontal;
    }
    public boolean isShipSunk(Board gboard) {
        int hitCellCount = 0;
        for (int i=0;i<length;i++) {
            if (isHorizontal) {
                if (gboard.getTileId(tileX+i, tileY) == 3 || gboard.getTileId(tileX+i, tileY) == 5) {
                    hitCellCount++;
                }
            } else {
                if (gboard.getTileId(tileX, tileY+i) == 3 || gboard.getTileId(tileX, tileY+i) == 5) {
                    hitCellCount++;
                }
            }
        }
        if (length == hitCellCount) {
            gboard.setTileLine(tileX, tileY, length, isHorizontal, 5); //sets the red x squares to black x squares
            return true;
        }
        return false;
    }
}
