import java.awt.Graphics;


public class PlayerTank extends Tank{
    public PlayerTank(int xPos, int yPos){
        super(xPos, yPos, 2, 3);
    }

    @Override
    public void shoot() {

    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(super.getImage(), super.getXPos(), super.getYPos(), null);
    }

    @Override
    public void update() {
        super.move();
    }
}
