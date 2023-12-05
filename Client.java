import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        new Client().startClient();
    }

    public void startClient() {
        try {
            // création d'une connexion avec le serveur
            Socket socket = new Socket("localhost", 8888);
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            // demande le nom du joueur
            System.out.print("Entrer votre nom: ");
            Scanner scanner = new Scanner(System.in);
            String playerName = scanner.nextLine();

            // envoi du nom du joueur au serveur
            output.writeUTF(playerName);

            while (true) {
                // en attente d'une instruction du serveur
                String messageType = input.readUTF();
                if (messageType.equals("QUESTION")) {
                    // lecture de la question et des choix
                    String questionText = input.readUTF();
                    String[] choices = new String[4];

                    for (int i = 0; i < 4; i++) {
                        choices[i] = input.readUTF();
                    }

                    // impression de la question et des choix
                    System.out.println("Question: " + questionText);
                    for (int i = 0; i < choices.length; i++) {
                        System.out.println((char) ('A' + i) + ". " + choices[i]);
                    }

                    // demande une réponse à l'utilisateur
                    System.out.print("Votre Réponse (A/B/C/D): ");
                    String userAnswer = scanner.nextLine();

                    // envoi de la réponse au serveur
                    output.writeUTF(userAnswer);
                } else if (messageType.equals("STATISTICS")) {
                    // Le serveur envoie des statistiques
                    String correctAnswer = input.readUTF();
                    // Recevoir et afficher les statistiques pour chaque réponse
                    for (int i = 0; i < 4; i++) {
                        String playerAnswer = input.readUTF();
                        System.out.println("Player " + (char) ('A' + i) + ": " + playerAnswer);
                    }

                } else if (messageType.equals("WINNER")) {
                    // Le serveur déclare un gagnant
                    String winnerName = input.readUTF();
                    int winnerScore = input.readInt();
                    System.out.println("Game over! Winner: " + winnerName + " avec une note de " + winnerScore);

                } else if (messageType.equals("GAME_END")) {
                    // Le serveur indique la fin du jeu
                    System.out.println("Le jeu est terminé. Merci d'avoir joué!");
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
