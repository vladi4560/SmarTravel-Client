package com.vladi.karasove.smartravel.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.vladi.karasove.smartravel.Adapters.QuestionAdapter;
import com.vladi.karasove.smartravel.Helpers.DataManger;
import com.vladi.karasove.smartravel.Objects.Question;
import com.vladi.karasove.smartravel.Objects.Trip;
import com.vladi.karasove.smartravel.R;
import com.vladi.karasove.smartravel.retrofit.Convertor;
import com.vladi.karasove.smartravel.retrofit.UserApi;
import com.vladi.karasove.smartravel.serverObjects.ActivityBoundary;
import com.vladi.karasove.smartravel.serverObjects.InstanceBoundary;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewDialogRating {
    private MaterialTextView dialog_List_LBL_title;
    private RecyclerView dialog_LST_Questions;
    private MaterialButton dialog_BTN_Rate;
    private MaterialButton dialog_BTN_Skip;
    private LinearLayoutManager linearLayoutManager;
    private DataManger dataManger;

    public interface Callback_ViewDialog{
        void timeToRate(Trip trip);
    }

    private void findViews(Dialog dialog) {
        dialog_List_LBL_title = dialog.findViewById(R.id.dialog_List_LBL_title);
        dialog_LST_Questions = dialog.findViewById(R.id.dialog_LST_Questions);
        dialog_BTN_Skip = dialog.findViewById(R.id.dialog_BTN_Skip);
        dialog_BTN_Rate = dialog.findViewById(R.id.dialog_BTN_Rate);
    }

    public void showDialog(Activity activity, Trip trip , ArrayList<Question> list, Callback_ViewDialog callBack_viewDialogItem) {
        dataManger = DataManger.getInstance();
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialogbox_rating);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        findViews(dialog);
        dialog_List_LBL_title.setText("Rating "+trip.getName());
        linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        dialog_LST_Questions.setLayoutManager(linearLayoutManager);
        dialog_LST_Questions.setHasFixedSize(true);
        dialog_LST_Questions.setItemAnimator(new DefaultItemAnimator());

        QuestionAdapter adapter_rate = new QuestionAdapter(activity, list);
        dialog_LST_Questions.setAdapter(adapter_rate);

        adapter_rate.setQuestionClickListener(new QuestionAdapter.QuestionClickListener() {
            @Override
            public void rateClicked(int adapterPosition, Question question, int value) {
                question.setAnswerRate(value);
            }
        });

        dialog_BTN_Skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trip.setIsRate(true);
                dialog.dismiss();
            }
        });

        dialog_BTN_Rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trip.setIsRate(true);
                dialog.dismiss();
                updateUserInstance(trip);
            }
        });

        dialog.show();
    }

    private void updateUserInstance(Trip trip){
        InstanceBoundary instanceBoundary = Convertor.convertTripToInstanceBoundary(trip);
        UserApi userApi= dataManger.getRetrofitService().getRetrofit().create(UserApi.class);
        userApi.updateInstanceById(instanceBoundary,dataManger.getCurrentUser().getDomain(),dataManger.getMyInstances().get("trip"+trip.getId()),DataManger.CLIENT_MANAGER_DOMAIN,DataManger.CLIENT_MANAGER_EMAIL)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        createActivityBoundary(trip.getId());
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                    }
                });
    }

    private void createActivityBoundary(int id) {
        ActivityBoundary activityBoundary = Convertor.convertToActivityBoundary(dataManger.getCurrentUser().getDomain(),dataManger.getMyInstances().get("trip"+id),dataManger.getCurrentUser().getDomain(),dataManger.getCurrentUser().getEmail(),"rateTrip");
        UserApi userApi= dataManger.getRetrofitService().getRetrofit().create(UserApi.class);
        userApi.createActivity(activityBoundary)
                .enqueue(new Callback<ActivityBoundary>() {
                    @Override
                    public void onResponse(Call<ActivityBoundary> call, Response<ActivityBoundary> response) {
                    }

                    @Override
                    public void onFailure(Call<ActivityBoundary> call, Throwable t) {
                    }
                });
    }



}