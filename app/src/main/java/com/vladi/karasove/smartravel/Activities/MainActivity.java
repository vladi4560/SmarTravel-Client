package com.vladi.karasove.smartravel.Activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textview.MaterialTextView;
import com.vladi.karasove.smartravel.Fragments.DayTripFragment;
import com.vladi.karasove.smartravel.Fragments.DaysPathTripFragment;
import com.vladi.karasove.smartravel.Fragments.HistoryFragment;
import com.vladi.karasove.smartravel.Fragments.MyPlacesFragment;
import com.vladi.karasove.smartravel.Fragments.ProfileFragment;
import com.vladi.karasove.smartravel.Fragments.UpcomingFragment;
import com.vladi.karasove.smartravel.Helpers.DataManger;
import com.vladi.karasove.smartravel.R;
import com.vladi.karasove.smartravel.retrofit.Convertor;
import com.vladi.karasove.smartravel.retrofit.UserApi;
import com.vladi.karasove.smartravel.serverObjects.ActivityBoundary;
import com.vladi.karasove.smartravel.serverObjects.UserBoundary;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout main_drawerLayout;
    private MaterialToolbar main_Toolbar_Top;
    private FragmentContainerView main_fragment;
    private BottomNavigationView main_bottomNavigationView;
    private BottomAppBar main_bottomAppBar;
    private FloatingActionButton main_fab;
    private NavigationView main_TopNavigationView;

    private View header;
    private CircleImageView header_IMG_user;
    private CircularProgressIndicator header_BAR_progress;
    private FloatingActionButton navigation_header_container_FAB_profile_pic;
    private MaterialTextView header_TXT_username;

    public static final int UPCOMING = 0,HISTORY = 1, MY_PLACES = 2, DAYS_PATH_TRIP = 3, DAY_TRIP = 4, PROFILE=5;
    private final int SIZE=7;
    private Fragment[] main_fragments;
    private FragmentManager fragmentManager;
    private DataManger dataManger;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataManger = DataManger.getInstance();
        findViews();
        setSupportActionBar(main_bottomAppBar);
        setSupportActionBar(main_Toolbar_Top);
        initColorMenu();
        setFragments();
        initButtons();
        replaceFragments(main_fragments[UPCOMING]);
    }

    private void findViews() {
        main_drawerLayout=findViewById(R.id.main_drawerlayout);
        main_Toolbar_Top=findViewById(R.id.main_Toolbar_Top);
        main_fragment=findViewById(R.id.main_fragment);
        main_bottomNavigationView=findViewById(R.id.main_bottomNavigationView);
        main_bottomNavigationView.setBackground(null);
        main_bottomAppBar=findViewById(R.id.main_bottomAppBar);
        main_fab=findViewById(R.id.main_fab);
        main_TopNavigationView=findViewById(R.id.main_TopNavigationView);

        header = main_TopNavigationView.getHeaderView(0);
        header_IMG_user=header.findViewById(R.id.header_IMG_user);
        header_BAR_progress=header.findViewById(R.id.header_BAR_progress);
        navigation_header_container_FAB_profile_pic=header.findViewById(R.id.navigation_header_container_FAB_profile_pic);
        header_TXT_username=header.findViewById(R.id.header_TXT_username);
    }

    private void loadUserDetails() {
        if(!dataManger.getCurrentUser().getAvatar().equals("empty")){
            Glide.with(this).load(dataManger.getCurrentUser().getAvatar()).into(header_IMG_user);
        }
        header_TXT_username.setText(dataManger.getCurrentUser().getFirstName()+dataManger.getCurrentUser().getLastName());
    }


    private void initButtons() {
        main_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceActivity(CreateNewTripActivity.class);
            }
        });

        main_Toolbar_Top.setNavigationOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                main_drawerLayout.openDrawer(main_drawerLayout.getForegroundGravity());
            }
        });

        navigation_header_container_FAB_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choseCover();

            }
        });

        header_IMG_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choseCover();
            }
        });

        main_bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                switch (item.getItemId()){
                    case R.id.menu_upcoming:
                        main_Toolbar_Top.setTitle(R.string.Upcoming);
                        replaceFragments(main_fragments[UPCOMING]);
                        break;
                    case R.id.menu_history:
                        main_Toolbar_Top.setTitle(R.string.History);
                        replaceFragments(main_fragments[HISTORY]);
                        break;
                    case R.id.menu_myPlaces:
                        main_Toolbar_Top.setTitle(R.string.MyPlaces);
                        replaceFragments(main_fragments[MY_PLACES]);
                        break;
                    case R.id.menu_profile:
                        main_Toolbar_Top.setTitle(R.string.Profile);
                        replaceFragments(main_fragments[PROFILE]);
                        break;
                }
                return true;
            }
        });

        main_TopNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                switch (item.getItemId()) {
                    case R.id.menu_upcoming:
                        main_Toolbar_Top.setTitle(R.string.Upcoming);
                        replaceFragments(main_fragments[UPCOMING]);
                        break;
                    case R.id.menu_history:
                        main_Toolbar_Top.setTitle(R.string.History);
                        replaceFragments(main_fragments[HISTORY]);
                        break;
                    case R.id.menu_myPlaces:
                        main_Toolbar_Top.setTitle(R.string.MyPlaces);
                        replaceFragments(main_fragments[MY_PLACES]);
                        break;
                    case R.id.menu_profile:
                        main_Toolbar_Top.setTitle(R.string.Profile);
                        replaceFragments(main_fragments[PROFILE]);
                        break;
                    case R.id.menu_logout:
                        DataManger.getInstance().getmGoogleSignInClient().signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                replaceActivity(LoginActivity.class);
                                finish();
                            }
                        });
                        break;
                }
                return true;
            }
        });
    }

    private void choseCover() {
        ImagePicker.with(MainActivity.this)
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
        header_IMG_user.setImageURI(dataManger.getResultUri());
        updateUserBoundaryDetails();
    }


    private void updateUserBoundaryDetails(){
        UserBoundary userboundary = Convertor.convertUserToUserBoundary(dataManger.getCurrentUser());
        UserApi userApi= dataManger.getRetrofitService().getRetrofit().create(UserApi.class);
        userApi.updateUserDetails(userboundary,dataManger.getCurrentUser().getDomain(),dataManger.getCurrentUser().getEmail())
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        createActivityBoundary();
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

    private void initColorMenu() {
        int navDefaultTextColor = Color.parseColor("#FFFFFFFF");
        int navDefaultIconColor = Color.parseColor("#FFFFFFFF");

        //Defining ColorStateList for menu item Text
        ColorStateList navMenuTextList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{android.R.attr.state_enabled},
                        new int[]{android.R.attr.state_pressed},
                        new int[]{android.R.attr.state_focused},
                        new int[]{android.R.attr.state_pressed}
                },
                new int []{
                        Color.parseColor("#4B9226"),
                        navDefaultTextColor,
                        navDefaultTextColor,
                        navDefaultTextColor,
                        navDefaultTextColor
                }
        );

        //Defining ColorStateList for menu item Icon
        ColorStateList navMenuIconList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{android.R.attr.state_enabled},
                        new int[]{android.R.attr.state_pressed},
                        new int[]{android.R.attr.state_focused},
                        new int[]{android.R.attr.state_pressed}
                },
                new int[] {
                        Color.parseColor("#4B9226"),
                        navDefaultIconColor,
                        navDefaultIconColor,
                        navDefaultIconColor,
                        navDefaultIconColor
                }
        );

        main_TopNavigationView.setItemTextColor(navMenuTextList);
        main_TopNavigationView.setItemIconTintList(navMenuIconList);
    }

    private void setFragments() {
        main_fragments = new Fragment[SIZE];
        main_fragments[UPCOMING] = new UpcomingFragment().setActivity(this);
        main_fragments[HISTORY] = new HistoryFragment().setActivity(this);
        main_fragments[MY_PLACES] = new MyPlacesFragment().setActivity(this);
        main_fragments[DAYS_PATH_TRIP] = new DaysPathTripFragment().setActivity(this);
        main_fragments[DAY_TRIP] = new DayTripFragment().setActivity(this);
        main_fragments[PROFILE] = new ProfileFragment().setActivity(this);
    }

    private void replaceFragments(Fragment fragment){
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_fragment, fragment, null).commit();
    }

    private void replaceActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadUserDetails();
    }
}