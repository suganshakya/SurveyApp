package survey.shakya.sugan.surveyapp.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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

public class UpdateQuestionFragment extends DialogFragment {
    private static final String ARG_QUESTION_ID = "questionId";

    private int questionId;
    private Question question;

//    DataHelper dataHelper;

    public UpdateQuestionFragment() {
        // Required empty public constructor
    }

    public static UpdateQuestionFragment newInstance(int questionId) {
        UpdateQuestionFragment fragment = new UpdateQuestionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_QUESTION_ID, questionId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            questionId = getArguments().getInt(ARG_QUESTION_ID);
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
        question = dataHelper.getQuestion(questionId);
        View view = inflater.inflate(R.layout.fragment_update_question, container, false);
        Dialog dialog= getDialog();
        if(dialog != null){
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        final EditText editText = (EditText) view.findViewById(R.id.edit_text_fragment_question_text);
        editText.setText(question.getQuestion());
        final Spinner spinner = (Spinner) view.findViewById(R.id.spinner_question_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.question_type_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final EditText optionsET = (EditText) view.findViewById(R.id.edit_text_question_options);
        optionsET.setText(question.getOptions());

        Button button = (Button) view.findViewById(R.id.button_update_question);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Question q = new Question();
                String questionText = editText.getText().toString();
                if(questionText == "" || questionText == null){
                    Toast.makeText(getContext(), "Enter Question.", Toast.LENGTH_SHORT).show();
                    return;
                }
                q.setQuestion(questionText);
                String questionType = spinner.getSelectedItem().toString();
                if(questionType == "" || questionType == null){
                    Toast.makeText(getContext(), "Select Question Type", Toast.LENGTH_SHORT).show();
                    return;
                }
                q.setType(questionType);

                String options = optionsET.getText().toString();
                if(questionType != "True/False" && (options == "" || options == null)){
                    Toast.makeText(getContext(), "Enter Question Options", Toast.LENGTH_SHORT).show();
                    return;
                }
                q.setOptions(options);
                q.setSurveyId(question.getSurveyId());
                DataHelper dataHelper = DataHelper.getInstance(getContext());
                long result = dataHelper.updateQuestion(question.getId(), q);
                if(result == -1){
                    Toast.makeText(getContext(), "Error updating the question", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Success! Updating the question", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }
        });
        return view;
    }

}
