import java.awt.*;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class MapEditor extends JPanel{
    private final SpriteManager spriteManager;
    private final GameObject[][] editedMap = new GameObject[16][16];

    private final BufferedImage bush;
    private final BufferedImage brickWall;
    private final BufferedImage steelWall;
    private final BufferedImage water;
    private final BufferedImage[] eagleImg;

    //Selected tool 0: Delete 1: brickWall 2: steelWall 3:Bush 4: Water
    private int selectedTool;

    public MapEditor(SpriteManager spriteManager){
        this.spriteManager = spriteManager;
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(512, 512));

        eagleImg = spriteManager.getEagleSprites();

        editedMap[15][8] = new Eagle(8 * 32, 15 * 32, eagleImg);

        brickWall = spriteManager.getSprite(256, 0, 16, 16);
        steelWall = spriteManager.getSprite(256, 16, 16, 16);
        bush = spriteManager.getSprite(272, 32, 16, 16);
        water = spriteManager.getSprite(256, 32, 16, 16);

        MouseAdapter mouseAdapter = new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){
                addTile(e.getX(), e.getY());
            }

            @Override
            public void mouseDragged(MouseEvent e){
                addTile(e.getX(), e.getY());
            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    private void addTile(int mouseX, int mouseY){
        int c = mouseX / 32;
        int r = mouseY / 32;

        if(r < 0 || r >= 16 || c < 0 || c >= 16) return;

        if (editedMap[r][c] instanceof Eagle) return;

        if ((r == 0 && (c == 0 || c == 7 || c == 14)) || (r == 15 && c == 8)) return;

        int xPos = c * 32;
        int yPos = r * 32;

        if(selectedTool == 0){
            editedMap[r][c] = null;
        }
        else if(selectedTool == 1){
            editedMap[r][c] = new BrickWall(xPos, yPos, brickWall);
        }
        else if(selectedTool == 2){
            editedMap[r][c] = new SteelWall(xPos, yPos, steelWall);
        }
        //TODO: Bush and water

        repaint();
    }

    public void setSelectedTool(int tool){
        this.selectedTool = tool;
    }

    public GameObject[][] getEditedMap(){
        return editedMap;
    }

    private String mapToMatrixString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int r = 0; r < 16; r++) {
            sb.append("[");
            for (int c = 0; c < 16; c++) {
                GameObject tile = editedMap[r][c];
                if (tile == null) sb.append("0");
                else if (tile instanceof BrickWall) sb.append("1");
                else if (tile instanceof SteelWall) sb.append("2");
                // TODO: İleride Bush için 3, Water için 4 ekleyebilirsin

                if (c < 15) sb.append(",");
            }
            sb.append("]");
            if (r < 15) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }



    public void saveMapToJSON(String mapName) {
        File file = new File("maps.json");
        String oldContent = "";

        if (file.exists() && file.length() > 0) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(file));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                oldContent = sb.toString().trim();

                int lastBracketIdx = oldContent.lastIndexOf("}");
                if (lastBracketIdx != -1) {
                    oldContent = oldContent.substring(0, lastBracketIdx);
                }
            } catch (IOException e) {
                System.err.println("Problem while loading functions.");
            } finally {
                try{
                    if(reader != null) reader.close();
                } catch (IOException e) {
                    System.err.println("Problem while closing reader");
                }
            }
        }

        BufferedWriter writer = null;
        try{
            writer = new BufferedWriter(new FileWriter(file));
            if (oldContent.isEmpty()) {
                writer.write("{\n");
            } else {
                writer.write(oldContent);
                writer.write(",\n");
            }

            writer.write("  \"" + mapName + "\": " + mapToMatrixString() + "\n");
            writer.write("}");

            JOptionPane.showMessageDialog(this, "Map '" + mapName + "' has been saved!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Exception happened while saving!", "Exception", JOptionPane.ERROR_MESSAGE);
        }finally {
            try{
                if(writer != null) writer.close();
            } catch (IOException e) {
                System.err.println("Couldn't close the writer!");
            }
        }
    }

    public void loadMapFromJSON(String mapName){
        File file = new File("maps.json");
        if(!file.exists()) return;

        BufferedReader reader = null;

        try{
            reader = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null){
                sb.append(line.trim());
            }

            String content = sb.toString();
            content = content.substring(1, content.length() - 1);

            String[] parts = content.split("],\"");
            String matrixData = null;

            for(String part : parts){
                String[] kv = part.split("\":");
                String key = kv[0].replace("\"", "").replace("{", "").trim();
                if(key.equals(mapName)) {
                    matrixData = kv[1].trim();
                    if(!matrixData.endsWith("]")) matrixData += "]";
                    break;
                }
            }

            if(matrixData == null){
                JOptionPane.showMessageDialog(this, "Map not found!", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            matrixData = matrixData.replace("[[", "").replace("]]", "");
            String[] rows = matrixData.split("\\],\\[");

            for(int r = 0; r < 16; r++){
                String[] cols = rows[r].split(",");
                for(int c = 0; c < 16; c++){
                    if(r == 15 && c == 8) continue;

                    int tileType = Integer.parseInt(cols[c].trim());
                    int xPos = c * 32;
                    int yPos = r * 32;

                    if (tileType == 0) editedMap[r][c] = null;
                    else if (tileType == 1) editedMap[r][c] = new BrickWall(xPos, yPos, brickWall);
                    else if (tileType == 2) editedMap[r][c] = new SteelWall(xPos, yPos, steelWall);
                }
            }
            repaint();
        }catch (Exception e){
            JOptionPane.showMessageDialog(this, "An error occurred while loading the map!", "Error", JOptionPane.ERROR_MESSAGE);
        }finally{
            if(reader != null){
                try{
                    reader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void clearMap() {
        for (int r = 0; r < 16; r++) {
            for (int c = 0; c < 16; c++) {
                if (!(editedMap[r][c] instanceof Eagle)) {
                    editedMap[r][c] = null;
                }
            }
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        for(int r = 0; r < 16; r++){
            for(int c = 0; c < 16; c++){
                if(editedMap[r][c] != null){
                    editedMap[r][c].draw(g);
                }
                else{
                    g.setColor(new Color(40,40,40));
                    g.drawRect(c * 32, r * 32, 32, 32);
                }


            }
        }

        g.setColor(Color.DARK_GRAY);
        g.fillRect(0 * 32, 0 * 32, 32, 32);
        g.fillRect(7 * 32, 0 * 32, 32, 32);
        g.fillRect(14 * 32, 0 * 32, 32, 32);
    }

}
