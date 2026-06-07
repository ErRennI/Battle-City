import javax.swing.*;
import javax.swing.event.MenuEvent;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

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
    private JLabel enemyLabel;

    private String currentDifficulty = "EASY";


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


        JMenuItem newGame = new JMenuItem("New Game");
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> mapNames = MapEditor.getSavedMapNames();

                String[] mapNameArray = mapNames.toArray(new String[0]);
                JComboBox<String> mapComboBox = new JComboBox<>(mapNameArray);

                Object[] message = {
                        "Select a map from saved templates:", mapComboBox,
                        "\nCurrent Difficulty: " + currentDifficulty
                };

                int option = JOptionPane.showConfirmDialog(
                        GameFrame.this,
                        message,
                        "Start New Game",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

                if (option == JOptionPane.OK_OPTION) {
                    String selectedMap = (String) mapComboBox.getSelectedItem();
                    if (selectedMap != null && !selectedMap.trim().isEmpty()) {

                        cardLayout.show(container, "GAME");
                        sideCardLayout.show(sideContainer, "GAME_SIDE");

                        gamePanel.setEnemyDifficulty(currentDifficulty);
                        gamePanel.startGameThread(selectedMap.trim());
                        gamePanel.requestFocusInWindow();
                    }
                }
            }
        });
        menu.add(newGame);

        JMenuItem mapEditor = new JMenuItem("Editor");
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
        options.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] difficultyOptions = {"EASY", "MEDIUM", "HARD"};

                String selection = (String) JOptionPane.showInputDialog(
                        GameFrame.this,
                        "Select Game Difficulty:\n(Affects enemy speed, spawn rate, and bullet speed)",
                        "Options",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        difficultyOptions,
                        currentDifficulty
                );

                if (selection != null) {
                    currentDifficulty = selection;
                    JOptionPane.showMessageDialog(GameFrame.this, "Difficulty successfully set to: " + currentDifficulty, "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        menu.add(options);

        JMenuItem highScores = new JMenuItem("Scores");
        highScores.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                ScoreManager.displayHighScores(GameFrame.this);
            }
        });
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

        container.setBackground(Color.DARK_GRAY);
        container.setPreferredSize(new Dimension(416, 416));

        StartScreen startScreen = new StartScreen();
        gamePanel = new GamePanel(this);
        mapEditorPanel = new MapEditor(new SpriteManager());

        container.add(startScreen, "START");
        container.add(gamePanel, "GAME");
        container.add(mapEditorPanel, "EDITOR");


        sideCardLayout = new CardLayout();
        sideContainer = new JPanel(sideCardLayout);
        sideContainer.setPreferredSize(new Dimension(200, 416));
        setResizable(false);

        gameSidePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        gameSidePanel.setBackground(Color.DARK_GRAY);

        JLabel infoTitle = new JLabel("---Status---");
        infoTitle.setForeground(Color.WHITE);
        gameSidePanel.add(infoTitle);

        livesLabel = new JLabel("PLAYER LIVES:    ");
        livesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        livesLabel.setForeground(Color.RED);
        gameSidePanel.add(livesLabel);

        enemyLabel = new JLabel("ENEMIES LEFT:    ");
        enemyLabel.setFont(new Font("Arial", Font.BOLD, 14));
        enemyLabel.setForeground(Color.ORANGE);
        gameSidePanel.add(enemyLabel);

        JButton pauseButton = new JButton("Pause");
        pauseButton.setFocusable(false);
        pauseButton.setFont(new Font("Arial", Font.BOLD, 12));
        pauseButton.setBackground(Color.LIGHT_GRAY);

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                boolean pausedState = gamePanel.togglePause();
                if (pausedState) {
                    pauseButton.setText("Resume");
                    pauseButton.setBackground(Color.GREEN);
                } else {
                    pauseButton.setText("Pause");
                    pauseButton.setBackground(Color.LIGHT_GRAY);
                }
                gamePanel.requestFocusInWindow();
            }
        });
        gameSidePanel.add(pauseButton);


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
        waterButton.addActionListener(new ActionListener() {
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
                ArrayList<String> mapNames = MapEditor.getSavedMapNames();

                String[] mapNameArray = mapNames.toArray(new String[0]);
                JComboBox<String> mapComboBox = new JComboBox<>(mapNameArray);

                Object[] message = {
                        "Select a map from saved templates:", mapComboBox,
                };

                int option = JOptionPane.showConfirmDialog(
                        GameFrame.this,
                        message,
                        "Load Map",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );
                if (option == JOptionPane.OK_OPTION) {
                    String selectedMap = (String) mapComboBox.getSelectedItem();
                    if (selectedMap != null) {
                        mapEditorPanel.loadMapFromJSON(selectedMap);
                        mapEditorPanel.requestFocusInWindow();
                    }
                }
            }
        });
        editorSidePanel.add(loadButton);

        sideContainer.add(gameSidePanel, "GAME_SIDE");
        sideContainer.add(editorSidePanel, "EDITOR_SIDE");

        mainSpace.add(sideContainer, BorderLayout.EAST);
        mainSpace.add(container, BorderLayout.CENTER);

        mainSpace.setBackground(Color.DARK_GRAY);

        add(mainSpace, BorderLayout.CENTER);


    }


    public void updateLivesUI(int lives) {
        if (livesLabel != null) {
            livesLabel.setText("PLAYER LIVES: " + lives);
        }
    }

    public void updateEnemiesUI(int destroyedEnemyCount){
        if(enemyLabel != null){
            int leftEnemies = 20 - destroyedEnemyCount;
            enemyLabel.setText("ENEMIES LEFT: " + leftEnemies);
        }
    }
}
