package survey.shakya.sugan.surveyapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import survey.shakya.sugan.surveyapp.model.Question;
import survey.shakya.sugan.surveyapp.model.Response;
import survey.shakya.sugan.surveyapp.model.Survey;
import survey.shakya.sugan.surveyapp.model.User;

public class DataHelper extends SQLiteOpenHelper {
    private static DataHelper instance;
    private static String TAG = DataHelper.class.getName();
    private static final String COMMA = ", ";

    private DataHelper(Context context) {
        super(context, SurveyData.DATABASE, null, SurveyData.DATABASE_VERSION);
    }

    public static synchronized DataHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DataHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create
        db.execSQL("CREATE TABLE " + SurveyData.USER_TABLE + " ( " +
                SurveyData.USER_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SurveyData.USER_COL_FIRSTNAME + " TEXT, " +
                SurveyData.USER_COL_LASTNAME + " TEXT, " +
                SurveyData.USER_COL_USERNAME + " TEXT NOT NULL UNIQUE, " +
                SurveyData.USER_COL_PASSWORD + " TEXT NOT NULL, " +
                SurveyData.USER_COL_TYPE + " TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + SurveyData.SURVEY_TABLE + " ( " +
                SurveyData.SURVEY_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SurveyData.SURVEY_COL_NAME + " TEXT NOT NULL, " +
                SurveyData.SURVEY_COL_SURVEYER + " INTEGER, " +
                "FOREIGN KEY (" + SurveyData.SURVEY_COL_SURVEYER + ") REFERENCES " + SurveyData.USER_TABLE + "(" + SurveyData.USER_COL_ID + "), " +
                "UNIQUE (" + SurveyData.SURVEY_COL_NAME + "," + SurveyData.SURVEY_COL_SURVEYER + "))");

        db.execSQL("CREATE TABLE " + SurveyData.QUESTION_TABLE + " ( " +
                SurveyData.QUESTION_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SurveyData.QUESTION_COL_QUESTION + " TEXT NOT NULL, " +
                SurveyData.QUESTION_COL_TYPE + " TEXT NOT NULL, " +
                SurveyData.QUESTION_COL_OPTIONS + " TEXT, " +
                SurveyData.QUESTION_COL_SURVEY + " INTEGER, " +
                "FOREIGN KEY (" + SurveyData.QUESTION_COL_SURVEY + ") REFERENCES " + SurveyData.SURVEY_TABLE + "(" + SurveyData.QUESTION_COL_ID + "))");

        db.execSQL("CREATE TABLE " + SurveyData.RESPONSE_TABLE + " ( " +
                SurveyData.RESPONSE_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SurveyData.RESPONSE_COL_RESPONSE + " TEXT NOT NULL, " +
                SurveyData.RESPONSE_COL_QUESTION + " INTEGER, " +
                SurveyData.RESPONSE_COL_SURVEYEE + " INTEGER, " +
                "FOREIGN KEY (" + SurveyData.RESPONSE_COL_SURVEYEE + ") REFERENCES " + SurveyData.USER_TABLE + "(" + SurveyData.USER_COL_ID + "), " +
                "FOREIGN KEY (" + SurveyData.RESPONSE_COL_QUESTION + ") REFERENCES " + SurveyData.QUESTION_TABLE + "(" + SurveyData.QUESTION_COL_ID + "), " +
                "UNIQUE (" + SurveyData.RESPONSE_COL_QUESTION + COMMA + SurveyData.RESPONSE_COL_SURVEYEE + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            deleteTable(db);
            onCreate(db);
        }
    }

    public void deleteTable(SQLiteDatabase database) {
        database.execSQL("DROP TABLE IF EXISTS " + SurveyData.USER_TABLE);
        database.execSQL("DROP TABLE IF EXISTS " + SurveyData.SURVEY_TABLE);
        database.execSQL("DROP TABLE IF EXISTS " + SurveyData.QUESTION_TABLE);
        database.execSQL("DROP TABLE IF EXISTS " + SurveyData.RESPONSE_TABLE);
    }

