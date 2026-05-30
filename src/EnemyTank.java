import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class EnemyTank extends Tank {
    private Random random = new Random();
    private BufferedImage[] bulletImages;
    private int moveTimer = 0;
    private int shootTimer = 0;

    public EnemyTank(int xPos, int yPos, BufferedImage[] tankImages, BufferedImage[] bulletImages){
        super(xPos, yPos, 1, 1, tankImages);
        this.bulletImages = bulletImages;
        setMoving(true);
        setDirection(Directions.DOWN);
    }

    @Override
    public Bullet shoot() {
        int bulletX = getXPos() + 12;
        int bulletY = getYPos() + 12;

        return new Bullet(bulletX, bulletY, getDirection(), false, 1, bulletImages);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(super.getImage(), getXPos(), getYPos(),32,32, null);
    }

    @Override
    public void update() {
        super.move();

        moveTimer++;
        shootTimer++;

        if(moveTimer > 120){
            changeDirection();
            moveTimer = 0;
        }
    }

    public void changeDirection(){
        Directions[] direction = Directions.values();
        setDirection(direction[random.nextInt(direction.length)]);
    }

    public boolean shouldShoot(){
        if(shootTimer > (90 + random.nextInt(60))){
            shootTimer = 0;
            return true;
        }
        return false;
    }
}
