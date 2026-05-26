import java.awt.image.BufferedImage;
import java.awt.Graphics;

public class Eagle extends GameObject{
    private boolean isAlive = true;
    private BufferedImage deadImage;

    public Eagle(int xPos, int yPos, BufferedImage[] eagleImages){
        super(xPos, yPos, 32, 32);
        setImage(eagleImages[0]);
        deadImage = eagleImages[1];
    }

    @Override
    public void draw(Graphics g){
        g.drawImage(getImage(), getXPos(), getYPos(), 32, 32, null);
    }

    @Override
    public void update(){}

    public boolean isAlive() {
        return isAlive;
    }

    public void destroy(){
        this.isAlive = false;
        setImage(deadImage);
    }
}
