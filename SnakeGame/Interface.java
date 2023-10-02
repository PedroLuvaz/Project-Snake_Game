//Importações
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//Classe Interface
public class Interface extends JPanel implements ActionListener {
    //Elementos
    private Snake snake;
    private Point food;
    private ArrayList<PowerUp> powerUpsGray = new ArrayList<>();
    private PowerUp powerUpYellow;
    private PowerUp powerUpBlue;
    private Timer timer;
    private Timer timerRegress;
    private boolean gameOver;

    //Visualização
    private JPanel gameOverPanel;
    private JPanel powerUpPanel;
    private JButton restartButton;
    private JLabel gameOverLabel;
    private JLabel pointLabel;
    private JLabel recordeLabel;
    private JLabel scoreLabel;
    private JLabel bluePowerUpLabel;
    private JLabel yellowPowerUpLabel;

    //Gerência de Pontuação
    private GameManager gameManager;

    //Constantes
    protected static final int TILE_SIZE = 20;
    protected static final int GRID_WIDTH = 20;
    protected static final int GRID_HEIGHT = 20;
    private int pontosDesdeUltimoPowerUp = 0;
    private int delayRegress = 10000;

    //Atributos do PowerUp Azul
    private boolean powerUpBlueGenerated = false;
    private ScheduledExecutorService powerUpBlueTimer;
    private boolean powerUpBlueCounting = false;

