package survey.shakya.sugan.surveyapp.model;

/**
 * Created by sugan on 28/09/16.
 */

public class Response {
    int id;
    int surveyeeId;
    int questionId;
    String response;

    public Response() {
    }

    public Response(int id, int surveyeeId, int questionId, String response) {
        this.id = id;
        this.surveyeeId = surveyeeId;
        this.questionId = questionId;
        this.response = response;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSurveyeeId() {
        return surveyeeId;
    }

    public void setSurveyeeId(int surveyeeId) {
        this.surveyeeId = surveyeeId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
