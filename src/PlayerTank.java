import java.awt.Graphics;
import java.awt.image.BufferedImage;


public class PlayerTank extends Tank{
    private BufferedImage[] bulletImages;
    private short maxBullets = 1;

    public PlayerTank(int xPos, int yPos, BufferedImage[] tankImages, BufferedImage[] bulletImages){
        super(xPos, yPos, 2, 3, tankImages);
        this.bulletImages = bulletImages;
    }

    @Override
    public Bullet shoot() {
        return new Bullet(getXPos() + 12, getYPos() + 12, getDirection(), true, bulletImages);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(super.getImage(), super.getXPos(), super.getYPos(), 32, 32, null);
    }

    @Override
    public void update() {
        super.move();
    }

    public short getMaxBullets() {
        return maxBullets;
    }
}
