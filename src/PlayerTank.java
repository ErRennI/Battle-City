import java.awt.Graphics;

public class PlayerTank extends Tank{
    public boolean up, down, left, right;

    public PlayerTank(int xPos, int yPos){
        super(xPos, yPos, 1, 3);
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

    }
}
