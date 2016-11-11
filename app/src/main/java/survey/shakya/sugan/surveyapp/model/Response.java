package survey.shakya.sugan.surveyapp.model;

import android.content.Context;

import survey.shakya.sugan.surveyapp.data.DataHelper;

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

    public Response(int surveyeeId, int questionId, String response) {
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

    public String getValue(Context context, String s) {
        switch (s) {
            case "Question ID":
                return String.valueOf(questionId);

            case "Question":
                DataHelper dataHelper = DataHelper.getInstance(context);
                Question question = dataHelper.getQuestion(questionId);
                return question.getQuestion();

            case "Surveyee ID":
                return String.valueOf(surveyeeId);

            case "Surveyee Name":
                DataHelper dataHelper1 = DataHelper.getInstance(context);
                User surveyee = dataHelper1.getUser(surveyeeId);
                return surveyee.toString();

            case "Response":
                return response;
            default:
                return null;
        }
    }
}
