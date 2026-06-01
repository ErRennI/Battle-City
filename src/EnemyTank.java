import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.CookieHandler;
import java.util.Random;

public class EnemyTank extends Tank {
    private Random random = new Random();
    private BufferedImage[] bulletImages;
    private int bulletSpeed;
    private int moveTimer = 0;
    private int shootTimer = 0;

    public EnemyTank(int xPos, int yPos, BufferedImage[] tankImages, int speed, int bulletSpeed,BufferedImage[] bulletImages){
        super(xPos, yPos, speed, 1, tankImages);
        this.bulletImages = bulletImages;
        this.bulletSpeed = bulletSpeed;
        setMoving(true);
        setDirection(Directions.DOWN);
    }

    @Override
    public Bullet shoot() {
        int bulletX = getXPos() + 12;
        int bulletY = getYPos() + 12;

        return new Bullet(bulletX, bulletY, getDirection(), false, 1,  bulletSpeed,bulletImages);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(super.getImage(), getXPos(), getYPos(),32,32, null);
    }

    @Override
    public void update() {
        moveTimer++;
        shootTimer++;
    }

    public void handleMovement(CollisionManager collisionManager){
        if (moveTimer > 120) {
            changeDirection();
            moveTimer = 0;
        }
        if (!collisionManager.checkTankCollision(this)) {
            this.move();
        } else {
            changeDirection();
            moveTimer = 0;
        }
    }

    public void changeDirection(){
        Directions[] directions = Directions.values();
        Directions newDirection;

        do {
            newDirection = directions[random.nextInt(directions.length)];
        } while (newDirection == getDirection());

        setDirection(newDirection);
    }

    public boolean shouldShoot(){
        if(shootTimer > (90 + random.nextInt(60))){
            shootTimer = 0;
            return true;
        }
        return false;
    }
}