    // ** GET METHODS ** //
    // SURVEYER
    public User getUser(String username) {
        User user = null;
        SQLiteDatabase db = getReadableDatabase();
        db.beginTransaction();
        Cursor cursor = db.rawQuery("SELECT * FROM " + SurveyData.USER_TABLE + " WHERE LOWER(" +
                SurveyData.USER_COL_USERNAME + ") = ?", new String[]{username.toLowerCase()});
        if (cursor.moveToFirst()) {
            user = new User();
            user.setUsername(username);
            user.setId(cursor.getInt(cursor.getColumnIndex(SurveyData.USER_COL_ID)));
            user.setFirstName(cursor.getString(cursor.getColumnIndex(SurveyData.USER_COL_FIRSTNAME)));
            user.setLastName(cursor.getString(cursor.getColumnIndex(SurveyData.USER_COL_LASTNAME)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(SurveyData.USER_COL_PASSWORD)));
            user.setUserType(User.UserType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SurveyData.USER_COL_TYPE))));
        }
        cursor.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return user;
    }

    public User getUser(int id) {
        User user = null;
        SQLiteDatabase db = getReadableDatabase();
        db.beginTransaction();
        Cursor cursor = db.rawQuery("SELECT * FROM " + SurveyData.USER_TABLE + " WHERE " +
                SurveyData.USER_COL_ID + " = ?", new String[]{"" + id});
        if (cursor.moveToFirst()) {
            user = new User();
            user.setId(id);
            user.setFirstName(cursor.getString(cursor.getColumnIndex(SurveyData.USER_COL_FIRSTNAME)));
            user.setLastName(cursor.getString(cursor.getColumnIndex(SurveyData.USER_COL_LASTNAME)));
            user.setUsername(cursor.getString(cursor.getColumnIndex(SurveyData.USER_COL_USERNAME)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(SurveyData.USER_COL_PASSWORD)));
            user.setUserType(User.UserType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(SurveyData.USER_COL_TYPE))));
        }
        cursor.close();
        db.endTransaction();
        db.close();
        return user;
    }

    public Survey getSurvey(int surveyId) {
        Survey survey = null;
        SQLiteDatabase db = getReadableDatabase();
        db.beginTransaction();
        Cursor cursor = db.rawQuery("SELECT * FROM " + SurveyData.SURVEY_TABLE + " WHERE " +
                SurveyData.SURVEY_COL_ID + " = ?", new String[]{""+surveyId});
        if (cursor.moveToFirst()) {
            survey = new Survey();
            survey.setId(surveyId);
            survey.setName(cursor.getString(cursor.getColumnIndex(SurveyData.SURVEY_COL_NAME)));
            survey.setSurveyerId(cursor.getInt(cursor.getColumnIndex(SurveyData.SURVEY_COL_SURVEYER)));
        }
        cursor.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return survey;
    }

    public Survey getSurvey(int surveyerId, String surveyName) {
        Survey survey = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + SurveyData.SURVEY_TABLE + " WHERE " +
                SurveyData.SURVEY_COL_NAME + " = ? AND " + SurveyData.SURVEY_COL_SURVEYER + " = ?",
                new String[]{surveyName,  ""+surveyerId});
        if (cursor.moveToFirst()) {
            survey = new Survey();
            survey.setId(cursor.getInt(cursor.getColumnIndex(SurveyData.SURVEY_COL_ID)));
            survey.setName(surveyName);
            survey.setSurveyerId(surveyerId);
        }
        cursor.close();
        return survey;
    }


    public List<Survey> getSurveyList(int surveyerId) {
        List<Survey> surveyList = null;
        SQLiteDatabase db = getReadableDatabase();
        db.beginTransaction();
        Cursor cursor = db.rawQuery("SELECT * FROM " + SurveyData.SURVEY_TABLE + " WHERE " +
                SurveyData.SURVEY_COL_SURVEYER + " = ?", new String[]{"" + surveyerId});
        if (cursor.moveToFirst()) {
            surveyList = new ArrayList<>();
            do {
                Survey survey = new Survey();
                survey.setSurveyerId(surveyerId);
                survey.setId(cursor.getInt(cursor.getColumnIndex(SurveyData.SURVEY_COL_ID)));
                survey.setName(cursor.getString(cursor.getColumnIndex(SurveyData.SURVEY_COL_NAME)));
                surveyList.add(survey);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return surveyList;
    }

    public List<Survey> getAllSurveys() {
        List<Survey> surveyList = null;
        SQLiteDatabase db = getReadableDatabase();
        db.beginTransaction();
        Cursor cursor = db.rawQuery("SELECT * FROM " + SurveyData.SURVEY_TABLE, null);
        if (cursor.moveToFirst()) {
            surveyList = new ArrayList<>();
            do {
                Survey survey = new Survey();
                survey.setId(cursor.getInt(cursor.getColumnIndex(SurveyData.SURVEY_COL_ID)));
                survey.setName(cursor.getString(cursor.getColumnIndex(SurveyData.SURVEY_COL_NAME)));
                survey.setSurveyerId(cursor.getInt(cursor.getColumnIndex(SurveyData.SURVEY_COL_SURVEYER)));
                surveyList.add(survey);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return surveyList;
    }

    public List<User> getAllSurveyee() {
        List<User> surveyeeList = null;
        SQLiteDatabase db = getReadableDatabase();
        db.beginTransaction();
        Cursor cursor = db.rawQuery("SELECT * FROM " + SurveyData.USER_TABLE + " WHERE " +
                SurveyData.USER_COL_TYPE + " = ?", new String[]{"SURVEYEE"});
        if (cursor.moveToFirst()) {
            surveyeeList = new ArrayList<>();
            do {
                User surveyee = new User();
                surveyee.setId(cursor.getInt(cursor.getColumnIndex(SurveyData.USER_COL_ID)));
                surveyee.setUserType(User.UserType.valueOf(cursor.getString(cursor.getColumnIndex(SurveyData.USER_COL_TYPE))));
                surveyee.setUsername(cursor.getString(cursor.getColumnIndex(SurveyData.USER_COL_USERNAME)));
                surveyee.setFirstName(cursor.getString(cursor.getColumnIndex(SurveyData.USER_COL_FIRSTNAME)));
                surveyee.setLastName(cursor.getString(cursor.getColumnIndex(SurveyData.USER_COL_LASTNAME)));

                surveyeeList.add(surveyee);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return surveyeeList;
    }

    public List<String> getAllSurveyeeName() {
        List<String> nameList = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + SurveyData.USER_TABLE + " WHERE " +
                SurveyData.USER_COL_TYPE + " = ?", new String[]{"SURVEYEE"});
        if (cursor.moveToFirst()) {
            nameList = new ArrayList<>();
            do {
                nameList.add(cursor.getString(cursor.getColumnIndex(SurveyData.USER_COL_FIRSTNAME)) + " " +
                                cursor.getString(cursor.getColumnIndex(SurveyData.USER_COL_LASTNAME)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return nameList;
    }

    public Question getQuestion(int questionId) {
        Question question = null;
        SQLiteDatabase db = getReadableDatabase();
        db.beginTransaction();
        Cursor cursor = db.rawQuery("SELECT * FROM " + SurveyData.QUESTION_TABLE + " WHERE " +
                SurveyData.QUESTION_COL_ID + " = ?", new String[]{""+questionId});
        if (cursor.moveToFirst()) {
            question = new Question();
            question.setId(questionId);
            question.setQuestion(cursor.getString(cursor.getColumnIndexOrThrow(SurveyData.QUESTION_COL_QUESTION)));
            question.setType(cursor.getString(cursor.getColumnIndexOrThrow(SurveyData.QUESTION_COL_TYPE)));
            question.setOptions(cursor.getString(cursor.getColumnIndexOrThrow(SurveyData.QUESTION_COL_OPTIONS)));
            question.setSurveyId(cursor.getInt(cursor.getColumnIndexOrThrow(SurveyData.QUESTION_COL_SURVEY)));
        }
        cursor.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return question;
    }

    public List<Question> getQuestionList(int surveyId) {
        List<Question> questionList = null;
        SQLiteDatabase db = getReadableDatabase();
        db.beginTransaction();
        Cursor cursor = db.rawQuery("SELECT * FROM " + SurveyData.QUESTION_TABLE + " WHERE " +
                SurveyData.QUESTION_COL_SURVEY + " = ?", new String[]{"" + surveyId});
        if (cursor.moveToFirst()) {
            questionList = new ArrayList<>();
            do {
                Question question = new Question();
                question.setSurveyId(surveyId);
                question.setId(cursor.getInt(cursor.getColumnIndex(SurveyData.QUESTION_COL_ID)));
                question.setQuestion(cursor.getString(cursor.getColumnIndex(SurveyData.QUESTION_COL_QUESTION)));
                question.setType(cursor.getString(cursor.getColumnIndex(SurveyData.QUESTION_COL_TYPE)));
                question.setOptions(cursor.getString(cursor.getColumnIndex(SurveyData.QUESTION_COL_OPTIONS)));
                questionList.add(question);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return questionList;
    }

    public List<Response> getResponseListBySurveyee(int surveyeeId) {
        List<Response> responseList = null;
        SQLiteDatabase db = getReadableDatabase();
        db.beginTransaction();
        Cursor cursor = db.rawQuery("SELECT * FROM " + SurveyData.RESPONSE_TABLE + " WHERE " +
                SurveyData.RESPONSE_COL_SURVEYEE + " = ?", new String[]{"" + surveyeeId});
        if (cursor.moveToFirst()) {
            responseList = new ArrayList<>();
            do {
                Response response = new Response();
                response.setId(cursor.getInt(cursor.getColumnIndex(SurveyData.RESPONSE_COL_ID)));
                response.setSurveyeeId(surveyeeId);
                response.setQuestionId(cursor.getInt(cursor.getColumnIndex(SurveyData.RESPONSE_COL_QUESTION)));
                response.setResponse(cursor.getString(cursor.getColumnIndex(SurveyData.RESPONSE_COL_RESPONSE)));
                responseList.add(response);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return responseList;
    }

    public List<Response> getResponseListByQuestion(int questionId) {
        List<Response> responseList = null;
        SQLiteDatabase db = getReadableDatabase();
        db.beginTransaction();
        Cursor cursor = db.rawQuery("SELECT * FROM " + SurveyData.RESPONSE_TABLE + " WHERE " +
                SurveyData.RESPONSE_COL_QUESTION + " = ?", new String[]{"" + questionId});
        if (cursor.moveToFirst()) {
            responseList = new ArrayList<>();
            do {
                Response response = new Response();
                response.setId(cursor.getInt(cursor.getColumnIndex(SurveyData.RESPONSE_COL_ID)));
                response.setQuestionId(questionId);
                response.setSurveyeeId(cursor.getInt(cursor.getColumnIndex(SurveyData.RESPONSE_COL_SURVEYEE)));
                response.setResponse(cursor.getString(cursor.getColumnIndex(SurveyData.RESPONSE_COL_RESPONSE)));
                responseList.add(response);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return responseList;
    }

    public Response getResponse(int surveyeeId, int questionId) {
        Response response = null;
        SQLiteDatabase db = getReadableDatabase();
        db.beginTransaction();
        Cursor cursor = db.rawQuery("SELECT * FROM " + SurveyData.RESPONSE_TABLE + " WHERE " +
                        SurveyData.RESPONSE_COL_QUESTION + " = ? AND " + SurveyData.RESPONSE_COL_SURVEYEE + " = ?",
                new String[]{"" + questionId, "" + surveyeeId});
        if (cursor.moveToFirst()) {
            response = new Response();
            response.setId(cursor.getInt(cursor.getColumnIndex(SurveyData.RESPONSE_COL_ID)));
            response.setQuestionId(questionId);
            response.setSurveyeeId(cursor.getInt(cursor.getColumnIndex(SurveyData.RESPONSE_COL_SURVEYEE)));
            response.setResponse(cursor.getString(cursor.getColumnIndex(SurveyData.RESPONSE_COL_RESPONSE)));
        }
        cursor.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return response;
    }

    // INSERT
    public long insertUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SurveyData.USER_COL_USERNAME, user.getUsername());
        contentValues.put(SurveyData.USER_COL_PASSWORD, user.getPassword());
        contentValues.put(SurveyData.USER_COL_FIRSTNAME, user.getFirstName());
        contentValues.put(SurveyData.USER_COL_LASTNAME, user.getLastName());
        contentValues.put(SurveyData.USER_COL_TYPE, user.getUserType().name());

        long value = db.insertOrThrow(SurveyData.USER_TABLE, null, contentValues);
        db.setTransactionSuccessful();
        db.endTransaction();
        return value;
    }

    public long insertSurvey(Survey survey) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SurveyData.SURVEY_COL_NAME, survey.getName());
        contentValues.put(SurveyData.SURVEY_COL_SURVEYER, survey.getSurveyerId());
        long value = db.insertOrThrow(SurveyData.SURVEY_TABLE, null, contentValues);
        db.setTransactionSuccessful();
        db.endTransaction();
        return value;
    }

    public long insertQuestion(Question question) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SurveyData.QUESTION_COL_QUESTION, question.getQuestion());
        contentValues.put(SurveyData.QUESTION_COL_TYPE, question.getType().toString());
        contentValues.put(SurveyData.QUESTION_COL_OPTIONS, question.getOptions());
        contentValues.put(SurveyData.QUESTION_COL_SURVEY, question.getSurveyId());
        long value = db.insertOrThrow(SurveyData.QUESTION_TABLE, null, contentValues);
        db.setTransactionSuccessful();
        db.endTransaction();
        return value;
    }

    // UPDATE
    public int updateUser(int id, User user) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        Cursor cursor = db.rawQuery("UPDATE " + SurveyData.USER_TABLE + " SET " +
                        SurveyData.USER_COL_USERNAME + " = ?, " +
                        SurveyData.USER_COL_PASSWORD + " = ?, " +
                        SurveyData.USER_COL_FIRSTNAME + " = ?, " +
                        SurveyData.USER_COL_LASTNAME + " = ?, " +
                        SurveyData.USER_COL_TYPE + " = ? WHERE " +
                        SurveyData.USER_COL_ID + " = ?",
                new String[]{user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getUserTypeString(), "" + id});
        int count = cursor.getCount();
        cursor.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        return count;
    }

    public int updateSurvey(int id, Survey survey) {    // id value of survey is not considered
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        Cursor cursor = db.rawQuery("UPDATE " + SurveyData.SURVEY_TABLE + " SET " +
                        SurveyData.SURVEY_COL_NAME + " = ?, " +
                        SurveyData.SURVEY_COL_SURVEYER + " = ? WHERE " +
                        SurveyData.SURVEY_COL_ID + " = ?",
                new String[]{survey.getName(), "" + survey.getSurveyerId(), "" + id});
        int count = cursor.getCount();
        cursor.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        return count;
    }

    public int updateQuestion(int id, Question question) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        Cursor cursor = db.rawQuery("UPDATE " + SurveyData.QUESTION_TABLE + " SET " +
                        SurveyData.QUESTION_COL_QUESTION + " = ?, " +
                        SurveyData.QUESTION_COL_TYPE + " = ?, " +
                        SurveyData.QUESTION_COL_OPTIONS + " = ?, " +
                        SurveyData.QUESTION_COL_SURVEY + " = ? " +
                        "WHERE " + SurveyData.QUESTION_COL_ID + " = ?",
                new String[]{question.getQuestion(), question.getType().toString(),
                        question.getOptions(), "" + question.getSurveyId(), "" + id});
        int count = cursor.getCount();
        cursor.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return count;
    }

    public int replaceResponse(Response response) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        String replaceSql = "INSERT OR REPLACE INTO " + SurveyData.RESPONSE_TABLE + " (" +
                SurveyData.RESPONSE_COL_ID + COMMA + SurveyData.RESPONSE_COL_RESPONSE + COMMA +
                SurveyData.RESPONSE_COL_QUESTION + COMMA + SurveyData.RESPONSE_COL_SURVEYEE + ") VALUES" +
                " ((SELECT " + SurveyData.RESPONSE_COL_ID + " FROM " + SurveyData.RESPONSE_TABLE +
                " WHERE " + SurveyData.RESPONSE_COL_QUESTION + " = ? AND " + SurveyData.RESPONSE_COL_SURVEYEE + " = ?), ?, ?, ?)";


        Cursor cursor = db.rawQuery(replaceSql,
                new String[]{"" + response.getQuestionId(), "" + response.getSurveyeeId(), response.getResponse(), "" +
                        response.getQuestionId(), "" + response.getSurveyeeId()});
        int count = cursor.getCount();
        cursor.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return count;
    }

    // DELETE
    public void deleteUser(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        db.execSQL("DELETE FROM " + SurveyData.USER_TABLE +
                " WHERE " + SurveyData.USER_COL_ID + " = " + id);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public void deleteSurvey(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        db.execSQL("DELETE FROM " + SurveyData.SURVEY_TABLE +
                " WHERE " + SurveyData.SURVEY_COL_ID + " = " + id);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public void deleteQuestion(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        db.execSQL("DELETE FROM " + SurveyData.QUESTION_TABLE +
                        " WHERE " + SurveyData.QUESTION_COL_ID + " = " + id);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public void deleteResponse(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        db.execSQL("DELETE FROM " + SurveyData.RESPONSE_TABLE +
                " WHERE " + SurveyData.RESPONSE_COL_ID + " = " + id);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public boolean isSurveyPresent(int surveyerId, String surveyName) {
        SQLiteDatabase db = getReadableDatabase();
        db.beginTransaction();
        Cursor cursor = db.rawQuery("SELECT * FROM " + SurveyData.SURVEY_TABLE + " WHERE " +
                        SurveyData.SURVEY_COL_NAME + " = ? AND " +
                        SurveyData.SURVEY_COL_SURVEYER + " = ? ",
                new String[]{surveyName,  "" + surveyerId });
        int count = cursor.getCount();
        cursor.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return count == 1;
    }
}
