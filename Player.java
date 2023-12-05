import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class Player {
    private DataInputStream input;
    private DataOutputStream output;

    int score;
    String name;

    private Question currentQuestion;

    private long sentTime;
    private long totalTime;

    public Player(Socket socket) {
        try {
            // utiliser socket pour stocker le flux d'entrée et de sortie
            this.input = new DataInputStream(socket.getInputStream());
            this.output = new DataOutputStream(socket.getOutputStream());
            this.name = input.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendWinner(String winnerName, int winnerScore) {
        // envoi des instructions WINNER avec le nom du gagnant et son score
        try {
            output.writeUTF("WINNER");
            output.writeUTF(winnerName);
            output.writeInt(winnerScore);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendQuestion(Question question) {
        try {
            // envoi d'une instruction QUESTION avec question + 4 choix
            this.currentQuestion = question;
            this.sentTime = System.currentTimeMillis();
            output.writeUTF("QUESTION");
            output.writeUTF(question.getQuestion());
            output.writeUTF(question.getChoice1());
            output.writeUTF(question.getChoice2());
            output.writeUTF(question.getChoice3());
            output.writeUTF(question.getChoice4());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String receiveAnswer() {
        try {
            // recevoir les réponses
            String answer = input.readUTF();
            totalTime = totalTime + (sentTime - System.currentTimeMillis());
            // si la réponse de l'utilisateur correspond à la bonne - score croissant
            if (answer.equalsIgnoreCase(currentQuestion.getAnswer())) {
                updateScore();
            }
            return answer;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void sendStatistics(Question question, List<String> answers) {
        // envoi de STATISTIQUES
        try {
            output.writeUTF("STATISTIQUES");
            output.writeUTF(question.getAnswer());

            for (String answer : answers) {
                output.writeUTF(answer);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendGameEnd() {
        // envoi de l'instruction GAME_END
        try {
            output.writeUTF("GAME_END");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // getter
    public int getScore() {
        return this.score;
    }

    public String getName() {
        return this.name;
    }

    public long getTotalTime() {
        return this.totalTime;
    }

    // méthode de mise à jour du score
    public void updateScore() {
        this.score++;
    }
}