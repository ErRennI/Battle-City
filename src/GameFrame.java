import javax.swing.*;

public class GameFrame extends JFrame{
    JMenuBar menu;
    JMenu help;


    public GameFrame(){
        super("Battle City");

        help = new JMenu("Help");
        menu = new JMenuBar();

        menu.add(help);
        setJMenuBar(menu);

        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
}
