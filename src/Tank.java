import java.awt.image.BufferedImage;

public abstract class Tank extends GameObject {
    private int speed;
    private int health;
    private Directions direction = Directions.UP;
    private boolean isMoving = false;
    private final BufferedImage[] tankDirectionImages;

    public Tank(int xPos, int yPos, int speed, int health, BufferedImage[] images){
        super(xPos, yPos, 32, 32);
        this.speed = speed;
        this.health = health;
        tankDirectionImages = images;
    }
    public abstract void shoot();

    public void move() {
        if(!isMoving) return;

        int nextX = getXPos();
        int nextY = getYPos();

        if (direction == Directions.UP) {
            nextY -= speed;
            setImage(tankDirectionImages[0]);
        }
        else if (direction == Directions.DOWN) {
            nextY += speed;
            setImage(tankDirectionImages[1]);
        }
        else if (direction == Directions.LEFT) {
            nextX -= speed;
            setImage(tankDirectionImages[2]);
        }
        else if (direction == Directions.RIGHT) {
            nextX += speed;
            setImage(tankDirectionImages[3]);
        }

        if(nextX >= 0 && nextX <= (512 - getWidth()) && nextY >= 0 && nextY <= (512 - getHeight())){
            setXPos(nextX);
            setYPos(nextY);
        }
    }

    public int getSpeed(){
        return speed;
    }
    public void setSpeed(int speed){
        this.speed = speed;
    }

    public int getHealth(){
        return health;
    }
    public void setHealth(int health){
        this.health = health;
    }

    public Directions getDirection() { return direction; }
    public void setDirection(Directions direction) { this.direction = direction; }
    public void setMoving(boolean isMoving){
        this.isMoving = isMoving;
    }

}
