import java.awt.*;
import java.awt.image.BufferedImage;

public class BrickWall extends Wall{
    public BrickWall(int xPos, int yPos, BufferedImage image){
        super(xPos, yPos, 1);
        setImage(image);
    }

    @Override
    public void draw(Graphics g){
        g.drawImage(getImage(), getXPos(), getYPos(), 32,32, null);
    }

    @Override
    public void update(){}

}
