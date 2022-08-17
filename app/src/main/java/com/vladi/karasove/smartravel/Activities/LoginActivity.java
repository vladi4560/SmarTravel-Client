package com.vladi.karasove.smartravel.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.vladi.karasove.smartravel.Helpers.DataManger;
import com.vladi.karasove.smartravel.Objects.Trip;
import com.vladi.karasove.smartravel.R;
import com.vladi.karasove.smartravel.retrofit.Convertor;
import com.vladi.karasove.smartravel.retrofit.UserApi;
import com.vladi.karasove.smartravel.serverObjects.InstanceBoundary;
import com.vladi.karasove.smartravel.serverObjects.UserBoundary;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount account;
    private DataManger dataManger;
    private SignInButton signInButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        dataManger = DataManger.getInstance();
        dataManger.setGso(gso);

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        dataManger.setmGoogleSignInClient(mGoogleSignInClient);
        findViews();
        initButton();
    }

    private void findViews() {
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
    }

    private void initButton() {
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                someActivityResultLauncher.launch(signInIntent);
            }
        });

    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        // The Task returned from this call is always completed, no need to attach
                        // a listener.
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        handleSignInResult(task);
                    }
                }
            });

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            account = task.getResult(ApiException.class);
            signIn();

            // Signed in successfully, show authenticated UI.
            //updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d("TAG", "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }


    private void signIn() {
        putAccountIntoUser();
        CreateUser();
        replaceActivity(MainActivity.class);
    }

    private void putAccountIntoUser() {
        if(account.getPhotoUrl()==null){
            dataManger.getCurrentUser()
                    .setFirstName(account.getGivenName())
                    .setLastName(account.getFamilyName())
                    .setAvatar("empty")
                    .setEmail(account.getEmail());
        }else{
            dataManger.getCurrentUser()
                    .setFirstName(account.getGivenName())
                    .setLastName(account.getFamilyName())
                    .setAvatar(account.getPhotoUrl().toString())
                    .setEmail(account.getEmail());
        }
    }

    private void CreateUser() {
        UserBoundary userboundary = Convertor.convertUserToUserBoundary(dataManger.getCurrentUser());
        UserApi userApi= dataManger.getRetrofitService().getRetrofit().create(UserApi.class);
        userApi.createUser(userboundary)
                .enqueue(new Callback<UserBoundary>() {
                    @Override
                    public void onResponse(Call<UserBoundary> call, Response<UserBoundary> response) {
                        try {
                            dataManger.getCurrentUser().setDomain(response.body().getUserId().getDomain());
                            createInstanceUser();
                        }catch (Exception e){
                            Log.d("error",e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<UserBoundary> call, Throwable t) {
                    }
                });

    }

    private void createInstanceUser() {
        InstanceBoundary instanceBoundary = Convertor.convertUserToInstanceBoundary(dataManger.getCurrentUser());
        UserApi userApi= dataManger.getRetrofitService().getRetrofit().create(UserApi.class);
        userApi.createInstance(instanceBoundary)
                .enqueue(new Callback<InstanceBoundary>() {
                    @Override
                    public void onResponse(Call<InstanceBoundary> call, Response<InstanceBoundary> response) {
                        try {
                            dataManger.getMyInstances().put("user",response.body().getInstanceId().getId());
                        }catch (Exception e){
                            Log.d("error",e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<InstanceBoundary> call, Throwable t) {
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        account = GoogleSignIn.getLastSignedInAccount(this);
        if(account!=null){
            dataManger.setAccount(account);
            putAccountIntoUser();
            getUser();
            replaceActivity(MainActivity.class);
        }
    }

    private void getUser() {
        UserApi userApi= dataManger.getRetrofitService().getRetrofit().create(UserApi.class);
        userApi.getUserById("2022b.maya.gembom", dataManger.getCurrentUser().getEmail())
                .enqueue(new Callback<UserBoundary>() {
                    @Override
                    public void onResponse(Call<UserBoundary> call, Response<UserBoundary> response) {
                        try {
                            dataManger.getCurrentUser().setDomain(response.body().getUserId().getDomain());
                            dataManger.getCurrentUser().setEmail(response.body().getUserId().getEmail());
                            dataManger.getCurrentUser().setAvatar(response.body().getAvatar());
                            getInstanceByUser();
                        }catch (Exception e){
                            Log.d("error",e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<UserBoundary> call, Throwable t) {

                    }
                });
    }

    private void getInstanceByUser(){
        UserApi userApi= dataManger.getRetrofitService().getRetrofit().create(UserApi.class);
        userApi.getAllInstancesByName(dataManger.getCurrentUser().getEmail(),dataManger.getCurrentUser().getDomain(),dataManger.getCurrentUser().getEmail(),100,0)
                .enqueue(new Callback<InstanceBoundary[]>() {
                    @Override
                    public void onResponse(Call<InstanceBoundary[]> call, Response<InstanceBoundary[]> response) {
                        InstanceBoundary[] instanceBoundaries =  response.body();
                        for (InstanceBoundary instanceBoundary:instanceBoundaries) {
                            if(instanceBoundary.getType().equals("user")){
                                dataManger.getMyInstances().put("user",instanceBoundary.getInstanceId().getId());
                                dataManger.getCurrentUser().setFirstName((String)instanceBoundary.getInstanceAttributes().get("firstName"));
                                dataManger.getCurrentUser().setLastName((String)instanceBoundary.getInstanceAttributes().get("lastName"));
                            }else if(instanceBoundary.getType().equals("trip")){
                                try {
                                    Gson g = new Gson();
                                    Trip trip = g.fromJson(g.toJsonTree(instanceBoundary.getInstanceAttributes().get("trip")),Trip.class);
                                    dataManger.getMyInstances().put("trip"+trip.getId(),instanceBoundary.getInstanceId().getId());
                                    dataManger.getUpcomingTripList().add(trip);
                                }catch (Exception e){
                                    Log.d("error",e.getMessage());
                                }
                            }
                        }
                        initTrips();
                        dataManger.getCallBackCreateTrip().createTrip();
                    }

                    @Override
                    public void onFailure(Call<InstanceBoundary[]> call, Throwable t) {
                    }
                });
    }

    private void initTrips() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, -1);
        date = c.getTime();
        for(int i=dataManger.getUpcomingTripList().size()-1;i>=0;i--){
            try {
                Date tripEndDate = formatter.parse(dataManger.getUpcomingTripList().get(i).getEndDate());
                if(date.after(tripEndDate)){
                    dataManger.getHistoryTripList().add(dataManger.getUpcomingTripList().get(i));
                    dataManger.getUpcomingTripList().remove(dataManger.getUpcomingTripList().get(i));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void replaceActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

}