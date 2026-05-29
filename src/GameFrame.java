import javax.swing.*;
import javax.swing.event.MenuEvent;
import java.awt.*;
import java.awt.event.*;

public class GameFrame extends JFrame{
    private JMenuBar menu;
    private JPanel mainSpace;
    private GamePanel gamePanel;
    private MapEditor mapEditorPanel;
    private CardLayout cardLayout;
    private Container container;

    private JPanel gameSidePanel;
    private JPanel editorSidePanel;
    private CardLayout sideCardLayout;
    private JPanel sideContainer;
    private JLabel livesLabel;


    public GameFrame(){
        super("Battle City");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        setMenu();
        setJPanel();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void setMenu(){
        menu = new JMenuBar();
        menu.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));

        JMenuItem newGame = new JMenuItem("New Game");
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(container, "GAME");
                sideCardLayout.show(sideContainer, "GAME_SIDE");

                String mapToPlay = JOptionPane.showInputDialog(null, "Enter the map name you want to play:", "Custom Map");
                if (mapToPlay == null || mapToPlay.trim().isEmpty()) {
                    mapToPlay = "Custom Map";
                }

                gamePanel.startGameThread(mapToPlay.trim());
                gamePanel.requestFocusInWindow();
            }
        });
        menu.add(newGame);

        JMenuItem mapEditor = new JMenuItem("Map Editor");
        mapEditor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(container, "EDITOR");
                sideCardLayout.show(sideContainer, "EDITOR_SIDE");
                mapEditorPanel.requestFocusInWindow();
            }
        });
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

        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);

        gamePanel = new GamePanel(this);
        mapEditorPanel = new MapEditor(new SpriteManager());

        container.add(gamePanel, "GAME");
        container.add(mapEditorPanel, "EDITOR");


        sideCardLayout = new CardLayout();
        sideContainer = new JPanel(sideCardLayout);
        sideContainer.setPreferredSize(new Dimension(200, 0));

        gameSidePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        gameSidePanel.setBackground(Color.DARK_GRAY);

        JLabel infoTitle = new JLabel("---Status---");
        infoTitle.setForeground(Color.WHITE);
        gameSidePanel.add(infoTitle);

        //TODO player health ile enemy sayısını dynamic yap
        livesLabel = new JLabel("PLAYER LIVES: 3");
        livesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        livesLabel.setForeground(Color.RED);
        gameSidePanel.add(livesLabel);

        JLabel enemyLabel = new JLabel("ENEMIES LEFT: 20");
        enemyLabel.setFont(new Font("Arial", Font.BOLD, 14));
        enemyLabel.setForeground(Color.ORANGE);
        gameSidePanel.add(enemyLabel);


        JPanel editorSidePanel = new JPanel();
        editorSidePanel.setBackground(Color.DARK_GRAY);
        editorSidePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15));

        JLabel editorTitle = new JLabel("--- MAP EDITOR ---");
        editorTitle.setForeground(Color.WHITE);
        editorSidePanel.add(editorTitle);

        JButton brickButton = new JButton("Brick Wall");
        brickButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapEditorPanel.setSelectedTool(1);
            }
        });
        editorSidePanel.add(brickButton);

        JButton steelButton = new JButton("Steel Wall");
        steelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapEditorPanel.setSelectedTool(2);
            }
        });
        editorSidePanel.add(steelButton);

        JButton bushButton = new JButton("Bush");
        bushButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapEditorPanel.setSelectedTool(3);
            }
        });
        editorSidePanel.add(bushButton);

        JButton waterButton = new JButton("Water");
        bushButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapEditorPanel.setSelectedTool(4);
            }
        });
        editorSidePanel.add(waterButton);

        JButton eraserButton = new JButton("Eraser");
        eraserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapEditorPanel.setSelectedTool(0);
            }
        });
        editorSidePanel.add(eraserButton);

        JButton saveButton = new JButton("Save Map");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane.showInputDialog(GameFrame.this, "Enter map name:", "Custom Map");
                if (name != null && !name.trim().isEmpty()) {
                    mapEditorPanel.saveMapToJSON(name.trim());
                }
            }
        });
        editorSidePanel.add(saveButton);

        JButton loadButton = new JButton("Load Map");
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane.showInputDialog(GameFrame.this, "Enter map name to load:", "Custom Map");
                if (name != null && !name.trim().isEmpty()) {
                    mapEditorPanel.loadMapFromJSON(name.trim());
                }
            }
        });
        editorSidePanel.add(loadButton);

        sideContainer.add(gameSidePanel, "GAME_SIDE");
        sideContainer.add(editorSidePanel, "EDITOR_SIDE");

        mainSpace.add(sideContainer, BorderLayout.EAST);
        mainSpace.add(container, BorderLayout.CENTER);

        add(mainSpace, BorderLayout.CENTER);


    }


    public void updateLivesUI(int lives) {
        if (livesLabel != null) {
            livesLabel.setText("PLAYER LIVES: " + lives);
        }
    }
}
