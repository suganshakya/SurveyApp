package survey.shakya.sugan.surveyapp.adapter;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import survey.shakya.sugan.surveyapp.R;
import survey.shakya.sugan.surveyapp.activity.ListQuestionActivity;
import survey.shakya.sugan.surveyapp.data.DataHelper;
import survey.shakya.sugan.surveyapp.dialogs.UpdateQuestionFragment;
import survey.shakya.sugan.surveyapp.model.Question;
import survey.shakya.sugan.surveyapp.model.User;

/**
 * Created by sugan on 08/10/16.
 */

public class QuestionAdapter extends BaseAdapter {
    private static String TAG = QuestionAdapter.class.getName();

    private static final int TYPE_COUNT = 5;

    private static final int FILL_TYPE = 1;
    private static final int TRUE_FALSE_TYPE = 2;
    private static final int RADIO_TYPE = 3;
    private static final int SPINNER_TYPE = 4;

    private AppCompatActivity activity;
    private Context context;
    List<Question> questionList = new ArrayList<>();
    private int surveyId;
    private int userId;
    private User user;

    private Map<Integer, Integer> resultMap1 = new HashMap<Integer, Integer>();
    private Map<Integer, String> resultMap2 = new HashMap<Integer, String>();

    public QuestionAdapter(AppCompatActivity activity, Context context, int userId, int surveyId) {
        this.activity = activity;
        this.context = context;
        this.surveyId = surveyId;
        this.userId = userId;
        DataHelper helper = DataHelper.getInstance(this.context);
        user = helper.getUser(userId);
        questionList = helper.getQuestionList(surveyId);
    }

    @Override
    public int getCount() {
        if (questionList == null)
            return 0;
        return questionList.size();
    }

    @Override
    public Object getItem(int position) {
        if (questionList == null) {
            return null;
        }
        return questionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        Question question = questionList.get(position);
        switch (question.getType()) {
            case FILL_IN_THE_BLANK:
                return FILL_TYPE;
            case TRUE_FALSE:
                return TRUE_FALSE_TYPE;
            case RADIO:
                return RADIO_TYPE;
            case SPINNER:
                return SPINNER_TYPE;
        }
        return 0;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        if (questionList == null) {
            Toast.makeText(context, "No Question Found for Survey", Toast.LENGTH_SHORT).show();
            return convertView;
        }

        Question question = questionList.get(position);

        int type = getItemViewType(position);
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        switch (type) {
            case FILL_TYPE:
                view = convertView;
                FillViewHolder fillViewHolder;
                if (view == null) {
                    fillViewHolder = new FillViewHolder();
                    view = layoutInflater.inflate(R.layout.view_question_text_layout, parent, false);
                    fillViewHolder.questionIdTextView = (TextView) view.findViewById(R.id.text_view_question_id);
                    fillViewHolder.questionQuestionTextView = (TextView) view.findViewById(R.id.text_view_question_question);
                    fillViewHolder.editButton = (ImageButton) view.findViewById(R.id.edit_icon);
                    fillViewHolder.viewButton = (ImageButton) view.findViewById(R.id.view_icon);
                    fillViewHolder.deleteButton = (ImageButton) view.findViewById(R.id.delete_icon);
                    fillViewHolder.questionResponseEditText = (EditText) view.findViewById(R.id.edit_text_question_response);
                    view.setTag(fillViewHolder);
                } else {
                    fillViewHolder = (FillViewHolder) view.getTag();
                }
                fillViewHolder.setValues(question);
                return view;

            case TRUE_FALSE_TYPE:
                view = convertView;

                TrueFalseViewHolder trueFalseViewHolder;
                if (view == null) {
                    trueFalseViewHolder = new TrueFalseViewHolder();
                    view = layoutInflater.inflate(R.layout.view_question_true_false_layout, parent, false);
                    trueFalseViewHolder.questionIdTextView = (TextView) view.findViewById(R.id.text_view_question_id);
                    trueFalseViewHolder.questionQuestionTextView = (TextView) view.findViewById(R.id.text_view_question_question);
                    trueFalseViewHolder.editButton = (ImageButton) view.findViewById(R.id.edit_icon);
                    trueFalseViewHolder.viewButton = (ImageButton) view.findViewById(R.id.view_icon);
                    trueFalseViewHolder.deleteButton = (ImageButton) view.findViewById(R.id.delete_icon);

                    trueFalseViewHolder.trueFalseGroup = (RadioGroup) view.findViewById(R.id.radio_group_true_false_response);
                } else {
                    trueFalseViewHolder = (TrueFalseViewHolder) view.getTag();
                }

                trueFalseViewHolder.setValues(question);

                String[] options = new String[]{"True", "False"};
                RadioButton rb[] = new RadioButton[options.length];
                rb[0] = (RadioButton) view.findViewById(R.id.true_button);
                rb[1] = (RadioButton) view.findViewById(R.id.false_button);

                for (int i = 0; i < options.length; ++i) {
                    rb[i].setText(options[i]);
                }
                return view;

            case RADIO_TYPE:
                view = convertView;
                RadioViewHolder radioViewHolder = new RadioViewHolder();
                if (view == null) {
                    view = layoutInflater.inflate(R.layout.view_question_radio_layout, parent, false);
                    radioViewHolder.questionIdTextView = (TextView) view.findViewById(R.id.text_view_question_id);
                    radioViewHolder.questionQuestionTextView = (TextView) view.findViewById(R.id.text_view_question_question);
                    radioViewHolder.editButton = (ImageButton) view.findViewById(R.id.edit_icon);
                    radioViewHolder.viewButton = (ImageButton) view.findViewById(R.id.view_icon);
                    radioViewHolder.deleteButton = (ImageButton) view.findViewById(R.id.delete_icon);
                    radioViewHolder.radioGroup = (RadioGroup) view.findViewById(R.id.radio_group_question_response);
                } else {
                    radioViewHolder = (RadioViewHolder) view.getTag();
                }

                radioViewHolder.setValues(question);
                String[] radioOptions = question.getOptions().split(",");
                for (int i = 0; i < radioOptions.length; ++i) {
                    RadioButton radioButton = new RadioButton(parent.getContext());
                    radioButton.setText(radioOptions[i]);
                    radioButton.setId(i);
                    radioViewHolder.radioGroup.addView(radioButton, new RadioGroup.LayoutParams(
                            RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT)
                    );
                }

                return view;
            case SPINNER_TYPE:
                view = convertView;
                SpinnerViewHolder spinnerViewHolder;
                if (view == null) {
                    spinnerViewHolder = new SpinnerViewHolder();
                    view = layoutInflater.inflate(R.layout.view_question_spinner_layout, parent, false);
                    spinnerViewHolder.questionIdTextView = (TextView) view.findViewById(R.id.text_view_question_id);
                    spinnerViewHolder.questionQuestionTextView = (TextView) view.findViewById(R.id.text_view_question_question);
                    spinnerViewHolder.editButton = (ImageButton) view.findViewById(R.id.edit_icon);
                    spinnerViewHolder.viewButton = (ImageButton) view.findViewById(R.id.view_icon);
                    spinnerViewHolder.deleteButton = (ImageButton) view.findViewById(R.id.delete_icon);

                    spinnerViewHolder.spinner = (Spinner) view.findViewById(R.id.spinner_question_response);
                } else {
                    spinnerViewHolder = (SpinnerViewHolder) view.getTag();
                }
                spinnerViewHolder.setValues(question);
                String[] spinnerOptions = question.getOptions().split(",");
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(parent.getContext(),
                        android.R.layout.simple_spinner_item, spinnerOptions);
                // set the view for the Drop down list
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // set the ArrayAdapter to the spinner
                spinnerViewHolder.spinner.setAdapter(dataAdapter);
                spinnerViewHolder.spinner.setSelection(0);

                return view;
        }
        return convertView;
    }

