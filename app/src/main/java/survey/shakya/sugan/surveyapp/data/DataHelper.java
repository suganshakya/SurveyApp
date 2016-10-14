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
import survey.shakya.sugan.surveyapp.model.Surveyee;
import survey.shakya.sugan.surveyapp.model.Surveyer;

public class DataHelper extends SQLiteOpenHelper {
    private static DataHelper instance;
    private static String TAG = DataHelper.class.getName();

    private static final String COMMA = ", ";

    private DataHelper(Context context) {
        super(context, SurveyData.DATABASE, null, SurveyData.DATABASE_VERSION);
    }

    public static synchronized DataHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DataHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create
        db.execSQL("CREATE TABLE " + SurveyData.SURVEYER_TABLE + " ( " +
                SurveyData.SURVEYER_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SurveyData.SURVEYER_COL_USERNAME + " TEXT NOT NULL, " +
                SurveyData.SURVEYER_COL_FIRSTNAME + " TEXT, " +
                SurveyData.SURVEYER_COL_LASTNAME + " TEXT, " +
                SurveyData.SURVEYER_COL_PASSWORD + " TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + SurveyData.SURVEYEE_TABLE + " ( " +
                SurveyData.SURVEYEE_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SurveyData.SURVEYEE_COL_FIRSTNAME + " TEXT NOT NULL, " +
                SurveyData.SURVEYER_COL_LASTNAME + " TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + SurveyData.SURVEY_TABLE + " ( " +
                SurveyData.SURVEY_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SurveyData.SURVEY_COL_NAME + " TEXT NOT NULL, " +
                SurveyData.SURVEY_COL_SURVEYER + " INTEGER, " +
                " FOREIGN KEY (" + SurveyData.SURVEY_COL_SURVEYER + ") REFERENCES " + SurveyData.SURVEYER_TABLE + "(" + SurveyData.SURVEYER_COL_ID + "))");

        db.execSQL("CREATE TABLE " + SurveyData.QUESTION_TABLE + " ( " +
                SurveyData.QUESTION_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SurveyData.QUESTION_COL_QUESTION + " TEXT NOT NULL, " +
                SurveyData.QUESTION_COL_TYPE + " TEXT NOT NULL, " +
                SurveyData.QUESTION_COL_OPTIONS + " TEXT, " +
                SurveyData.QUESTION_COL_SURVEY + " INTEGER, " +
                " FOREIGN KEY (" + SurveyData.QUESTION_COL_SURVEY + ") REFERENCES " + SurveyData.SURVEY_TABLE + "(" + SurveyData.QUESTION_COL_ID + "))");

        db.execSQL("CREATE TABLE " + SurveyData.RESPONSE_TABLE + " ( " +
                SurveyData.RESPONSE_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SurveyData.RESPONSE_COL_RESPONSE + " TEXT NOT NULL, " +
                SurveyData.RESPONSE_COL_SURVEYEE + " INTEGER, " +
                SurveyData.RESPONSE_COL_QUESTION + " INTEGER, " +
                "FOREIGN KEY (" + SurveyData.RESPONSE_COL_SURVEYEE + ") REFERENCES " + SurveyData.SURVEYEE_TABLE + "(" + SurveyData.SURVEYEE_COL_ID + "), " +
                "FOREIGN KEY (" + SurveyData.RESPONSE_COL_QUESTION + ") REFERENCES " + SurveyData.QUESTION_TABLE + "(" + SurveyData.QUESTION_COL_ID + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            deleteTable(db);
            onCreate(db);
        }
    }

    public void deleteTable(SQLiteDatabase database) {
        database.execSQL("DROP TABLE IF EXISTS " + SurveyData.SURVEYER_TABLE);
        database.execSQL("DROP TABLE IF EXISTS " + SurveyData.SURVEYEE_TABLE);

        database.execSQL("DROP TABLE IF EXISTS " + SurveyData.SURVEY_TABLE);
        database.execSQL("DROP TABLE IF EXISTS " + SurveyData.QUESTION_TABLE);
        database.execSQL("DROP TABLE IF EXISTS " + SurveyData.RESPONSE_TABLE);
    }

    // ** GET METHODS ** //
    // SURVEYER
    public Surveyer getSurveyer(String username) {
        Surveyer surveyer = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + SurveyData.SURVEYER_TABLE + " WHERE " +
                SurveyData.SURVEYER_COL_USERNAME + " = ?", new String[]{username});
        if (cursor.moveToFirst()) {
            surveyer = new Surveyer();
            surveyer.setUsername(username);
            surveyer.setId(cursor.getInt(cursor.getColumnIndex(SurveyData.SURVEYER_COL_ID)));
            surveyer.setFirstName(cursor.getString(cursor.getColumnIndex(SurveyData.SURVEYER_COL_FIRSTNAME)));
            surveyer.setLastName(cursor.getString(cursor.getColumnIndex(SurveyData.SURVEYER_COL_LASTNAME)));
            surveyer.setPassword(cursor.getString(cursor.getColumnIndex(SurveyData.SURVEYER_COL_PASSWORD)));
        }
        cursor.close();
        return surveyer;
    }

