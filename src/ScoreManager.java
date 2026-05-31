import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ScoreManager {
    private static final String FILE_NAME = "high_scores.csv";

    public static void saveScore(JFrame parentFrame, int score, long gameStartTime){
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                String playerName = JOptionPane.showInputDialog(parentFrame, "Game Over! Enter your name for High Scores:", "Save Score", JOptionPane.PLAIN_MESSAGE);

                if(playerName == null || playerName.trim().isEmpty()){
                    playerName = "Anonymous";
                }

                long endTime = System.currentTimeMillis();
                long gameTime = (endTime - gameStartTime) / 1000;
                String durationStr = gameTime + "s";

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String currentDate = sdf.format(new Date());

                File file = new File(FILE_NAME);
                BufferedWriter writer = null;

                try{
                    boolean isNewFile = !file.exists() || file.length() == 0;
                    writer = new BufferedWriter(new FileWriter(file, true));

                    if(isNewFile){
                        writer.write("Name,Score,Time,Date\n");
                    }

                    writer.write(playerName.trim() + "," + score + "," + durationStr + "," + currentDate + "\n" );

                }catch(IOException e){
                    System.err.println("Error while saving the score!");
                }finally {
                    try{
                        if(writer != null) writer.close();
                    }catch(IOException _){}
                }
                displayHighScores(parentFrame);
            }
        });
    }


    public static void displayHighScores(JFrame parentFrame){
        File file = new File(FILE_NAME);
        if(!file.exists() || file.length() == 0){
            JOptionPane.showMessageDialog(parentFrame, "No high scores registered yet!", "High Scores", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        ArrayList<String[]> scores = new ArrayList<>();
        BufferedReader reader = null;

        try{
            reader = new BufferedReader(new FileReader(file));
            String line;
            boolean isFirstLine = true;

            while((line = reader.readLine()) != null){
                if(isFirstLine){
                    isFirstLine = false;
                    continue;
                }
                String[] data = line.split(",");
                if(data.length == 4){
                    scores.add(data);
                }
            }
        }catch(IOException e){
            JOptionPane.showMessageDialog(parentFrame, "Error reading high scores!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }finally{
            if(reader != null){
                try{
                    reader.close();
                } catch (IOException _) {}
            }
        }

        scores.sort(new ScoreComparator());

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-4s %-15s %-8s %-8s %-15s\n", "No", "Name", "Score", "Time", "Date"));
        sb.append("-------------------------------------------------------------------------\n");


        for (int i = 0; i < Math.min(scores.size(), 10); i++) {
            String[] row = scores.get(i);
            sb.append(String.format("%-4d %-15s %-8s %-8s %-15s\n", (i + 1), row[0], row[1], row[2], row[3]));
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(520, 250));

        JOptionPane.showMessageDialog(parentFrame, scrollPane, "🏆 TOP 10 HIGH SCORES 🏆", JOptionPane.PLAIN_MESSAGE);

    }
}
