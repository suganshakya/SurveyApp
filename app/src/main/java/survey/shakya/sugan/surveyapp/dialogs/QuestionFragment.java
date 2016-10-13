package survey.shakya.sugan.surveyapp.dialogs;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import survey.shakya.sugan.surveyapp.R;
import survey.shakya.sugan.surveyapp.data.DataHelper;
import survey.shakya.sugan.surveyapp.model.Question;

public class QuestionFragment extends DialogFragment {
    private static final String ARG_SURVEY_ID = "surveyId";

    private int surveyId;

    public QuestionFragment() {
        // Required empty public constructor
    }

    public static QuestionFragment newInstance(int surveyId) {
        QuestionFragment fragment = new QuestionFragment();
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
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        Dialog dialog= getDialog();
        if(dialog != null){
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        final EditText editText = (EditText) view.findViewById(R.id.edit_text_fragment_question_text);
        final Spinner spinner = (Spinner) view.findViewById(R.id.spinner_question_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.question_type_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final EditText optionsET = (EditText) view.findViewById(R.id.edit_text_question_options);
        Button button = (Button) view.findViewById(R.id.button_create_question);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = editText.getText().toString();
                if(question == "" || question == null){
                    Toast.makeText(getContext(), "Enter Question.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String questionType = spinner.getSelectedItem().toString();
                if(questionType == "" || questionType == null){
                    Toast.makeText(getContext(), "Select Question Type", Toast.LENGTH_SHORT).show();
                    return;
                }

                String options = optionsET.getText().toString();
                if(questionType != "True/False" && (options == "" || options == null)){
                    Toast.makeText(getContext(), "Enter Question Options", Toast.LENGTH_SHORT).show();
                    return;
                }

                Question question1 = new Question(question, questionType , options, surveyId);

                DataHelper dataHelper = DataHelper.getInstance(getContext());

                long result = dataHelper.insertQuestion(question1);
                if(result == -1){
                    Toast.makeText(getContext(), "Error inserting a new Question", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Success! Inserting a new Question", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }
        });
        return view;
    }
}
