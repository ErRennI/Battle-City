import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener {
    private boolean up, down, left, right, shoot;

    @Override
    public void keyPressed(KeyEvent event){
        handleKey(event.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent event){
        handleKey(event.getKeyCode(), false);
    }

    private void handleKey(int keyCode, boolean isPressed){
        if(keyCode == KeyEvent.VK_SPACE){
            shoot = isPressed;
            return;
        }

        if(isPressed){
            up = false;
            down = false;
            left = false;
            right = false;

            if(keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP){
                up = true;
            }
            else if(keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN){
                down = true;
            }
            else if(keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT){
                left = true;
            }
            else if(keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT){
                right = true;
            }
        } else {

            if(keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP){
                up = false;
            }
            else if(keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN){
                down = false;
            }
            else if(keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT){
                left = false;
            }
            else if(keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT){
                right = false;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    public void resetAllKeys() {
        this.up = false;
        this.down = false;
        this.left = false;
        this.right = false;
        this.shoot = false;
    }

    public boolean isUpPressed(){ return up; }
    public boolean isDownPressed(){ return down; }
    public boolean isLeftPressed(){ return left; }
    public boolean isRightPressed(){ return right; }
    public boolean isShootPressed(){ return shoot; }
}
