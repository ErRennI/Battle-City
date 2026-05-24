import java.awt.Rectangle;

public class CollisionManager {
    private GameObject[][] map;

    public CollisionManager(GameObject[][] map){
        this.map = map;
    }

    public boolean checkTankCollision(Tank tank){
        if(!tank.isMoving()){
            return true;
        }

        int nextX = tank.getXPos();
        int nextY = tank.getYPos();
        int speed = tank.getSpeed();

        if(tank.getDirection() == Directions.UP){
            nextY -= speed;
        }
        else if(tank.getDirection() == Directions.DOWN){
            nextY += speed;
        }
        else if(tank.getDirection() == Directions.LEFT){
            nextX -= speed;
        }
        else if(tank.getDirection() == Directions.RIGHT){
            nextX += speed;
        }

        if(nextX < 0 || nextX > (512 - tank.getWidth()) || nextY < 0 || nextY > (512 - tank.getHeight())){
            return true;
        }

        Rectangle nextBounds = new Rectangle(nextX, nextY, tank.getWidth(), tank.getHeight());

        for(int r = 0; r < 16; r++){
            for(int c = 0; c < 16; c++){
                GameObject gameObject = map[r][c];

                if(gameObject != null && nextBounds.intersects(gameObject.getBounds())){
                    return true;
                }
            }
        }
        return false;
    }
}
