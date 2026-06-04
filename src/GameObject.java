import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class GameObject {
    private int xPos;
    private int yPos;
    private int width;
    private int height;
    private BufferedImage image;
    private boolean isActive = true;

    public GameObject(int xPos, int yPos, int width, int height){
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics g){
        g.drawImage(getImage(), getXPos(), getYPos(),32,32, null);
    }

    public void deleteGameObject(){
        this.isActive = false;
    }

    public abstract void update();

    public Rectangle getBounds(){
        return new Rectangle(xPos, yPos, width, height);
    }

    public int getXPos() {
        return xPos;
    }
    public void setXPos(int xPos){
        this.xPos = xPos;
    }

    public int getYPos() {
        return yPos;
    }
    public void setYPos(int yPos){
        this.yPos = yPos;
    }

    public int getWidth(){
        return this.width;
    }
    public int getHeight(){
        return this.height;
    }

    public BufferedImage getImage(){
        return image;
    }
    public void setImage(BufferedImage image){
        this.image = image;
    }

    public void setActive(boolean state){
        this.isActive = state;
    }

}
