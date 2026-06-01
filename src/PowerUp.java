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
    public void draw(Graphics g){
        if (getImage() != null) {
            g.drawImage(getImage(), getXPos(), getYPos(), 32, 32, null);
        }
    }

    @Override
    public void update() {}

    public PowerUpTypes getType(){
        return type;
    }
}
