import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Water extends GameObject{
    public Water(int xPos, int yPos, BufferedImage image) {
        super(xPos, yPos, 32, 32);
        setImage(image);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(getImage(), getXPos(), getYPos(), 32, 32, null);
    }

    @Override
    public void update() {
    }

}
