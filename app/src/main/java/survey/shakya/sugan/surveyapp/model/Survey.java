package survey.shakya.sugan.surveyapp.model;

/**
 * Created by sugan on 28/09/16.
 */

public class Survey {
    int id;
    String name;
    int surveyerId;

    public Survey() {
    }

    public Survey(int id, String name, int surveyerId) {
        this.id = id;
        this.name = name;
        this.surveyerId = surveyerId;
    }

    public Survey(String name, int surveyerId){
        this.name = name;
        this.surveyerId = surveyerId;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSurveyerId() {
        return surveyerId;
    }

    public void setSurveyerId(int surveyerId) {
        this.surveyerId = surveyerId;
    }

    @Override
    public String toString() {
        return "Survey{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surveyerId=" + surveyerId +
                '}';
    }
}
