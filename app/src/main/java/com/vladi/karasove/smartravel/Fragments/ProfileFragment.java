package com.vladi.karasove.smartravel.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.vladi.karasove.smartravel.Activities.ProfileEditActivity;
import com.vladi.karasove.smartravel.Helpers.DataManger;
import com.vladi.karasove.smartravel.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private AppCompatActivity activity;
    private CircleImageView Profile_IMG_User;
    private MaterialTextView Profile_LBL_UserName;
    private MaterialTextView Profile_LBL_UserFirstName;
    private MaterialTextView Profile_LBL_UserLastName;
    private MaterialTextView Profile_LBL_UserEmail;
    private MaterialButton Profile_BTN_EditProfile;

    private DataManger dataManger = DataManger.getInstance();

    public Fragment setActivity(AppCompatActivity activity){
        this.activity=activity;
        return this;
    }

    public ProfileFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_profile, container, false);
        findViews(view);
        initButtons();
        return view;
    }

    private void findViews(View view) {
        Profile_IMG_User=view.findViewById(R.id.Profile_IMG_User);
        Profile_LBL_UserName=view.findViewById(R.id.Profile_LBL_UserName);
        Profile_LBL_UserFirstName=view.findViewById(R.id.Profile_LBL_UserFirstName);
        Profile_LBL_UserLastName=view.findViewById(R.id.Profile_LBL_UserLastName);
        Profile_LBL_UserEmail=view.findViewById(R.id.Profile_LBL_UserEmail);
        Profile_BTN_EditProfile=view.findViewById(R.id.Profile_BTN_EditProfile);
    }

    private void loadUserDetails() {
        if(!dataManger.getCurrentUser().getAvatar().equals("empty")){
            Glide.with(this).load(dataManger.getCurrentUser().getAvatar()).into(Profile_IMG_User);
        }
        Profile_LBL_UserName.setText(dataManger.getCurrentUser().getFirstName()+dataManger.getCurrentUser().getLastName());
        Profile_LBL_UserFirstName.setText(dataManger.getCurrentUser().getFirstName());
        Profile_LBL_UserLastName.setText(dataManger.getCurrentUser().getLastName());
        Profile_LBL_UserEmail.setText(dataManger.getCurrentUser().getEmail());
    }


    private void initButtons() {
        Profile_BTN_EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceActivity(ProfileEditActivity.class);
            }
        });
    }


    private void replaceActivity(Class activity) {
        Intent intent = new Intent(getActivity(), activity);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        loadUserDetails();
    }
}