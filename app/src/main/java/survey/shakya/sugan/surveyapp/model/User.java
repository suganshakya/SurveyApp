package survey.shakya.sugan.surveyapp.model;

/**
 * Created by sugan on 29/10/16.
 */

public class User {
    int id;
    String username;
    String firstName;
    String lastName;
    String password;
    UserType userType;

    public enum UserType {
        SURVEYER, SURVEYEE;
    }

    public User() {
    }

    public User(String firstName, String lastName, String username, String password, UserType userType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getUserType() {
        return userType;
    }

    public String getUserTypeString(){
        return userType.name();
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
