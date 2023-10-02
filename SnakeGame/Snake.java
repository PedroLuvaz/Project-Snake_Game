//Importações
import java.awt.Point;
import java.util.LinkedList;

//Classe Snake
//É a classe com as funcionalidades da cobra e suas interações
public class Snake {
    //Controle
    private LinkedList<Point> body;
    private int direction;
    private boolean grow;

    //Constantes de direções
    public static final int UP = 0;
    public static final int RIGHT = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;

    //Estado de PowerUp
    private boolean isBluePowerUpActive = false;

    //Método Construtor Da Snake
    public Snake() {
        this.body = new LinkedList<>();
        this.body.add(new Point(5, 5));
        this.direction = RIGHT;
        this.grow = false;
    }

    //Método "get" que retorna o corpo da cobra
    public LinkedList<Point> getBody() {
        return this.body;
    }

    //Método "get" que retorna a direção da cobra
    public int getDirection() {
        return this.direction;
    }

    //Método "set" que atribui um novo valor para a direção da cobra
    public void setDirection(int direction) {
        this.direction = direction;
    }

    //Método que controla a movimentação da cobra
    public void move() {
        Point newHead = (Point) body.getFirst().clone();

        //Condição que verifica o estado atual do PowerUp
        if (this.isBluePowerUpActive) {
            handleBluePowerUpMovement(newHead); //Permite que a cobra atravesse as bordas
        } else {
            handleRegularMovement(newHead); //Não permite que a cobra atravesse as bordas
        }

        body.addFirst(newHead);

        //Condição que verifica o crescimento da cobra
        if (!grow) {
            body.removeLast();
        } else {
            this.grow = false;
        }
    }

    //Método que ativa o crescimento da cobra
    public void grow() {
        this.grow = true;
    }

    //Método que checa se a cabeça da cobra colidiu com algo
    public boolean checkCollision() {
        Point head = body.getFirst();

        //Verifica a colisão com a própria cobra
        for (int i = 1; i < body.size(); i++) {
            if (head.equals(body.get(i))) {
                return true;
            }
        }

        //Verifica colisão da cobra com as bordas do cenário (Se o PowerUp não está ativo)
        if (!isBluePowerUpActive) {
            if (head.x < 0 || head.x >= Interface.GRID_WIDTH || head.y < 0 || head.y >= Interface.GRID_HEIGHT) {
                return true;
            }
        }

        return false;
    }

    //Método que ativa o estado do PowerUp Azul
    public void activateBluePowerUp() {
        this.isBluePowerUpActive = true;
    }

    //Método que desativa o estado do PowerUp Azul
    public void deactivateBluePowerUp() {
        this.isBluePowerUpActive = false;
    }

    //Método com lógica de movimento quando o estado do PowerUp Azul está ativado
    private void handleBluePowerUpMovement(Point newHead) {
        if (direction == UP) {
            handleBluePowerUpUp(newHead);
        } else if (direction == RIGHT) {
            handleBluePowerUpRight(newHead);
        } else if (direction == DOWN) {
            handleBluePowerUpDown(newHead);
        } else if (direction == LEFT) {
            handleBluePowerUpLeft(newHead);
        }
    }

    //Método com lógica de movimento quando o estado do PowerUp Azul está desativado
    private void handleRegularMovement(Point newHead) {
        if (direction == UP) {
            newHead.translate(0, -1);
        } else if (direction == RIGHT) {
            newHead.translate(1, 0);
        } else if (direction == DOWN) {
            newHead.translate(0, 1);
        } else if (direction == LEFT) {
            newHead.translate(-1, 0);
        }
    }

    //Atravessa a borda da tela por cima (Entra em cima e sai em baixo)
    private void handleBluePowerUpUp(Point newHead) {
        if (newHead.y == 0) {
            newHead.y = Interface.GRID_HEIGHT - 1;
        } else {
            newHead.translate(0, -1);
        }
    }

    //Atravessa a borda da tela pelo lado direito (Entra na direita e sai na esquerda)
    private void handleBluePowerUpRight(Point newHead) {
        if (newHead.x == Interface.GRID_WIDTH - 1) {
            newHead.x = 0;
        } else {
            newHead.translate(1, 0);
        }
    }

    //Atravessa a borda da tela por baixo (Entra em baixo e sai em cima)
    private void handleBluePowerUpDown(Point newHead) {
        if (newHead.y == Interface.GRID_HEIGHT - 1) {
            newHead.y = 0;
        } else {
            newHead.translate(0, 1);
        }
    }

    //Atravessa a borda da tela pelo lado esquerdo (Entra na esquerda e sai na direita)
    private void handleBluePowerUpLeft(Point newHead) {
        if (newHead.x == 0) {
            newHead.x = Interface.GRID_WIDTH - 1;
        } else {
            newHead.translate(-1, 0);
        }
    }
}
