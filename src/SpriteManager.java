import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class SpriteManager {
    private BufferedImage spriteSheet;

    public SpriteManager(){
        try {
            spriteSheet = ImageIO.read(new File("SpriteSheet"));
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

}
