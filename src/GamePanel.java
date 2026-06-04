import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.*;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import  java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GamePanel extends JPanel implements Runnable{
    private final SpriteManager spriteManager;
    private CollisionManager collisionManager;
    private final KeyManager keyManager;

    private GameFrame gameFrame;
    private ExecutorService gameExecutor;

    private GameObject[][] map = new GameObject[13][13];
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private ArrayList<EnemyTank> enemyTanks = new ArrayList<>();
    private ArrayList<PowerUp> activePowerUps = new ArrayList<>();
    private int enemySpawnTimer = 0;
    private PlayerTank playerTank;
    private Eagle playerBase;

    private final BufferedImage brickWall;
    private final BufferedImage steelWall;
    private final BufferedImage bush;
    private final BufferedImage water;
    private BufferedImage[] bulletImage = new BufferedImage[4];

    private int enemyTankSpeed = 1;
    private int enemySpawnCooldown = 300;
    private int maxEnemyOnScreen = 4;
    private int enemyBulletSpeed = 4;

    private volatile boolean isPaused = false;
    private volatile boolean gameOver;
    private int score = 0;
    private long gameStartTime;
    private int destroyedEnemyCount = 0;

    private boolean wasShootPressedLastFrame = false;

    private boolean isEnemyFrozen = false;
    private long freezeEndTime = 0;
    private boolean isShovelActive = false;
    private long shovelEndTime = 0;
    private final int[][] baseWallCoords = {
            {11, 5}, {11, 6}, {11, 7},
            {12, 5},          {12, 7}
    };
    private GameObject[] previousBaseWallObjects = new GameObject[5];

    private Color gameOverBackground = new Color(0, 0, 0, 180);

    public GamePanel(GameFrame gameFrame){
        this.gameFrame = gameFrame;
        setBackground(Color.BLACK);
        setFocusable(true);

        spriteManager = new SpriteManager();
        setPreferredSize(new Dimension(416, 416));

        brickWall = spriteManager.getSprite(256, 0, 16, 16);
        steelWall = spriteManager.getSprite(256, 16, 16, 16);
        bush = spriteManager.getSprite(272, 32, 16, 16);
        water = spriteManager.getSprite(256, 32, 16,16);
        bulletImage = spriteManager.getBulletSprites();

        collisionManager = new CollisionManager(map, playerTank, enemyTanks, this);
        this.keyManager = new KeyManager();
        addKeyListener(keyManager);
    }

    public void startGameThread(String mapName){
        if(gameExecutor != null) {
             gameOver = true;
             gameExecutor.shutdown();

             try{
                 gameExecutor.awaitTermination(100, TimeUnit.MILLISECONDS);
             }
             catch(InterruptedException _){
                 System.err.println("Thread termination awaited but interrupted.");
             }
             gameExecutor = null;
        }

        gameOver = false;
        bullets.clear();
        enemyTanks.clear();
        activePowerUps.clear();
        enemySpawnTimer = 0;
        isShovelActive = false;
        previousBaseWallObjects = new GameObject[5];

        score = 0;
        destroyedEnemyCount = 0;
        gameStartTime = System.currentTimeMillis();

        for (int r = 0; r < 13; r++) {
            for (int c = 0; c < 13; c++) {
                map[r][c] = null;
            }
        }

        playerBase = new Eagle(6 * 32, 12 * 32, spriteManager.getEagleSprites());
        map[12][6] = playerBase;

        loadMapFromJSONForGame(mapName);

        playerTank = new PlayerTank(128, 384, spriteManager.getPlayerTanks(), bulletImage);

        collisionManager = new CollisionManager(map, playerTank, enemyTanks, this);

        gameFrame.updateLivesUI(playerTank.getHealth());

        gameExecutor = Executors.newSingleThreadExecutor();
        gameExecutor.execute(this);
    }


    @Override
    public void run(){
        while(gameExecutor != null && !gameOver){

            if (isPaused) {
                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

            if(playerTank != null){
                boolean movingThisFrame = false;

                if(keyManager.isUpPressed()){
                    alignToGrid(playerTank, true);
                    playerTank.setDirection(Directions.UP);
                    playerTank.setMoving(true);
                    movingThisFrame = true;
                }
                else if(keyManager.isDownPressed()){
                    alignToGrid(playerTank, true);
                    playerTank.setDirection(Directions.DOWN);
                    playerTank.setMoving(true);
                    movingThisFrame = true;
                }
                else if(keyManager.isLeftPressed()){
                    alignToGrid(playerTank, false);
                    playerTank.setDirection(Directions.LEFT);
                    playerTank.setMoving(true);
                    movingThisFrame = true;
                }
                else if(keyManager.isRightPressed()){
                    alignToGrid(playerTank, false);
                    playerTank.setDirection(Directions.RIGHT);
                    playerTank.setMoving(true);
                    movingThisFrame = true;
                }

                if(!movingThisFrame){
                    playerTank.setMoving(false);
                }

                if(playerTank.isMoving()){
                    if(!collisionManager.checkTankCollision(playerTank)){
                        playerTank.update();
                    }
                }

                if(keyManager.isShootPressed()){
                    if(!wasShootPressedLastFrame){
                        short playerBulletCount = 0;

                        for(Bullet b : bullets){
                            if(b.isPlayerBullet()){
                                playerBulletCount++;
                            }
                        }


                        if(playerBulletCount < playerTank.getMaxBullets()){
                            bullets.add(playerTank.shoot());
                        }
                    }
                    wasShootPressedLastFrame = true;
                } else {
                    wasShootPressedLastFrame = false;
                }
            }

            collisionManager.checkPlayerPowerUpCollision();

            if (isShovelActive && System.currentTimeMillis() > shovelEndTime) {
                isShovelActive = false;

                if (map[11][5] instanceof SteelWall) map[11][5] = previousBaseWallObjects[0];
                if (map[11][6] instanceof SteelWall) map[11][6] = previousBaseWallObjects[1];
                if (map[11][7] instanceof SteelWall) map[11][7] = previousBaseWallObjects[2];
                if (map[12][5] instanceof SteelWall) map[12][5] = previousBaseWallObjects[3];
                if (map[12][7] instanceof SteelWall) map[12][7] = previousBaseWallObjects[4];
            }

            enemySpawnTimer++;

            if(enemySpawnTimer > enemySpawnCooldown && enemyTanks.size() < maxEnemyOnScreen){
                enemySpawnTimer = 0;

                int[] spawnXPositions = { 0, 192, 384 };

                int randomSpawnX = new Random().nextInt(3);
                int spawnX = spawnXPositions[randomSpawnX];
                int spawnY = 0;

                Rectangle spawnBounds = new Rectangle(spawnX, spawnY, 32, 32);
                boolean isSpawnBlocked = false;

                if (spawnBounds.intersects(playerTank.getBounds())) {
                    isSpawnBlocked = true;
                } else {
                    for (EnemyTank enemy : enemyTanks) {
                        if (spawnBounds.intersects(enemy.getBounds())) {
                            isSpawnBlocked = true;
                            break;
                        }
                    }
                }

                if (!isSpawnBlocked) {
                    EnemyTank newEnemy = new EnemyTank(spawnX, spawnY, spriteManager.getEnemyTanks(), enemyTankSpeed, enemyBulletSpeed, bulletImage);
                    if (isEnemyFrozen) {
                        newEnemy.setMoving(false);
                    }
                    enemyTanks.add(newEnemy);
                } else {
                    enemySpawnTimer = enemySpawnCooldown - 60;
                }
            }

            if(isEnemyFrozen && System.currentTimeMillis() > freezeEndTime){
                isEnemyFrozen = false;

                for(EnemyTank enemy : enemyTanks){
                    enemy.setMoving(true);
                }
            }

            for(EnemyTank enemy: enemyTanks){
                enemy.update();
                if(!isEnemyFrozen) {
                    enemy.handleMovement(collisionManager);
                }
                else{
                    enemy.setMoving(false);
                }

                if(!isEnemyFrozen && enemy.shouldShoot()){
                    bullets.add(enemy.shoot());
                }
            }

            Iterator<Bullet> iterator = bullets.iterator();
            while(iterator.hasNext()){
                Bullet b = iterator.next();
                b.update();

                if(collisionManager.checkBulletCollision(b)){
                    iterator.remove();
                    continue;
                }

                if(b.getXPos() < 0 || b.getXPos() > 416 || b.getYPos() < 0 || b.getYPos() > 416){
                    iterator.remove();
                }
            }

            if (destroyedEnemyCount >= 20) {
                triggerGameOver();
            }
            repaint();
            if (playerTank != null) {
                gameFrame.updateLivesUI(playerTank.getHealth());
            }

            gameFrame.updateEnemiesUI(destroyedEnemyCount);

            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void triggerGameOver(){
        if (!gameOver) {
            this.gameOver = true;
            if (gameExecutor != null) {
                gameExecutor.shutdown();
            }
            repaint();
            ScoreManager.saveScore(gameFrame, score, gameStartTime);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(gameExecutor == null){
            return;
        }

        for(int r=0; r<13; r++) {
            for(int c=0; c<13; c++) {
                if(map[r][c] != null && !(map[r][c] instanceof Bush)) map[r][c].draw(g);
            }
        }
        playerTank.draw(g);

        for(EnemyTank enemy : enemyTanks) {
            enemy.draw(g);
        }

        for(Bullet b : bullets) {
            b.draw(g);
        }

        for(PowerUp power: activePowerUps){
            power.draw(g);
        }

        for(int r=0; r<13; r++) {
            for(int c=0; c<13; c++) {
                if(map[r][c] instanceof Bush) {
                    map[r][c].draw(g);
                }
            }
        }

        if(gameOver){
            g.setColor(gameOverBackground);
            g.fillRect(0, 0, getWidth(), getHeight());

            g.drawImage(spriteManager.getGameOver(), (getWidth() - 64) / 2, (getHeight() - 64) / 2, 64, 32,null);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    public void loadMapFromJSONForGame(String mapName) {
        File file = new File("maps.json");
        if(!file.exists()) return;

        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) sb.append(line.trim());

            String content = sb.toString();
            content = content.substring(1, content.length() - 1);

            String[] parts = content.split("],\"");
            String matrixData = null;
            for(String part : parts){
                String[] kv = part.split("\":");
                String key = kv[0].replace("\"", "").replace("{", "").trim();
                if(key.equals(mapName)){
                    matrixData = kv[1].trim();
                    if(!matrixData.endsWith("]")) matrixData += "]";
                    break;
                }
            }

            if(matrixData == null) return;

            matrixData = matrixData.replace("[[", "").replace("]]", "");
            String[] rows = matrixData.split("\\],\\[");

            for(int r = 0; r < 13; r++){
                String[] cols = rows[r].split(",");
                for(int c = 0; c < 13; c++) {

                    if((r == 12 && c == 6) || (r == 12 && c == 4)) {
                        continue;
                    }
                    int tileType = Integer.parseInt(cols[c].trim());
                    int xPos = c * 32;
                    int yPos = r * 32;

                    if(tileType == 0) map[r][c] = null;
                    else if(tileType == 1) map[r][c] = new BrickWall(xPos, yPos, brickWall);
                    else if(tileType == 2) map[r][c] = new SteelWall(xPos, yPos, steelWall);
                    else if(tileType == 3) map[r][c] = new Bush(xPos, yPos, bush);
                    else if(tileType == 4) map[r][c] = new Water(xPos, yPos, water);
                }
            }

            map[12][6] = playerBase;
        } catch (Exception e) {
            System.err.println("There was a exception while reading from file!");
        }
    }

    public void incrementScore(){
        this.score += 100;
        this.destroyedEnemyCount++;
    }

    public int getScore(){
        return this.score;
    }

    public long getGameStartTime() {
        return this.gameStartTime;
    }

    public void setEnemyDifficulty(String difficulty) {
        if (difficulty.equals("EASY")) {
            this.enemyTankSpeed = 1;
            this.enemySpawnCooldown = 300;
            this.maxEnemyOnScreen = 4;
            this.enemyBulletSpeed = 4;
        } else if (difficulty.equals("MEDIUM")) {
            this.enemyTankSpeed = 2;
            this.enemySpawnCooldown = 200;
            this.maxEnemyOnScreen = 5;
            this.enemyBulletSpeed = 5;
        } else if (difficulty.equals("HARD")) {
            this.enemyTankSpeed = 2;
            this.enemySpawnCooldown = 120;
            this.maxEnemyOnScreen = 6;
            this.enemyBulletSpeed = 7;
        }
    }

    public boolean togglePause(){
        if(gameExecutor != null && !gameOver){
            this.isPaused = !this.isPaused;
        }
        return this.isPaused;
    }

    public void alignToGrid(PlayerTank tank, boolean alignX){
        int remainder;
        if(alignX){
            int currentX = tank.getXPos();
            remainder = currentX % 16;
            if(remainder < 8){
                tank.setXPos(currentX - remainder);
            }
            else{
                tank.setXPos(currentX + (16 - remainder));
            }
        }
        else {
            int currentY = tank.getYPos();
            remainder = currentY % 16;
            if(remainder < 8){
                tank.setYPos(currentY - remainder);
            }
            else {
                tank.setYPos(currentY + (16 - remainder));
            }
        }
    }

    public void spawnPowerUpRandomly(int x, int y) {
        if (new Random().nextInt(100) < 30) {
            PowerUp.PowerUpTypes[] types = PowerUp.PowerUpTypes.values();
            PowerUp.PowerUpTypes randomType = types[new Random().nextInt(types.length)];


            BufferedImage pImg = spriteManager.getPowerUps(randomType.ordinal());

            activePowerUps.add(new PowerUp(x, y, randomType, pImg));
        }
    }

    public void activateShovelShield(int durationMs) {
        this.isShovelActive = true;
        this.shovelEndTime = System.currentTimeMillis() + durationMs;

        previousBaseWallObjects[0] = map[11][5];
        previousBaseWallObjects[1] = map[11][6];
        previousBaseWallObjects[2] = map[11][7];
        previousBaseWallObjects[3] = map[12][5];
        previousBaseWallObjects[4] = map[12][7];

        if (!isTileBlocked(11, 5)) map[11][5] = new SteelWall(5 * 32, 11 * 32, steelWall);
        if (!isTileBlocked(11, 6)) map[11][6] = new SteelWall(6 * 32, 11 * 32, steelWall);
        if (!isTileBlocked(11, 7)) map[11][7] = new SteelWall(7 * 32, 11 * 32, steelWall);
        if (!isTileBlocked(12, 5)) map[12][5] = new SteelWall(5 * 32, 12 * 32, steelWall);
        if (!isTileBlocked(12, 7)) map[12][7] = new SteelWall(7 * 32, 12 * 32, steelWall);
    }

    private boolean isTileBlocked(int r, int c) {
        Rectangle tileBounds = new Rectangle(c * 32, r * 32, 32, 32);

        if (playerTank != null && tileBounds.intersects(playerTank.getBounds())) return true;
        for (EnemyTank enemy : enemyTanks) {
            if (tileBounds.intersects(enemy.getBounds())) return true;
        }
        for (Bullet b : bullets) {
            if (tileBounds.intersects(b.getBounds())) return true;
        }
        return false;
    }

    public ArrayList<PowerUp> getActivePowerUps() {
        return activePowerUps;
    }

    public int getEnemyCurrentNumber(){
        return enemyTanks.size();
    }

    public void setEnemyFrozen(boolean frozen) {
        this.isEnemyFrozen = frozen;
    }

    public void setFreezeEndTime(long endTime) {
        this.freezeEndTime = endTime;
    }

}
