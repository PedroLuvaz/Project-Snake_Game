//Importações
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.Image;
import java.awt.Toolkit;

//Classe Main
//Classe que executa tudo
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame menuFrame = new JFrame("Snake Game");
                menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                menuFrame.setResizable(false);

                Image iconImage = Toolkit.getDefaultToolkit().getImage("aicon.png");
                menuFrame.setIconImage(iconImage);

                Menu menu = new Menu();
                menuFrame.add(menu);

                menuFrame.pack();
                menuFrame.setLocationRelativeTo(null);
                menuFrame.setVisible(true);
            }
        });
    }
}