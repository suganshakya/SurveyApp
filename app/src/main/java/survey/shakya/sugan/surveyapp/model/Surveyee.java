package survey.shakya.sugan.surveyapp.model;

/**
 * Created by sugan on 28/09/16.
 */

public class Surveyee {
    int id;
    String firstName;
    String lastName;

    public Surveyee() {
    }

    public Surveyee(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}
