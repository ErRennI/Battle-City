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
        return new BufferedImage[]{
                this.getSprite(0, 0, 16, 16),   this.getSprite(16, 0, 16, 16),  // UP (Normal & Alt Frame)
                this.getSprite(64, 0, 16, 16),  this.getSprite(80, 0, 16, 16),  // DOWN
                this.getSprite(32, 0, 16, 16),  this.getSprite(48, 0, 16, 16),  // LEFT
                this.getSprite(96, 0, 16, 16),  this.getSprite(112, 0, 16, 16)  // RIGHT
        };
    }

    public BufferedImage[] getEnemyTanks(){
        return new BufferedImage[]{
                this.getSprite(128, 0, 16, 16), this.getSprite(144, 0, 16, 16), // UP
                this.getSprite(192, 0, 16, 16), this.getSprite(208, 0, 16, 16), // DOWN
                this.getSprite(160, 0, 16, 16), this.getSprite(176, 0, 16, 16), // LEFT
                this.getSprite(224, 0, 16, 16), this.getSprite(240, 0, 16, 16)  // RIGHT
        };
    }

    public BufferedImage[] getBulletSprites() {
        BufferedImage[] bulletImages = {this.getSprite(323, 102, 3, 4), this.getSprite(323, 102, 3, 4),
                this.getSprite(330, 103, 4, 3), this.getSprite(330, 103, 4, 3)};
        return bulletImages;
    }

    public BufferedImage[] getEagleSprites(){
        BufferedImage[] eagleImage = {this.getSprite(304, 32, 16, 16), this.getSprite(320, 32, 16, 16)};
        return eagleImage;
    }

    public BufferedImage getGameOver(){
        return this.getSprite(288, 184, 32, 16);
    }

    public BufferedImage getPowerUps(int typeIndex){
        return this.getSprite(256 + (typeIndex * 16), 112, 16, 16);
    }
}
