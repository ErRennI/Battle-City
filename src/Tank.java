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
        setImage(tankDirectionImages[0]);
    }
    public abstract void shoot();

    public void move() {
        setImage(tankDirectionImages[direction.ordinal()]);

        if(isMoving){
            if (direction == Directions.UP)         setYPos(getYPos() - speed);
            else if (direction == Directions.DOWN)   setYPos(getYPos() + speed);
            else if (direction == Directions.LEFT)   setXPos(getXPos() - speed);
            else if (direction == Directions.RIGHT)  setXPos(getXPos() + speed);
        }

    }

    public boolean isMoving() {
        return isMoving;
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
