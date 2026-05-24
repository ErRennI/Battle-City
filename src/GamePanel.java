import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.swing.*;


public class GamePanel extends JPanel implements Runnable{
    private SpriteManager spriteManager;
    private CollisionManager collisionManager;

    private Thread gameThread;

    private GameObject[][] map = new GameObject[16][16];
    private PlayerTank playerTank;


    private final BufferedImage brickWall;
    private final BufferedImage steelWall;
    private final BufferedImage bush;
    private final BufferedImage water;

    public GamePanel(){
        setBackground(Color.BLACK);
        setFocusable(true);

        spriteManager = new SpriteManager();
        collisionManager = new CollisionManager(map);

        setPreferredSize(new Dimension(512, 512));

        brickWall = spriteManager.getSprite(256, 0, 16, 16);
        steelWall = spriteManager.getSprite(256, 16, 16, 16);
        bush = spriteManager.getSprite(272, 32, 16, 16);
        water = spriteManager.getSprite(256, 32, 16,16);


        playerTank = new PlayerTank(300, 300, spriteManager.getPlayerTanks());

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

            }

            @Override
            public void keyReleased(KeyEvent e){
                playerTank.setMoving(false);
            }
        });

        for (int r = 0; r < 16; r++) {
            for (int c = 0; c < 16; c++) {

                if (r == 0) {
                    map[r][c] = new SteelWall(c * 32, 0, steelWall);
                }
                else if (r == 5 && (c == 8 || c == 9)) {
                    map[r][c] = new BrickWall(c * 32, r * 32, brickWall);
                }
            }
        }
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run(){
        while(gameThread != null){
            if(playerTank.isMoving()){
                if(collisionManager.checkTankCollision(playerTank)){
                    playerTank.setMoving(false);
                    playerTank.update();
                    playerTank.setMoving(true);
                }
                else{
                    playerTank.update();
                }
            }


            repaint();
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

        playerTank.draw(g);

        for(int r=0; r<16; r++) {
            for(int c=0; c<16; c++) {
                if(map[r][c] != null) map[r][c].draw(g);
            }
        }
    }
}