    //Método Construtor Da Interface
    public Interface() {
        setPreferredSize(new Dimension(TILE_SIZE * GRID_WIDTH, TILE_SIZE * GRID_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);

        //Inicialização
        this.snake = new Snake();
        this.gameOver = false;

        //Configuração da Leitura de Teclas
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_UP && snake.getDirection() != Snake.DOWN) {
                    snake.setDirection(Snake.UP);
                } else if (keyCode == KeyEvent.VK_RIGHT && snake.getDirection() != Snake.LEFT) {
                    snake.setDirection(Snake.RIGHT);
                } else if (keyCode == KeyEvent.VK_DOWN && snake.getDirection() != Snake.UP) {
                    snake.setDirection(Snake.DOWN);
                } else if (keyCode == KeyEvent.VK_LEFT && snake.getDirection() != Snake.RIGHT) {
                    snake.setDirection(Snake.LEFT);
                }
            }
        });

        this.timer = new Timer(100, this);

        //Definição e Configuração de Elementos de Interface
        this.gameOverPanel = new JPanel(new BorderLayout());
        gameOverPanel.setBackground(Color.BLACK);
        gameOverPanel.setLayout(new BoxLayout(gameOverPanel, BoxLayout.Y_AXIS));

        this.pointLabel = new JLabel("Pontuação Nesta Rodada: ");
        pointLabel.setForeground(Color.WHITE);
        pointLabel.setFont(new Font("Arial", Font.BOLD, 20));
        pointLabel.setHorizontalAlignment(SwingConstants.CENTER);

        this.gameOverLabel = new JLabel("Game Over!");
        gameOverLabel.setForeground(Color.WHITE);
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 30));
        gameOverLabel.setHorizontalAlignment(SwingConstants.CENTER);

        this.recordeLabel = new JLabel("Recorde Geral: ");
        recordeLabel.setForeground(Color.WHITE);
        recordeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        recordeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        pointLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameOverPanel.add(pointLabel);

        gameOverPanel.add(Box.createVerticalStrut(130));

        gameOverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameOverPanel.add(gameOverLabel);

        gameOverPanel.add(Box.createVerticalStrut(130));

        recordeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameOverPanel.add(recordeLabel);

        gameOverPanel.add(Box.createVerticalGlue());

        this.restartButton = new JButton("Reiniciar");
        restartButton.setBackground(Color.WHITE);
        Dimension buttonSize = new Dimension(400, restartButton.getPreferredSize().height);
        restartButton.setMaximumSize(buttonSize);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame();
            }
        });
        restartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameOverPanel.add(this.restartButton);

        this.gameManager = new GameManager();
        this.scoreLabel = new JLabel("Pontuação: " + gameManager.getScore());
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        scoreLabel.setHorizontalAlignment(SwingConstants.LEFT);

        this.powerUpPanel = new JPanel(new BorderLayout());
        powerUpPanel.setBackground(null);
        powerUpPanel.setOpaque(false);

        this.bluePowerUpLabel = new JLabel("Tempo Restante: " + (delayRegress / 1000) + "s");
        bluePowerUpLabel.setForeground(Color.CYAN);
        bluePowerUpLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        bluePowerUpLabel.setVisible(false);

        this.yellowPowerUpLabel = new JLabel("-5 No Corpo Da Cobra");
        yellowPowerUpLabel.setForeground(Color.YELLOW);
        yellowPowerUpLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        yellowPowerUpLabel.setVisible(false);

        setLayout(new BorderLayout());
        add(gameOverPanel);
        add(scoreLabel, BorderLayout.SOUTH);
        powerUpPanel.add(bluePowerUpLabel, BorderLayout.WEST);
        powerUpPanel.add(yellowPowerUpLabel, BorderLayout.EAST);
        add(powerUpPanel, BorderLayout.NORTH);
        gameOverPanel.setVisible(false);
    }

    //Método executado ao iniciar o jogo
    public void startGame() {
        timer.start();
        generateFood();

        this.powerUpBlueTimer = Executors.newScheduledThreadPool(1);
        Runnable powerUpBlueTask = new Runnable() {
            @Override
            public void run() {
                generatePowerUpBlue();
            }
        };
        powerUpBlueTimer.scheduleAtFixedRate(powerUpBlueTask, 40, 40, TimeUnit.SECONDS);

        this.powerUpBlueCounting = true;
    }

    // Função que é executada ao perder o jogo
    public void handleGameOver() {
        timer.stop();
        pointLabel.setText("Pontuação Nesta Rodada: " + gameManager.getScore());
        gameOverLabel.setText("Game Over!");
        recordeLabel.setText("Recorde Geral: " + gameManager.getRecorde());
        scoreLabel.setVisible(false);
        bluePowerUpLabel.setVisible(false);
        yellowPowerUpLabel.setVisible(false);
        restartButton.setVisible(true);
        gameOverPanel.setVisible(true);
        powerUpBlueTimer.shutdown();
        removeAllPowerUpsGray();
    }

    //Método que gera a comida (Vermelho)
    public void generateFood() {
        Random rand = new Random();
        int x, y;
        do {
            x = rand.nextInt(GRID_WIDTH);
            y = rand.nextInt(GRID_HEIGHT);
        } while (snake.getBody().contains(new Point(x, y)));

        this.food = new Point(x, y);
    }

    //Método que gera o PowerUp Amarelo
    private void generatePowerUpYellow() {
        Random rand = new Random();
        int x, y;
        do {
            x = rand.nextInt(GRID_WIDTH);
            y = rand.nextInt(GRID_HEIGHT);
        } while (snake.getBody().contains(new Point(x, y)) || food.equals(new Point(x, y)));

        this.powerUpYellow = new PowerUp(new Point(x, y), Color.YELLOW);
    }

    //Método que gera o PowerUp Azul
    private void generatePowerUpBlue() {
        if (powerUpBlueCounting) {
            Random rand = new Random();
            int x, y;
            do {
                x = rand.nextInt(GRID_WIDTH);
                y = rand.nextInt(GRID_HEIGHT);
            } while (snake.getBody().contains(new Point(x, y)) || food.equals(new Point(x, y)));

            this.powerUpBlue = new PowerUp(new Point(x, y), Color.BLUE);
            this.powerUpBlueGenerated = true;
            this.powerUpBlueCounting = false;
        }
    }

    //Método com a lógica da posição do PowerUp Armadilha Cinza
    private boolean powerUpAtPosition(int x, int y) {
        for (PowerUp grayPowerUp : powerUpsGray) {
            if (grayPowerUp.getPosition().equals(new Point(x, y))) {
                return true;
            }
        }
        return false;
    }

    //Método que gera o PowerUp Armadilha Cinza
    private void generatePowerUpGray() {
        Random rand = new Random();
        int x, y;
        do {
            x = rand.nextInt(GRID_WIDTH);
            y = rand.nextInt(GRID_HEIGHT);
        } while (snake.getBody().contains(new Point(x, y)) || food.equals(new Point(x, y)) || powerUpAtPosition(x, y));

        PowerUp grayPowerUp = new PowerUp(new Point(x, y), Color.GRAY);
        powerUpsGray.add(grayPowerUp);

        repaint();
    }

    //Método com funcionalidades e interações
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            snake.move();

            //Checa a colisão da cobra
            if (snake.checkCollision()) {
                this.gameOver = true;
                handleGameOver();
            }

            //Pega a comida
            if (snake.getBody().getFirst().equals(food)) {
                snake.grow();
                generateFood();
                gameManager.increaseScore();
                scoreLabel.setText("Pontuação: " + gameManager.getScore());

                //Contagem para o PowerUp Amarelo
                if (pontosDesdeUltimoPowerUp >= 9) {
                    generatePowerUpYellow();
                    this.pontosDesdeUltimoPowerUp = 0;
                } else {
                    this.pontosDesdeUltimoPowerUp++;
                }

                //Contagem para o PowerUp Armadilha Cinza
                if (gameManager.getScore() % 30 == 0) {
                    generatePowerUpGray();
                }
            }

            //Apresenta a label ao pegar o PowerUp Amarelo
            if (powerUpYellow != null && snake.getBody().getFirst().equals(powerUpYellow.getPosition())) {
                powerUpYellow.applyEffectYellow(snake);
                this.powerUpYellow = null;
                yellowPowerUpLabel.setText("-5 No Corpo Da Cobra");
                yellowPowerUpLabel.setVisible(true);

                Timer timerYellow = new Timer(5000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        yellowPowerUpLabel.setVisible(false);
                    }
                });
                timerYellow.setRepeats(false);
                timerYellow.start();
            }

            //Desenha o ArrayList com os PowerUps Armadilha Cinza
            for (PowerUp grayPowerUp : powerUpsGray) {
                if (snake.getBody().getFirst().equals(grayPowerUp.getPosition())) {
                    handleGameOver();
                    break;
                }
            }

            //Condição ao pegar PowerUp Azul
            if (powerUpBlue != null && snake.getBody().getFirst().equals(powerUpBlue.getPosition())) {
                this.powerUpBlue = null;
                this.powerUpBlueGenerated = false;
                this.powerUpBlueCounting = true;
                snake.activateBluePowerUp();
                bluePowerUpLabel.setText("Tempo Restante: " + (delayRegress / 1000) + "s");
                bluePowerUpLabel.setVisible(true);

                this.timerRegress = new Timer(1000, new ActionListener() {
                    int secondsLeft = delayRegress / 1000;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        secondsLeft--;
                        if (secondsLeft >= 0) {
                            bluePowerUpLabel.setText("Tempo Restante: " + secondsLeft + "s");
                        }
                    }
                });

                timerRegress.start();

                Timer timerBlue = new Timer(delayRegress, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        snake.deactivateBluePowerUp();
                        bluePowerUpLabel.setVisible(false);
                    }
                });
                timerBlue.setRepeats(false);
                timerBlue.start();

                repaint();
            }
            repaint();
        }
    }

    //Desenha na tela os componentes
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        //Desenha a cobra
        g.setColor(Color.GREEN);
        for (Point segment : snake.getBody()) {
            g.fillRect(segment.x * TILE_SIZE, segment.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        //Desenha a comida
        g.setColor(Color.RED);
        g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        //Desenha o PowerUp Amarelo
        if (powerUpYellow != null) {
            g.setColor(powerUpYellow.getColor());
            g.fillRect(powerUpYellow.getPosition().x * TILE_SIZE, powerUpYellow.getPosition().y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        //Desenha o PowerUp Azul
        if (powerUpBlueGenerated && powerUpBlue != null) {
            g.setColor(powerUpBlue.getColor());
            g.fillRect(powerUpBlue.getPosition().x * TILE_SIZE, powerUpBlue.getPosition().y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        //Desenha o PowerUp Cinza
        if (!powerUpsGray.isEmpty()) {
            for (PowerUp grayPowerUp : powerUpsGray) {
                g.setColor(grayPowerUp.getColor());
                g.fillRect(grayPowerUp.getPosition().x * TILE_SIZE, grayPowerUp.getPosition().y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    //Método executado ao reiniciar o jogo
    private void resetGame() {
        startGame();
        this.snake = new Snake();
        this.gameOver = false;
        gameOverLabel.setText("");
        restartButton.setVisible(false);
        scoreLabel.setVisible(true);
        gameManager.resetScore();
        scoreLabel.setText("Pontuação: " + gameManager.getScore());
        gameOverPanel.setVisible(false);
        this.powerUpYellow = null;
        this.powerUpBlue = null;
        removeAllPowerUpsGray();
        pontosDesdeUltimoPowerUp = 0;
    }

    //Método que remove todos os PowerUps Armadilha Cinza
    private void removeAllPowerUpsGray() {
        powerUpsGray.clear();
        repaint();
    }
}