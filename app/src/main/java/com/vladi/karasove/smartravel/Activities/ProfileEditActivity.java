package com.vladi.karasove.smartravel.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.vladi.karasove.smartravel.Helpers.DataManger;
import com.vladi.karasove.smartravel.Helpers.Validator;
import com.vladi.karasove.smartravel.R;
import com.vladi.karasove.smartravel.retrofit.Convertor;
import com.vladi.karasove.smartravel.retrofit.UserApi;
import com.vladi.karasove.smartravel.serverObjects.ActivityBoundary;
import com.vladi.karasove.smartravel.serverObjects.InstanceBoundary;
import com.vladi.karasove.smartravel.serverObjects.UserBoundary;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileEditActivity extends AppCompatActivity {

    private MaterialButton EditProfile_BTN_Update;
    private MaterialToolbar EditProfile_toolBar;
    private FloatingActionButton EditProfile_FAB_edit_user_IMG;
    private CircleImageView EditProfile_IMG_User;
    private TextInputLayout EditProfile_LBL_FirstName;
    private TextInputEditText EditProfile_LBL_EditFirstName;
    private TextInputLayout EditProfile_LBL_LastName;
    private TextInputEditText EditProfile_LBL_EditLastName;

    private Validator validatorFirstName;
    private Validator validatorLastName;

    private DataManger dataManger = DataManger.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        findViews();
        initEditFields();
        initValidator();
    }

    private void loadUserDetails() {
        if(!dataManger.getCurrentUser().getAvatar().equals("empty")){
            Glide.with(this).load(dataManger.getCurrentUser().getAvatar()).into(EditProfile_IMG_User);
        }
        EditProfile_LBL_EditFirstName.setText(dataManger.getCurrentUser().getFirstName());
        EditProfile_LBL_EditLastName.setText(dataManger.getCurrentUser().getLastName());
    }

    private void findViews() {
        EditProfile_toolBar = findViewById(R.id.EditProfile_toolBar);
        EditProfile_FAB_edit_user_IMG = findViewById(R.id.EditProfile_FAB_edit_user_IMG);
        EditProfile_IMG_User = findViewById(R.id.EditProfile_IMG_User);
        EditProfile_LBL_FirstName = findViewById(R.id.EditProfile_LBL_FirstName);
        EditProfile_LBL_EditFirstName = findViewById(R.id.EditProfile_LBL_EditFirstName);
        EditProfile_LBL_LastName = findViewById(R.id.EditProfile_LBL_LastName);
        EditProfile_LBL_EditLastName = findViewById(R.id.EditProfile_LBL_EditLastName);
        EditProfile_BTN_Update = findViewById(R.id.EditProfile_BTN_Update);
    }

    private void initValidator() {
        validatorFirstName= Validator.Builder.make(EditProfile_LBL_FirstName)
                .addWatcher(new Validator.Watcher_StringEmpty("Name Cannot Be Empty"))
                .addWatcher(new Validator.Watcher_String("Name Contains Only Characters"))
                .build();

        validatorLastName=Validator.Builder.make(EditProfile_LBL_LastName)
                .addWatcher(new Validator.Watcher_StringEmpty("Name Cannot Be Empty"))
                .addWatcher(new Validator.Watcher_String("Name Contains Only Characters"))
                .build();
    }

    private void initEditFields() {
        EditProfile_toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        EditProfile_IMG_User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choseCover();
            }
        });

        EditProfile_FAB_edit_user_IMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choseCover();
            }
        });


        EditProfile_BTN_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserDetails();
                updateUserBoundaryDetails();
                finish();
            }
        });
    }

    private void updateUserDetails() {
        if(dataManger.getResultUri()!=null){
            dataManger.getCurrentUser().setAvatar(dataManger.getResultUri().toString());
        }
        dataManger.getCurrentUser().setFirstName(EditProfile_LBL_FirstName.getEditText().getText().toString());
        dataManger.getCurrentUser().setLastName(EditProfile_LBL_LastName.getEditText().getText().toString());
    }


    private void choseCover() {
        ImagePicker.with(ProfileEditActivity.this)
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .crop(1f, 1f)
                .maxResultSize(1080, 1080)
                //Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

    /**
     *Results From ImagePicker will be catch here
     * will place the image in the relevant Image View
     * Right after that, will catch the image bytes back from the view and store them in the Firebase Storage.
     * After successful upload will update the Object Url Field
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        dataManger.setResultUri(data.getData());
        dataManger.getCurrentUser().setAvatar(dataManger.getResultUri().toString());
        EditProfile_IMG_User.setImageURI(dataManger.getResultUri());
    }


    private void updateUserBoundaryDetails(){
        UserBoundary userboundary = Convertor.convertUserToUserBoundary(dataManger.getCurrentUser());
        UserApi userApi= dataManger.getRetrofitService().getRetrofit().create(UserApi.class);
        userApi.updateUserDetails(userboundary,dataManger.getCurrentUser().getDomain(),dataManger.getCurrentUser().getEmail())
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        updateUserInstance();
                        createActivityBoundary();
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                    }
                });
    }

    private void updateUserInstance(){
        InstanceBoundary instanceBoundary = Convertor.convertUserToInstanceBoundary(dataManger.getCurrentUser());
        UserApi userApi= dataManger.getRetrofitService().getRetrofit().create(UserApi.class);
        userApi.updateInstanceById(instanceBoundary,dataManger.getCurrentUser().getDomain(),dataManger.getMyInstances().get("user"),DataManger.CLIENT_MANAGER_DOMAIN,DataManger.CLIENT_MANAGER_EMAIL)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                    }
                });
    }

    private void createActivityBoundary() {
        ActivityBoundary activityBoundary = Convertor.convertToActivityBoundary(dataManger.getCurrentUser().getDomain(),dataManger.getMyInstances().get("user"),dataManger.getCurrentUser().getDomain(),dataManger.getCurrentUser().getEmail(),"editProfile");
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

    @Override
    protected void onStart() {
        super.onStart();
        loadUserDetails();
    }
}