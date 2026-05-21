import javax.swing.*;
import javax.swing.event.MenuEvent;
import java.awt.*;
import java.awt.event.*;

public class GameFrame extends JFrame{
    JMenuBar menu;
    JPanel mainSpace;
    GamePanel gamePanel;
    JPanel sidePanel;

    public GameFrame(){
        super("Battle City");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());


        setMenu();
        setJPanel();

        setVisible(true);
    }

    public void setMenu(){
        menu = new JMenuBar();
        menu.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));

        JMenuItem newGame = new JMenuItem("New Game");
        //TO-DO ActionListener
        menu.add(newGame);

        JMenuItem mapEditor = new JMenuItem("Map Editor");
        //TO-DO ActionListener
        menu.add(mapEditor);

        JMenuItem options = new JMenuItem("Options");
        //TO-DO ActionListener
        menu.add(options);

        JMenuItem highScores = new JMenuItem("High Scores");
        //TO-DO ActionListener
        menu.add(highScores);

        JMenuItem help = new JMenuItem("Help");
        help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                String helpMessage = """
                        🎮 BATTLE CITY - HOW TO PLAY
                        
                        OBJECTIVE:
                        Protect your base (the Eagle) and destroy all enemy tanks!
                        
                        🕹️ CONTROLS:
                        - Movement: Arrow Keys or W, A, S, D
                        - Shoot: Spacebar
                        
                        🗺 TERRAIN GUIDE:
                        - Brick Walls: Can be destroyed by shooting.
                        - Steel Walls: Indestructible by normal shots.
                        - Trees/Bushes: Hides tanks, but bullets pass through.
                        - Water: Tanks cannot cross, but bullets can fly over.
                        
                        💡 QUICK TIPS:
                        1. Defend the Base: If the Eagle is destroyed, it's GAME OVER!
                        2. Power-Ups: Shoot glowing enemy tanks to get shields, bombs, or upgrades.
                        3. Lives: Check your remaining lives on the screen. Don't let them hit zero.
                        
                        ✨ Good Luck, Commander! Defend the base at all costs.""";
                JOptionPane.showMessageDialog(null, helpMessage, "Help", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        menu.add(help);

        JMenuItem about = new JMenuItem("About");
        about.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
                JOptionPane.showMessageDialog(null, """
                        Name: Eren Ada Doğan
                        School Number: 20240702022
                        Personal Email: erenadadgn05@gmail.com
                        University Email: erenada.dogan@std.yeditepe.edu.tr
                        """,
                        "About",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
        menu.add(about);

        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menu.add(exit);
        setJMenuBar(menu);
    }

    public void setJPanel(){
        mainSpace = new JPanel(new BorderLayout());

        gamePanel = new GamePanel();

        sidePanel = new JPanel(new GridLayout(3,1,20, 20));
        sidePanel.setBackground(Color.DARK_GRAY);
        sidePanel.setPreferredSize(new Dimension(200, 0));

        mainSpace.add(sidePanel, BorderLayout.EAST);
        mainSpace.add(gamePanel, BorderLayout.CENTER);

        add(mainSpace, BorderLayout.CENTER);

        gamePanel.startGameThread();
    }
}
