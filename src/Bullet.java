import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Bullet extends GameObject{
    private Directions direction;
    private final int speed = 4;
    private boolean playerBullet;

    public Bullet(int xPos, int yPos, Directions direction, boolean playerBullet, BufferedImage[] bulletImages){
        super(xPos, yPos, 8, 8);
        this.direction = direction;
        this.playerBullet = playerBullet;
        setImage(bulletImages[direction.ordinal()]);
    }

    @Override
    public void draw(Graphics g){
        g.drawImage(super.getImage(), getXPos(), getYPos(), 8, 8, null);
    }

    @Override
    public void update(){
        if(direction == Directions.UP) setYPos(getYPos() - speed);
        else if(direction == Directions.DOWN) setYPos(getYPos() + speed);
        else if(direction == Directions.LEFT) setXPos(getXPos() - speed);
        else if(direction == Directions.RIGHT) setXPos(getXPos() + speed);
    }

    public boolean isPlayerBullet(){
        return playerBullet;
    }
}
