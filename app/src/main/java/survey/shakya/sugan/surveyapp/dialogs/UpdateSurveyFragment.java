package survey.shakya.sugan.surveyapp.dialogs;

import android.app.Dialog;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import survey.shakya.sugan.surveyapp.R;
import survey.shakya.sugan.surveyapp.data.DataHelper;
import survey.shakya.sugan.surveyapp.model.Survey;

public class UpdateSurveyFragment extends DialogFragment {
    private static final String ARG_SURVEY_ID = "surveyId";

    private int surveyId;
    private Survey survey;

//    DataHelper dataHelper;

    public UpdateSurveyFragment() {
        // Required empty public constructor
    }

    public static UpdateSurveyFragment newInstance(int surveyId) {
        UpdateSurveyFragment fragment = new UpdateSurveyFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SURVEY_ID, surveyId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            surveyId = getArguments().getInt(ARG_SURVEY_ID);
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
        DataHelper dataHelper = DataHelper.getInstance(getContext());
        survey = dataHelper.getSurvey(surveyId);

        View view =  inflater.inflate(R.layout.fragment_update_survey, container, false);
        Dialog dialog= getDialog();

        if(dialog != null){
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        final EditText editText = (EditText) view.findViewById(R.id.edit_text_update_survey_name);
        editText.setText(survey.getName());
        Button button = (Button) view.findViewById(R.id.button_update_survey);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText.getText().toString();
                if(name == "" || name == null){
                    Toast.makeText(getContext(), "Enter Survey.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Survey s = new Survey(name, survey.getSurveyerId());
                DataHelper dataHelper = DataHelper.getInstance(getContext());
                long response =  dataHelper.updateSurvey(survey.getId(), s);
                if(response == -1){
                    Snackbar.make(getView(), "Error: updating Survey", Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(getView(), "Success: updating Survey", Snackbar.LENGTH_LONG).show();
                    dismiss();
                }

            }
        });
        return view;
    }
}
