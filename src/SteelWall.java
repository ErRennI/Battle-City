import java.awt.*;
import java.awt.image.BufferedImage;

public class SteelWall extends Wall{
    public SteelWall(int xPos, int yPos, BufferedImage image){
        super(xPos, yPos, 3);
        super.setImage(image);
    }
}
