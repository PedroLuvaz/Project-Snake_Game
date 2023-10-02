//Importações
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

//Classe GameManager
//É a classe que gerencia a pontuação e o recorde do jogo
public class GameManager {
    //Pontuação
    private int score;
    private int recorde;

    //Método Construtor Do GameManager
    public GameManager() {
        this.score = 0;
        this.recorde = loadRecorde();
    }

    //Método "get" que retorna a pontuação atual
    public int getScore() {
        return this.score;
    }

    //Método "get" que retorna o recorde atual
    public int getRecorde() {
        return this.recorde;
    }

    //Método que aumenta a pontuação e atualiza o recorde
    public void increaseScore() {
        this.score++;
        if (this.score > this.recorde) {
            this.recorde = this.score;
            saveRecorde();
        }
    }

    //Método que reseta a pontuação para 0
    public void resetScore() {
        this.score = 0;
    }

    //Método que carrega o recorde do jogo armazenado em um arquivo externo
    private int loadRecorde() {
        String diretorioLoad = "C:\\Users\\natha\\OneDrive\\Área de Trabalho\\SnakeGame\\SaveDB\\";
        String arquivoRecordeLoad = diretorioLoad + "recorde.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivoRecordeLoad))) {
            String line = reader.readLine();
            if (line != null) {
                return Integer.parseInt(line);
            }
        } catch (IOException | NumberFormatException e) { //Lida com exceções e erros

        }
        return 0;
    }

    //Método que salva o recorde do jogo em um arquivo externo
    private void saveRecorde() {
        String diretorioSave = "C:\\Users\\natha\\OneDrive\\Área de Trabalho\\SnakeGame\\SaveDB\\";
        String arquivoRecordeSave = diretorioSave + "recorde.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoRecordeSave))) {
            writer.write(Integer.toString(recorde));
        } catch (IOException e) { //Lida com exceções e erros
            
        }
    }
}
