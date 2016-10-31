package survey.shakya.sugan.surveyapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import survey.shakya.sugan.surveyapp.R;
import survey.shakya.sugan.surveyapp.adapter.ResponseAdapter;

public class ListResponseActivity extends AppCompatActivity {
    private static String TAG = ListResponseActivity.class.getName();
    int questionId;
    BaseAdapter responseAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_response_by_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Intent intent = getIntent();
        questionId = intent.getIntExtra("QUESTION_ID", -1);
        if (questionId == -1) {
            Toast.makeText(getApplicationContext(), "Invalid Question ID", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean isByUser = false;
        responseAdapter = new ResponseAdapter(getApplicationContext(), isByUser, null, questionId);

        listView = (ListView) findViewById(R.id.list_view_response_by_question);
        listView.setAdapter(responseAdapter);
    }

}
