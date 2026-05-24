import java.awt.Graphics;
import java.awt.image.BufferedImage;


public class PlayerTank extends Tank{


    public PlayerTank(int xPos, int yPos, BufferedImage[] tankImages){
        super(xPos, yPos, 2, 3, tankImages);
    }

    @Override
    public void shoot() {

    }


    @Override
    public void draw(Graphics g) {
        g.drawImage(super.getImage(), super.getXPos(), super.getYPos(), 32, 32, null);
    }

    @Override
    public void update() {
        super.move();
    }
}
