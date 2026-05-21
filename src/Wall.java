import java.awt.*;

public abstract class Wall extends GameObject{
    private int wallDurability;
    public Wall(int xPos, int yPos, int wallDurability){
        super(xPos, yPos, 16, 16);
        this.wallDurability = wallDurability;
    }

    public int getWallDurability(){
        return wallDurability;
    }
}
