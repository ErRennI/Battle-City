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



public class GamePanel extends JPanel implements Runnable{
    private SpriteManager spriteManager;
    private CollisionManager collisionManager;

    private GameFrame gameFrame;
    private Thread gameThread;

    private GameObject[][] map = new GameObject[16][16];
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private ArrayList<EnemyTank> enemyTanks = new ArrayList<>();
    private int enemySpawnTimer = 0;
    private PlayerTank playerTank;
    private Eagle playerBase;

    private final BufferedImage brickWall;
    private final BufferedImage steelWall;
    private final BufferedImage bush;
    private final BufferedImage water;
    private BufferedImage[] bulletImage = new BufferedImage[4];

    public GamePanel(GameFrame gameFrame){
        this.gameFrame = gameFrame;
        setBackground(Color.BLACK);
        setFocusable(true);

        spriteManager = new SpriteManager();

        setPreferredSize(new Dimension(512, 512));

        brickWall = spriteManager.getSprite(256, 0, 16, 16);
        steelWall = spriteManager.getSprite(256, 16, 16, 16);
        bush = spriteManager.getSprite(272, 32, 16, 16);
        water = spriteManager.getSprite(256, 32, 16,16);
        bulletImage = spriteManager.getBulletSprites();

        playerTank = new PlayerTank(300, 300, spriteManager.getPlayerTanks(), bulletImage);
        collisionManager = new CollisionManager(map, playerTank, enemyTanks);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();

                if(key == KeyEvent.VK_W){
                    playerTank.setDirection(Directions.UP);
                    playerTank.setMoving(true);
                }
                else if(key == KeyEvent.VK_S){
                    playerTank.setDirection(Directions.DOWN);
                    playerTank.setMoving(true);
                }
                else if(key == KeyEvent.VK_A){
                    playerTank.setDirection(Directions.LEFT);
                    playerTank.setMoving(true);
                }
                else if(key == KeyEvent.VK_D){
                    playerTank.setDirection(Directions.RIGHT);
                    playerTank.setMoving(true);
                }
                else if(key == KeyEvent.VK_SPACE){
                    short playerBulletCount = 0;
                    for(Bullet b: bullets){
                        if(b.isPlayerBullet()){
                            playerBulletCount++;
                        }
                    }

                    if(playerBulletCount < playerTank.getMaxBullets()){
                        bullets.add(playerTank.shoot());
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e){
                int key = e.getKeyCode();

                if(key == KeyEvent.VK_W || key == KeyEvent.VK_S || key == KeyEvent.VK_A || key == KeyEvent.VK_D) {
                    playerTank.setMoving(false);
                }
            }
        });

        for (int r = 0; r < 16; r++) {
            for (int c = 0; c < 16; c++) {

                if (r == 0) {

                }
                else if (r == 5 && (c == 8 || c == 9)) {
                    map[r][c] = new BrickWall(c * 32, r * 32, brickWall);
                }
            }
        }
        playerBase = new Eagle(8 * 32, 15 * 32, spriteManager.getEagleSprites());
        map[15][8] = playerBase;
    }

    public void startGameThread(String mapName){
        if(gameThread != null) {
            gameThread = null;
        }

        loadMapFromJSONForGame(mapName);
        if (playerTank != null) {
            gameFrame.updateLivesUI(playerTank.getHealth());
        }

        gameThread = new Thread(this);
        gameThread.start();
    }


    @Override
    public void run(){
        while(gameThread != null){
            if(playerTank.isMoving()) {
                if (!collisionManager.checkTankCollision(playerTank)) {
                    playerTank.update();
                }
            }

            enemySpawnTimer++;

            if(enemySpawnTimer > 300 && enemyTanks.size() < 4){
                enemySpawnTimer = 0;

                int[] spawnXPositions = { 0, 224, 448 };

                int randomSpawnX = new Random().nextInt(3);
                int spawnX = spawnXPositions[randomSpawnX];
                int spawnY = 0;

                int spawnCol = spawnX / 32;
                int spawnRow = 0;

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
                    enemyTanks.add(new EnemyTank(spawnX, spawnY, spriteManager.getEnemyTanks(), bulletImage));
                } else {
                    enemySpawnTimer = 240;
                }
            }

            for(EnemyTank enemy: enemyTanks){
                if(collisionManager.checkTankCollision(enemy)){
                    enemy.changeDirection();
                }
                else{
                    enemy.update();
                }

                if(enemy.shouldShoot()){
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

                if(b.getXPos() < 0 || b.getXPos() > 512 || b.getYPos() < 0 || b.getYPos() > 512){
                    iterator.remove();
                }
            }

            repaint();
            if (playerTank != null) {
                gameFrame.updateLivesUI(playerTank.getHealth());
            }
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for(int r=0; r<16; r++) {
            for(int c=0; c<16; c++) {
                if(map[r][c] != null && !(map[r][c] instanceof Bush)) map[r][c].draw(g);
            }
        }
        playerTank.draw(g);

        for (EnemyTank enemy : enemyTanks) {
            enemy.draw(g);
        }

        for (Bullet b : bullets) {
            b.draw(g);
        }

        for(int r=0; r<16; r++) {
            for(int c=0; c<16; c++) {
                if(map[r][c] instanceof Bush) {
                    map[r][c].draw(g);
                }
            }
        }

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

            for(int r = 0; r < 16; r++){
                String[] cols = rows[r].split(",");
                for(int c = 0; c < 16; c++) {
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

            map[15][8] = playerBase;
        } catch (Exception e) {
            System.err.println("There was a exception while reading from file!");
        }
    }
}
