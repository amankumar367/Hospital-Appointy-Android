package com.example.aman.hospitalappointy.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.aman.hospitalappointy.R;
import com.example.aman.hospitalappointy.home.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private Button mLogin;
    private Button mForgot;
    private Button mRegister;


    //Firebase Auth
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private ProgressDialog mLoginProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("LOG IN");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLoginProgress = new ProgressDialog(this);

        mEmail = (TextInputLayout) findViewById(R.id.login_email_layout);
        mPassword = (TextInputLayout) findViewById(R.id.login_password_layout);
        mLogin = (Button) findViewById(R.id.login_button);
        mForgot = (Button) findViewById(R.id.login_forgot_button);
        mRegister = (Button) findViewById(R.id.login_register_button);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmail.getEditText().getText().toString();
                String password = mPassword.getEditText().getText().toString();

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

                    mLoginProgress.setTitle("Loggin In");
                    mLoginProgress.setMessage("Please wait! While your Account is Logging In");
                    mLoginProgress.setCanceledOnTouchOutside(false);
                    mLoginProgress.show();

                    login(email,password);
                }
                else {
                    Toast.makeText(LoginActivity.this,"Please Enter Email & Password",Toast.LENGTH_LONG).show();
                }

            }
        });

        mForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotPassword_Intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(forgotPassword_Intent);
            }
        });
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registration_Intent = new Intent(LoginActivity.this, RegisterPatientActivity.class);
                startActivity(registration_Intent);
            }
        });
    }

    private void login(String email, String password) {

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            mLoginProgress.dismiss();
                            Intent main_Intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(main_Intent);
                            //Toast.makeText(LoginActivity.this,"Successfully Logged IN",Toast.LENGTH_LONG).show();
                        }
                        else {

                            mLoginProgress.dismiss();
                            Toast.makeText(LoginActivity.this,"Entered Email & Password is wrong",Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}
