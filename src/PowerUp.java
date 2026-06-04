import java.awt.*;
import java.awt.image.BufferedImage;

public class PowerUp extends GameObject{
    public enum PowerUpTypes{
        SHIELD,
        CLOCK,
        SHOVEL,
        STAR,
        BOMB,
        TANK
    }

    private final PowerUpTypes type;

    public PowerUp(int xPos, int yPos, PowerUpTypes type, BufferedImage image){
        super(xPos, yPos, 32, 32);
        this.type = type;
        setImage(image);
    }

    @Override
    public void update() {}

    public PowerUpTypes getType(){
        return type;
    }
}
