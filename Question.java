public class Question {

    // variables au niveau de l'instance pour stocker les questions, les choix et la réponse correcte
    private String question;
    private String choice1;
    private String choice2;
    private String choice3;
    private String choice4;
    private String answer;

    // constructeur paramétré
    public Question(String question, String choice1, String choice2, String choice3, String choice4, String answer) {
        this.question = question;
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.choice3 = choice3;
        this.choice4 = choice4;
        this.answer = answer;
    }

    // getters
    public String getQuestion() {
        return question;
    }

    public String getChoice1() {
        return choice1;
    }

    public String getChoice2() {
        return choice2;
    }

    public String getChoice3() {
        return choice3;
    }

    public String getChoice4() {
        return choice4;
    }

    public String getAnswer() {
        return answer;
    }
}