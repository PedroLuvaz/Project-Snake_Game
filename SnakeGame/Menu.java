//Importações
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//Classe Menu
//É a classe que constrói um Menu Principal para que o jogador possa iniciar o jogo
public class Menu extends JPanel {
    //Elementos Gráficos De Interface
    private JButton startButton;

    //Método Construtor Do Menu
    public Menu() {
        initializeUI();
    }

    //Inicializa a interface ao iniciar o jogo
    private void initializeUI() {
        setPreferredSize(new Dimension(Interface.TILE_SIZE * Interface.GRID_WIDTH, Interface.TILE_SIZE * Interface.GRID_HEIGHT));
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        // Cria um painel para inserior o botão de iniciar jogo
        JPanel buttonPanel = createButtonPanel();
        buttonPanel.add(this.startButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Cria uma label para exibir o nome do jogo no Menu Principal
        JLabel titleLabel = createTitleLabel();
        add(titleLabel, BorderLayout.CENTER);

        JLabel instructionLabel = createInstructionLabel();
        add(instructionLabel, BorderLayout.NORTH);
    }

    //Cria um painel para que o botão de iniciar jogo seja inserido
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.BLACK);

        this.startButton = new JButton("Iniciar Jogo");
        this.startButton.setBackground(Color.WHITE);
        this.startButton.setFocusPainted(false);
        this.startButton.setPreferredSize(new Dimension(400, startButton.getPreferredSize().height)); // Define o tamanho preferencial aqui
        this.startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Inicia o jogo quando o botão for clicado
                startGame();
            }
        });

        return buttonPanel;
    }

    //Cria uma label para exibir o título do jogo no Menu Principal
    private JLabel createTitleLabel() {
        JLabel titleLabel = new JLabel("Snake Game");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        return titleLabel;
    }

    //Cria uma label de instrução
    private JLabel createInstructionLabel() {
        JLabel instructionLabel = new JLabel("Use as setas do teclado para se movimentar");
        instructionLabel.setForeground(Color.WHITE);
        instructionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);

        return instructionLabel;
    }

    //Método para iniciar o jogo quando o botão for clicado
    private void startGame() {
        //Fecha a janela do Menu Principal ao iniciar o jogo
        Window menuWindow = SwingUtilities.getWindowAncestor(this);
        if (menuWindow != null) {
            menuWindow.dispose();
        }

        //Cria a janela onde o jogo será executado
        JFrame frame = new JFrame("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        //Define o ícone na janela do jogo
        Image iconImage = Toolkit.getDefaultToolkit().getImage("aicon.png");
        frame.setIconImage(iconImage);

        //Cria o cenário em que o jogo será imprimido
        Interface scenario = new Interface();
        frame.add(scenario);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        //Inicia o jogo de fato
        scenario.startGame();
    }
}
