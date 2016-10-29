package survey.shakya.sugan.surveyapp.data;

/**
 * Created by sugan on 27/09/16.
 */

public class SurveyData {
    public static final String DATABASE = "survey.db";
    public static final int DATABASE_VERSION = 1;
	// Name of the tables
    public static final String USER_TABLE = "user";
    public static final String SURVEY_TABLE = "survey";
    public static final String QUESTION_TABLE = "question";
    public static final String RESPONSE_TABLE = "response";

    // User Table
    public static final String USER_COL_ID = "id";
    public static final String USER_COL_USERNAME = "username";
    public static final String USER_COL_PASSWORD = "password";
    public static final String USER_COL_FIRSTNAME = "firstname";
    public static final String USER_COL_LASTNAME = "lastname";
    public static final String USER_COL_TYPE = "type";

    public static final String SURVEY_COL_ID = "id";
    public static final String SURVEY_COL_NAME = "name";
    public static final String SURVEY_COL_SURVEYER = "surveyer";

    public static final String QUESTION_COL_ID = "id";
    public static final String QUESTION_COL_QUESTION = "question";
    public static final String QUESTION_COL_TYPE = "type";
    public static final String QUESTION_COL_OPTIONS = "options";
    public static final String QUESTION_COL_SURVEY = "survey";

    public static final String RESPONSE_COL_ID = "id";
    public static final String RESPONSE_COL_SURVEYEE = "surveyee";
    public static final String RESPONSE_COL_QUESTION = "question";
    public static final String RESPONSE_COL_RESPONSE = "response";
}
