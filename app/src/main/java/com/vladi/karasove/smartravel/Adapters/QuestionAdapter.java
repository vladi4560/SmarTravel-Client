package com.vladi.karasove.smartravel.Adapters;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.slider.Slider;
import com.google.android.material.textview.MaterialTextView;
import com.vladi.karasove.smartravel.Objects.Question;
import com.vladi.karasove.smartravel.R;

import java.util.ArrayList;

public class QuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private ArrayList<Question> rates;
    private QuestionClickListener questionClickListener;

    public interface QuestionClickListener {
        void rateClicked(int adapterPosition, Question question, int value);
    }

    public QuestionAdapter(Activity activity, ArrayList<Question> rates) {
        this.activity = activity;
        this.rates = rates;
    }

    public QuestionAdapter setQuestionClickListener(QuestionClickListener questionClickListener) {
        this.questionClickListener = questionClickListener;
        return this;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_question, viewGroup, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        QuestionViewHolder questionViewHolder = (QuestionViewHolder) holder;
        Question question = getQuestion(position);

        questionViewHolder.question_LBL_qaNumber.setText(question.getQuestionNumber()+". ");
        questionViewHolder.question_LBL_qa.setText(question.getQuestionRate());

        if (question.getAnswerRate()!= -1)
            questionViewHolder.question_discreteSliderRate.setValue(question.getAnswerRate());

    }

    @Override
    public int getItemCount() {
        if (rates == null) return 0;
        return rates.size();
    }

    private Question getQuestion(int position) {
        return rates.get(position);
    }

    public class QuestionViewHolder extends RecyclerView.ViewHolder {

        public MaterialTextView question_LBL_qaNumber;
        public MaterialTextView question_LBL_qa;
        public Slider question_discreteSliderRate;

        public QuestionViewHolder(final View questionView) {
            super(questionView);
            this.question_LBL_qaNumber = itemView.findViewById(R.id.question_LBL_qaNumber);
            this.question_LBL_qa = itemView.findViewById(R.id.question_LBL_qa);
            this.question_discreteSliderRate = itemView.findViewById(R.id.question_discreteSliderRate);


            question_discreteSliderRate.addOnChangeListener(new Slider.OnChangeListener() {
                @SuppressLint("RestrictedApi")
                @Override
                public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                    slider.setValue(value);
                    int temp = ((int) value);
                    questionClickListener.rateClicked(getAdapterPosition(),getQuestion(getAdapterPosition()),temp);
                }
            });

        }
    }

}