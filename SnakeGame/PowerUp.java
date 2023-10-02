//Importações
import java.awt.Color;
import java.awt.Point;

//Classe PowerUp
//É a classe que controla os atributos e métodos de um PowerUp
public class PowerUp {
    //Atributos Do PowerUp
    private Point position;
    private Color color;

    //Método Construtor De Um PowerUp
    public PowerUp(Point position, Color color) {
        this.position = position;
        this.color = color;
    }

    //Método "get" que retorna a posição do PowerUp
    public Point getPosition() {
        return this.position;
    }

    //Método "get" que retorna a cor do PowerUp
    public Color getColor() {
        return this.color;
    }

    //Aplica o efeito do PowerUp Amarelo à cobra
    public void applyEffectYellow(Snake snake) {
        if (!snake.getBody().isEmpty() && snake.getBody().size() >= 5) {
            for (int i = 0; i < 5; i++) {
                snake.getBody().removeLast();
            }
        }
    }
}
