import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Bullet extends GameObject{
    private Directions direction;
    private final int speed;
    private boolean playerBullet;
    private int bulletLevel;

    public Bullet(int xPos, int yPos, Directions direction, boolean playerBullet, int bulletLevel, int speed,BufferedImage[] bulletImages){
        super(xPos, yPos, 8, 8);
        this.direction = direction;
        this.playerBullet = playerBullet;
        this.bulletLevel = bulletLevel;
        this.speed = speed;
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

    public void increaseBulletLevel(){
        if(bulletLevel < 3){
            bulletLevel++;
        }
    }
    public int getBulletLevel(){
        return bulletLevel;
    }
}
