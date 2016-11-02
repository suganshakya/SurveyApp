package survey.shakya.sugan.surveyapp.model;

/**
 * Created by sugan on 27/09/16.
 */

public class Question {
    public static final String FILL_IN_BLANK = "Fill in the blank";
    public static final String TRUE_FALSE = "True/False";
    public static final String SPINNER = "Spinner";
    public static final String RADIO = "Radio";

    int id;
    String question;
    String type;
    String options;
    int surveyId;

    public Question() {

    }

    public Question(String question, String type, String options, int surveyId) {
        this.question = question;
        this.type = type;
        this.options = options;
        this.surveyId = surveyId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public int getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(int surveyId) {
        this.surveyId = surveyId;
    }

    @Override
    public String toString(){
        return question;
    }

//    @Override
//    public String toString() {
//        return "Question{" +
//                "id=" + id +
//                ", question='" + question + '\'' +
//                ", type='" + type + '\'' +
//                ", options='" + options + '\'' +
//                ", surveyId=" + surveyId +
//                '}';
//    }
}
