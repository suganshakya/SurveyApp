package survey.shakya.sugan.surveyapp.model;

/**
 * Created by sugan on 27/09/16.
 */

public class Question {
    public enum Type {
        FILL_IN_THE_BLANK, TRUE_FALSE, RADIO, SPINNER;

        public static Type fromString(String tradeType){

            try{
                return Type.valueOf(tradeType.replaceAll(" ", "_"));
            }catch(IllegalArgumentException e){
                throw new RuntimeException(tradeType + " is not supported question type");
            }
        }

        @Override
        public String toString(){
            return super.toString().replaceAll("_", " ");
        }
    }



    int id;
    String question;
    Type type;
    String options;
    int surveyId;

    public Question() {

    }

    public Question(String question, Type type, String options, int surveyId) {
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setType(String typeStr){
        this.type = Type.valueOf(typeStr.replace(" ", "_"));
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        if(options == null){
            this.options = null;
        } else {
            this.options = options.trim().replaceAll("\\s*,\\s*", ",").replaceAll(",$", "");
        }
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
}
