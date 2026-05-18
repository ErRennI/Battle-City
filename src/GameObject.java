import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class GameObject {
    private int xPos;
    private int yPos;
    private int width;
    private int height;
    private BufferedImage Image;

    public GameObject(int xPos, int yPos, int width, int height){
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
    }

    public abstract void draw(Graphics g);

    public abstract void update();

    public Rectangle getBounds(){
        return new Rectangle(xPos, yPos, width, height);
    }
}
