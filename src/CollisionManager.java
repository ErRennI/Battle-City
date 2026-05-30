import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;

public class CollisionManager {
    private GameObject[][] map;
    private PlayerTank playerTank;
    private ArrayList<EnemyTank> enemyTanks;

    public CollisionManager(GameObject[][] map, PlayerTank playerTank, ArrayList<EnemyTank> enemyTanks){
        this.map = map;
        this.playerTank = playerTank;
        this.enemyTanks = enemyTanks;
    }

    public boolean checkTankCollision(Tank tank){
        if(!tank.isMoving()){
            return false;
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

                if(gameObject != null && !(gameObject instanceof Bush) && nextBounds.intersects(gameObject.getBounds())){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkBulletCollision(Bullet bullet){
        Rectangle bulletBounds = bullet.getBounds();

        if(bullet.isPlayerBullet()){
            Iterator<EnemyTank> enemyTankIterator = enemyTanks.iterator();
            while(enemyTankIterator.hasNext()){
                EnemyTank enemy = enemyTankIterator.next();
                if(bulletBounds.intersects(enemy.getBounds())){
                    enemyTankIterator.remove();
                    return true;
                }
            }
        }
        else{
            if(bulletBounds.intersects(playerTank.getBounds())){
                playerTank.setHealth(playerTank.getHealth() - 1);

                if(playerTank.getHealth() <= 0){
                    //TODO gameOver
                }
                return true;
            }
        }


        for(int r = 0; r < 16; r++){
            for(int c = 0; c < 16; c++){
                GameObject gameObject = map[r][c];

                if(gameObject != null && bulletBounds.intersects(gameObject.getBounds())){

                    if (gameObject instanceof Bush || gameObject instanceof Water) {
                        continue;
                    }

                    if(gameObject instanceof Eagle eagle){
                        if(eagle.isAlive()){
                            eagle.destroy();
                            //TODO: finishes the game
                        }
                        return true;
                    }

                    if(gameObject instanceof BrickWall){
                        map[r][c] = null;
                    }

                    if((gameObject instanceof SteelWall) && bullet.getBulletLevel() == 3){
                        map[r][c] = null;
                    }

                    return true;
                }

            }
        }
        return false;
    }

}
