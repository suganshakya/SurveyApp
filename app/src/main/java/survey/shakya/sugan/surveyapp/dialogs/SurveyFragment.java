package survey.shakya.sugan.surveyapp.dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import survey.shakya.sugan.surveyapp.R;
import survey.shakya.sugan.surveyapp.activity.ListSurveyActivity;
import survey.shakya.sugan.surveyapp.data.DataHelper;
import survey.shakya.sugan.surveyapp.model.Survey;

public class SurveyFragment extends DialogFragment {
    private static String TAG = SurveyFragment.class.getName();


    private static final String ARG_SURVEYER_ID = "surveyerId";

    private int surveyerId;

    public SurveyFragment() {
        // Required empty public constructor
    }

    public static SurveyFragment newInstance(int surveyerId) {
        SurveyFragment fragment = new SurveyFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SURVEYER_ID, surveyerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            surveyerId = getArguments().getInt(ARG_SURVEYER_ID);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_survey, container, false);
        Dialog dialog= getDialog();
        if(dialog != null){
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        final EditText editText = (EditText) view.findViewById(R.id.edit_text_fragment_survey_name);
        Button button = (Button) view.findViewById(R.id.button_create_survey);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String surveyName = editText.getText().toString();
                if(surveyName == "" || surveyName == null){
                    Toast.makeText(getContext(), "Enter valid survey Name.", Toast.LENGTH_SHORT).show();
                    return;
                }
                insertSurvey(surveyName);
            }
        });
        return view;
    }

    public void insertSurvey(String surveyName){

        DataHelper dataHelper = DataHelper.getInstance(getContext());
        Survey survey = new Survey(surveyName, surveyerId);
        long result = dataHelper.insertSurvey(survey);
        if(result == -1){
            Toast.makeText(getContext(), "Error inserting a new Survey", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Success! Inserting a new Survey", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), ListSurveyActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void dismiss() {
        Log.i(TAG, "SurveyFragment Dismissed.");
        super.dismiss();
    }
}
