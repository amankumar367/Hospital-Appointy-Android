package com.example.aman.hospitalappointy.home;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aman.hospitalappointy.SplashActivity;
import com.example.aman.hospitalappointy.about.AboutActivity;
import com.example.aman.hospitalappointy.doctor.DoctorProfileActivity;
import com.example.aman.hospitalappointy.doctor.ShowDoctorAppointmentActivity;
import com.example.aman.hospitalappointy.feedback.FeedbackActivity;
import com.example.aman.hospitalappointy.auth.LoginActivity;
import com.example.aman.hospitalappointy.patient.PatientViewBookedAppointmentActivity;
import com.example.aman.hospitalappointy.R;
import com.example.aman.hospitalappointy.utils.KeyboardUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavigationView;

    private String Type = "", status = "";

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private SectionPagerAdapter mSectionPagerAdapter;

    //Firebase Auth
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mUserDatabase = FirebaseDatabase.getInstance().getReference();


    public static void launch(Activity context) {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Hospital Appointy");

        //DrawerLayout and ToggleButton
        mDrawerLayout = findViewById(R.id.main_drawerLayout);
        mToggle = new ActionBarDrawerToggle(HomeActivity.this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //NavigationView
        mNavigationView = findViewById(R.id.main_nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        //TabLayout , SectionPagerAdapter & ViewPager
        mViewPager = findViewById(R.id.main_ViewPager);
        mSectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                hideKeyboard();
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {
                hideKeyboard();
            }
        });

        mTabLayout = findViewById(R.id.main_tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);

    }


    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        Menu menuNav = mNavigationView.getMenu();
        final MenuItem nav_profile = menuNav.findItem(R.id.nav_profile);
        final MenuItem nav_ShowAppointment = menuNav.findItem(R.id.nav_showAppointment);
        final MenuItem nav_BookedAppointment = menuNav.findItem(R.id.nav_bookedAppointment);
        final MenuItem nav_feedback = menuNav.findItem(R.id.nav_feedback);
        MenuItem nav_logOut = menuNav.findItem(R.id.nav_logout);
        MenuItem nav_logIn = menuNav.findItem(R.id.nav_login);

        nav_profile.setVisible(false);
        nav_ShowAppointment.setVisible(false);
        nav_BookedAppointment.setVisible(false);
        nav_logIn.setVisible(false);
        nav_logOut.setVisible(false);
        nav_feedback.setVisible(false);


        // Check if user is signed in  or not
        if (currentUser == null) {
            nav_logIn.setVisible(true);

            View mView = mNavigationView.getHeaderView(0);
            TextView userName = (TextView) mView.findViewById(R.id.header_userName);
            TextView userEmail = (TextView) mView.findViewById(R.id.header_userEmail);

            userName.setText("User Name");
            userEmail.setText("User Email");

            Toast.makeText(getBaseContext(), "Your Account is not Logged In ", Toast.LENGTH_LONG).show();
        } else {
            nav_logOut.setVisible(true);
            chechType();
        }
    }

    private void chechType() {

        Menu menuNav = mNavigationView.getMenu();
        final MenuItem nav_profile = menuNav.findItem(R.id.nav_profile);
        final MenuItem nav_ShowAppointment = menuNav.findItem(R.id.nav_showAppointment);
        final MenuItem nav_BookedAppointment = menuNav.findItem(R.id.nav_bookedAppointment);
        final MenuItem nav_feedback = menuNav.findItem(R.id.nav_feedback);

        nav_profile.setVisible(false);
        nav_ShowAppointment.setVisible(false);
        nav_BookedAppointment.setVisible(false);
        nav_feedback.setVisible(false);

        final String uid = mAuth.getUid().toString();

        mUserDatabase.child("User_Type").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Type = (String) dataSnapshot.child("Type").getValue();
                status = (String) dataSnapshot.child("Status").getValue();

                if (Type.equals("Patient")) {
                    nav_BookedAppointment.setVisible(true);
                    nav_feedback.setVisible(true);


                    mUserDatabase.child("Patient_Details").child(uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String name = dataSnapshot.child("Name").getValue().toString();
                            String email = dataSnapshot.child("Email").getValue().toString();

                            View mView = mNavigationView.getHeaderView(0);
                            TextView userName = (TextView) mView.findViewById(R.id.header_userName);
                            TextView userEmail = (TextView) mView.findViewById(R.id.header_userEmail);

                            userName.setText(name);
                            userEmail.setText(email);

                            Toast.makeText(HomeActivity.this, "Your Are Logged In", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else if (Type.equals("Doctor") && status.equals("Approved")) {
                    nav_profile.setVisible(true);
                    nav_ShowAppointment.setVisible(true);
                    nav_feedback.setVisible(true);
                    nav_BookedAppointment.setVisible(true);

                    mUserDatabase.child("Doctor_Details").child(uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String name = dataSnapshot.child("Name").getValue().toString();
                            String email = dataSnapshot.child("Email").getValue().toString();

                            View mView = mNavigationView.getHeaderView(0);
                            TextView userName = (TextView) mView.findViewById(R.id.header_userName);
                            TextView userEmail = (TextView) mView.findViewById(R.id.header_userEmail);

                            userName.setText(name);
                            userEmail.setText(email);

                            Toast.makeText(HomeActivity.this, "Your Are Logged In", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    Toast.makeText(HomeActivity.this, "You are not authorized for this facility or Account Under Pending", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                    onStart();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, databaseError.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_profile:
                launchScreen(DoctorProfileActivity.class);
                break;

            case R.id.nav_showAppointment:
                launchScreen(ShowDoctorAppointmentActivity.class);
                break;

            case R.id.nav_bookedAppointment:
                launchScreen(PatientViewBookedAppointmentActivity.class);
                break;

            case R.id.nav_login:
                launchScreen(LoginActivity.class);
                break;

            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getBaseContext(), "Successfully Logged Out", Toast.LENGTH_LONG).show();
                onStart();
                break;

            case R.id.nav_feedback:
                startActivity(new Intent(HomeActivity.this, FeedbackActivity.class));
                break;

            case R.id.nav_aboutapp:
                startActivity(new Intent(HomeActivity.this, AboutActivity.class));
                break;
            default:
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void launchScreen(Class<?> activity) {
        hideKeyboard();
        Intent intent = new Intent(HomeActivity.this, activity);
        startActivity(intent);
    }

    private void hideKeyboard() {
        KeyboardUtils.hideKeyboard(HomeActivity.this);
    }
}
