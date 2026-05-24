import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class SpriteManager {
    private BufferedImage spriteSheet;

    public SpriteManager(){
        try {
            spriteSheet = ImageIO.read(getClass().getResourceAsStream("SpriteSheet.png"));
        }
        catch(IOException e){
            System.err.println("Image load was unsuccessful, retry.");
            System.exit(1);
        }
    }

    public BufferedImage getSprite(int x, int y, int width, int height){
        return spriteSheet.getSubimage(x, y, width, height);
    }
    public BufferedImage[][] getSpriteGrid(int startX, int startY, int cols, int rows, int spriteWidth, int spriteHeight) {
        BufferedImage[][] sprites = new BufferedImage[rows][cols];


        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int x = startX + (c * spriteWidth);
                int y = startY + (r * spriteHeight);
                sprites[r][c] = spriteSheet.getSubimage(x, y, spriteWidth, spriteHeight);
            }
        }
        return sprites;
    }

    public BufferedImage[] getPlayerTanks(){
        SpriteManager manager = new SpriteManager();
        BufferedImage[] images = {manager.getSprite(0,0,16,16), manager.getSprite(64,0,16,16),
                manager.getSprite(32,0,16, 16), manager.getSprite(96,0,16,16)};
        return images;
    }

    public BufferedImage[] getEnemyTanks(){
        SpriteManager manager = new SpriteManager();
        BufferedImage[] images = {manager.getSprite(128,128,16,16), manager.getSprite(192,0,16,16),
                manager.getSprite(160,0,16, 16), manager.getSprite(224,0,16,16)};
        return images;
    }
}
