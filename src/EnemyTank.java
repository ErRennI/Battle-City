import java.awt.*;
import java.awt.image.BufferedImage;

public class EnemyTank extends Tank {

    public EnemyTank(int xPos, int yPos, BufferedImage[] tankImages){
        super(xPos, yPos, 1, 1, tankImages);
    }

    @Override
    public Bullet shoot() {
        return null;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(super.getImage(), getXPos(), getYPos(),32,32, null);
    }

    @Override
    public void update() {

    }

}
