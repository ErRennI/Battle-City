import java.awt.*;
import java.awt.image.BufferedImage;

public class SteelWall extends Wall{
    public SteelWall(int xPos, int yPos, BufferedImage image){
        super(xPos, yPos, 3);
        super.setImage(image);
    }

    @Override
    public void draw(Graphics g){
        g.drawImage(getImage(), getXPos(), getYPos(), null);
    }

    @Override
    public void update(){}
}