    // SURVEYEE
    public Surveyee getSurveyee(String firstName) {
        Surveyee surveyee = null;
        surveyee.setFirstName(firstName);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + SurveyData.SURVEYEE_TABLE + " WHERE " +
                SurveyData.SURVEYEE_COL_FIRSTNAME + " = ?", new String[]{firstName});
        if (cursor.moveToFirst()) {
            surveyee = new Surveyee();
            surveyee.setId(cursor.getInt(cursor.getColumnIndex(SurveyData.SURVEYEE_COL_ID)));
            surveyee.setLastName(cursor.getString(cursor.getColumnIndex(SurveyData.SURVEYEE_COL_LASTNAME)));
        }
        cursor.close();
        return surveyee;
    }

    public List<Survey> getSurveyList(int surveyerId) {
        List<Survey> surveyList = null;
        SQLiteDatabase db = getReadableDatabase();
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
        return surveyList;
    }

    public List<Question> getQuestionList(int surveyId) {
        List<Question> questionList = null;
        SQLiteDatabase db = getReadableDatabase();
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
        return questionList;
    }

    public List<Response> getResponseListBySurveyee(int surveyeeId) {
        List<Response> responseList = null;
        SQLiteDatabase db = getReadableDatabase();
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
        return responseList;
    }

    public List<Response> getResponseListByQuestion(int questionId) {
        List<Response> responseList = null;
        SQLiteDatabase db = getReadableDatabase();
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
        return responseList;
    }

    public Response getResponse(int surveyeeId, int questionId) {
        Response response = null;
        SQLiteDatabase db = getReadableDatabase();
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
        return response;
    }

    // INSERT
    public long insertSurveyer(Surveyer surveyer) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SurveyData.SURVEYER_COL_USERNAME, surveyer.getUsername());
        contentValues.put(SurveyData.SURVEYER_COL_PASSWORD, surveyer.getPassword());
        long value = db.insertOrThrow(SurveyData.SURVEYER_TABLE, null, contentValues);
        db.setTransactionSuccessful();
        db.endTransaction();
        return value;
    }

    public long insertSurveyee(Surveyee surveyee) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SurveyData.SURVEYEE_COL_FIRSTNAME, surveyee.getFirstName());
        contentValues.put(SurveyData.SURVEYEE_COL_LASTNAME, surveyee.getLastName());
        long value = db.insertOrThrow(SurveyData.SURVEYEE_TABLE, null, contentValues);
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
        contentValues.put(SurveyData.QUESTION_COL_TYPE, question.getType());
        contentValues.put(SurveyData.QUESTION_COL_OPTIONS, question.getOptions());
        contentValues.put(SurveyData.QUESTION_COL_SURVEY, question.getSurveyId());
        long value = db.insertOrThrow(SurveyData.QUESTION_TABLE, null, contentValues);
        db.setTransactionSuccessful();
        db.endTransaction();
        return value;
    }

    public long insertResponse(Response response) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SurveyData.RESPONSE_COL_RESPONSE, response.getResponse());
        contentValues.put(SurveyData.RESPONSE_COL_QUESTION, response.getQuestionId());
        contentValues.put(SurveyData.RESPONSE_COL_SURVEYEE, response.getSurveyeeId());

        long value = db.insertOrThrow(SurveyData.RESPONSE_TABLE, null, contentValues);
        db.setTransactionSuccessful();
        db.endTransaction();
        return value;
    }


    // UPDATE
    public int updateSurveyer(int id, Surveyer surveyer) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        Cursor cursor = db.rawQuery("UPDATE " + SurveyData.SURVEYER_TABLE + " SET " +
                        SurveyData.SURVEYER_COL_USERNAME + " = ? , " +
                        SurveyData.SURVEYER_COL_FIRSTNAME + " = ?, " +
                        SurveyData.SURVEYER_COL_LASTNAME + " = ?, " +
                        SurveyData.SURVEYER_COL_PASSWORD + " = ? WHERE " +
                        SurveyData.SURVEYER_COL_ID + " = ?",
                new String[]{surveyer.getUsername(), surveyer.getFirstName(), surveyer.getLastName(), surveyer.getPassword(), "" + id});
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int updateSurveyee(int id, Surveyee surveyee) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        Cursor cursor = db.rawQuery("UPDATE " + SurveyData.SURVEYEE_TABLE + " SET " +
                        SurveyData.SURVEYEE_COL_FIRSTNAME + " = ?, " +
                        SurveyData.SURVEYEE_COL_LASTNAME + " = ? WHERE " +
                        SurveyData.SURVEYEE_COL_ID + " = ?",
                new String[]{surveyee.getFirstName(), surveyee.getLastName(), "" + id});
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int updateSurvey(int id, Survey survey) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        Cursor cursor = db.rawQuery("UPDATE " + SurveyData.SURVEY_TABLE + " SET " +
                        SurveyData.SURVEY_COL_NAME + " = ?, " +
                        SurveyData.SURVEY_COL_SURVEYER + " = ? WHERE " +
                        SurveyData.SURVEY_COL_ID + " = ?",
                new String[]{survey.getName(), "" + survey.getSurveyerId(), "" + id});
        int count = cursor.getCount();
        cursor.close();
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
                new String[]{question.getQuestion(), question.getType(),
                        question.getOptions(), "" + question.getSurveyId(), "" + id});
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int updateResponse(int id, Response response) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        Cursor cursor = db.rawQuery("UPDATE " + SurveyData.RESPONSE_TABLE + " SET " +
                        SurveyData.RESPONSE_COL_RESPONSE + " = ?, " +
                        SurveyData.RESPONSE_COL_SURVEYEE + " = ? " +
                        SurveyData.RESPONSE_COL_QUESTION + " = ? " +
                        "WHERE " + SurveyData.RESPONSE_COL_ID + " = ?",
                new String[]{response.getResponse(), "" + response.getSurveyeeId(), "" +
                        response.getQuestionId(), "" + id});
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    // DELETE
    public void deleteSurveyer(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(SurveyData.SURVEYER_TABLE, SurveyData.SURVEYER_COL_ID + " LIKE ?", new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error deleting Appointment");
        } finally {
            db.endTransaction();
        }
    }

    public void deleteSurveyee(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        db.delete(SurveyData.SURVEYEE_TABLE, SurveyData.SURVEYEE_COL_ID + " = " + id, null);
        db.endTransaction();
    }

    public void deleteSurvey(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        db.delete(SurveyData.SURVEY_TABLE, SurveyData.SURVEY_COL_ID + " = " + id, null);
        db.endTransaction();
    }

    public void deleteQuestion(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        db.delete(SurveyData.QUESTION_TABLE, SurveyData.QUESTION_COL_ID + " = " + id, null);
        db.endTransaction();
    }

    public void deleteResponse(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        db.delete(SurveyData.SURVEYEE_TABLE, SurveyData.SURVEYEE_COL_ID + " = " + id, null);
        db.endTransaction();
    }

}
