import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private List<Player> players = new ArrayList<>();

    public static void main(String[] args) {
        new Server().startServer();
    }

    public void startServer() {
        try {
            // démarrage du serveur à 8888
            ServerSocket serverSocket = new ServerSocket(8888);
            System.out.println("Server is running. Waiting for players...");

            // j'attends que 4 joueurs se connectent
            while (players.size() < 4) {
                Socket clientSocket = serverSocket.accept();
                Player player = new Player(clientSocket);
                players.add(player);
            }

            // démarrage du jeu
            startGame();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startGame() {
        // obtenir la liste des questions
        List<Question> questions = generateQuestions();
        int questionNumber = 1;

        // boucler les questions
        for (Question question : questions) {
            // impression de la question sur le serveur
            System.out.println("Question " + questionNumber + ": " + question.getQuestion());
            // envoi de questions aux clients
            broadcastQuestion(question);

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Collecter et compiler les réponses
            List<String> answers = collectAnswers();

            // Distribue les statistiques
            distributeStatistics(question, answers);

            questionNumber++;
        }

        // Déterminer le gagnant et afficher les statistiques finales
        designateWinner();
        displayFinalStatistics();
    }

    private List<Question> generateQuestions() {
        // Liste des questions
        List<Question> questions = new ArrayList<>();
        Question question1 = new Question("Quelle est la deuxième ville la plus peuplée du monde ?", "Shanghai",
                "Tokyo", "London", "Mexico city", "D");
        Question question2 = new Question("Bali est une célèbre station de pique-nique et touristique de ______",
                "Malaysia", "Indonesia", "Australia", "None of these", "B");
        Question question3 = new Question(
                "Laquelle des villes suivantes est connue sous le nom de « Ville des canaux » ?", "Paris", "Amsterdam",
                "Oslo", "Venice", "D");
        Question question4 = new Question("Bamako est la capitale et le grand centre commercial de ________", "Nigeria",
                "Azerbijan", "Mali", "Italy", "C");
        Question question5 = new Question("Tallinn est la ville principale et la capitale de_____", "Estonia", "Latvia",
                "Cyprus", "Lithuania", "A");
        Question question6 = new Question("Bridgetown est la capitale et le centre commercial de________", "Jamika",
                "Barbados", "Togo", "Cosia Rica", "B");

        questions.add(question1);
        questions.add(question2);
        questions.add(question3);
        questions.add(question4);
        questions.add(question5);
        questions.add(question6);
        return questions;
    }

    private void broadcastQuestion(Question question) {
        // envoi de questions aux joueurs
        for (Player player : players) {
            player.sendQuestion(question);
        }
    }

    private List<String> collectAnswers() {
        // collecte des réponses des joueurs
        List<String> answers = new ArrayList<>();
        for (Player player : players) {
            String answer = player.receiveAnswer();
            answers.add(answer);
        }
        return answers;
    }

    private void distributeStatistics(Question question, List<String> answers) {
        for (Player player : players) {
            player.sendStatistics(question, answers);
        }
    }

    private void designateWinner() {
        // logique pour calculer le gagnant en fonction du score
        Player winner = null;
        int maxScore = -1;
        long minTime = Long.MAX_VALUE;

        for (Player player : players) {
            int score = player.getScore();
            if (score > maxScore) {
                maxScore = score;
                winner = player;
                minTime = player.getTotalTime();
            } else if (score == maxScore) {
                if (player.getTotalTime() < minTime) {
                    winner = player;
                    minTime = player.getTotalTime();
                }
            }
        }

        if (winner != null) {
            System.out.println("Winner: " + winner.getName() + " avec un score de: " + maxScore);
            // Informer les clients du gagnant
            for (Player player : players) {
                player.sendWinner(winner.getName(), maxScore);
            }
        } else {
            System.out.println("Pas de gagnant");
        }
    }

    private void displayFinalStatistics() {
        // affichage des scores finaux sur le serveur
        System.out.println("Scores Finals");

        for (Player player : players) {
            System.out.println(player.getName() + ": " + player.getScore());
        }
        for (Player player : players) {
            player.sendGameEnd();
        }
    }
}
