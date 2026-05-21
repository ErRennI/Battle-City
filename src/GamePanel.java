import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;


public class GamePanel extends JPanel implements Runnable{
    private PlayerTank playerTank;
    private SpriteManager spriteManager;
    private Thread gameThread;

    public GamePanel(){
        setBackground(Color.BLACK);
        setFocusable(true);
        spriteManager = new SpriteManager();

        playerTank = new PlayerTank(300, 300);
        playerTank.setImage(spriteManager.getSprite(0,0,16,16));

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();

                if(key == KeyEvent.VK_W){
                    playerTank.setDirection(Directions.UP);
                }
                else if(key == KeyEvent.VK_S){
                    playerTank.setDirection(Directions.DOWN);
                }
                else if(key == KeyEvent.VK_A){
                    playerTank.setDirection(Directions.LEFT);
                }
                else if(key == KeyEvent.VK_D){
                    playerTank.setDirection(Directions.RIGHT);
                }

                playerTank.setMoving(true);
            }

            @Override
            public void keyReleased(KeyEvent e){
                playerTank.setMoving(false);
            }
        });
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run(){
        while(gameThread != null){
            playerTank.update();
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
    }
}
