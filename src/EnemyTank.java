import java.awt.*;

public class EnemyTank extends Tank {

    public EnemyTank(int xPos, int yPos){
        super(xPos, yPos, 1, 1);
    }

    @Override
    public void shoot() {

    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(super.getImage(), getXPos(), getYPos(), null);
    }

    @Override
    public void update() {

    }
}