    private class ViewHolder {
        TextView questionIdTextView;
        TextView questionQuestionTextView;

        ImageButton viewButton;
        ImageButton editButton;
        ImageButton deleteButton;

        public void findView() {

        }

        public void setValues(Question question1) {
            final Question question = question1;
            questionIdTextView.setText("" + question.getId());
            questionQuestionTextView.setText(question.getQuestion());

            if (user.getUserType() == User.UserType.SURVEYER) {
                editButton.setVisibility(View.VISIBLE);
                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                        Fragment prev = activity.getSupportFragmentManager().findFragmentByTag("UpdateQuestionDialogFragment");
                        if (prev != null) {
                            ft.remove(prev);
                        }
                        ft.addToBackStack(null);

                        DialogFragment surveyFragment = UpdateQuestionFragment.newInstance(question.getId());
                        surveyFragment.show(ft, "UpdateQuestionDialogFragment");
                    }
                });

            } else {
                editButton.setVisibility(View.GONE);
            }

            if (user.getUserType() == User.UserType.SURVEYER) {
                viewButton.setVisibility(View.VISIBLE);
                viewButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (activity instanceof ListQuestionActivity) {
                            ((ListQuestionActivity) activity).startListResponseActivity(question.getId());
                        }
                    }
                });
            } else {
                viewButton.setVisibility(View.GONE);
            }

            if (user.getUserType() == User.UserType.SURVEYER) {
                deleteButton.setVisibility(View.VISIBLE);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DataHelper dataHelper = DataHelper.getInstance(context);
                        dataHelper.deleteQuestion(question.getId());
                        Toast.makeText(context, "Question " + question.getId() + " deleted.", Toast.LENGTH_LONG).show();
                        notifyDataSetChanged();
                    }
                });
            } else {
                deleteButton.setVisibility(View.GONE);
            }

        }
    }

    private class FillViewHolder extends ViewHolder {
        EditText questionResponseEditText;
    }

    private class TrueFalseViewHolder extends ViewHolder {
        RadioGroup trueFalseGroup;
    }

    private class SpinnerViewHolder extends ViewHolder {
        Spinner spinner;
    }

    private class RadioViewHolder extends ViewHolder {
        RadioGroup radioGroup;
    }
}
