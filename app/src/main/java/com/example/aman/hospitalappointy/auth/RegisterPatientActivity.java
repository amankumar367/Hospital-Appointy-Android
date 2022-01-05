package com.example.aman.hospitalappointy.auth;

import android.app.AlertDialog;
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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.aman.hospitalappointy.R;
import com.example.aman.hospitalappointy.home.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterPatientActivity extends AppCompatActivity {

    private TextInputLayout mName;
    private TextInputLayout mAge;
    private TextInputLayout mBloodGroup;
    private TextInputLayout mContactNumber;
    private TextInputLayout mAddress;
    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private Button mRegister;

    //RadioGroup & RadioButton
    private RadioGroup mGender;


    //Firebase Auth
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    //Database Reference
    private DatabaseReference mUserDetails = FirebaseDatabase.getInstance().getReference();

    private Toolbar mToolbar;
    private ProgressDialog mRegProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_patient);


        // Toolbar
        mToolbar = (Toolbar) findViewById(R.id.register_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Patient Registration");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRegProgress = new ProgressDialog(this);


        //User Details
        mName = (TextInputLayout) findViewById(R.id.reg_name_layout);
        mAge = (TextInputLayout) findViewById(R.id.reg_age_layout);
        mBloodGroup = (TextInputLayout) findViewById(R.id.reg_bloodgroup_layout);
        mContactNumber = (TextInputLayout) findViewById(R.id.reg_contact_layout);
        mAddress = (TextInputLayout) findViewById(R.id.reg_address_layout);

        mEmail = (TextInputLayout) findViewById(R.id.reg_email_layout);
        mPassword = (TextInputLayout) findViewById(R.id.reg_password_layout);
        mRegister = (Button) findViewById(R.id.reg_button);



        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = mName.getEditText().getText().toString();
                String age = mAge.getEditText().getText().toString();
                String bloodgroup = mBloodGroup.getEditText().getText().toString();
                String contactnumber = mContactNumber.getEditText().getText().toString();
                String address = mAddress.getEditText().getText().toString();
                String email = mEmail.getEditText().getText().toString();
                String password = mPassword.getEditText().getText().toString();
                String gender = "";

                //RadioGroup
                mGender = (RadioGroup) findViewById(R.id.reg_gender_radiogroup);
                int checkedId = mGender.getCheckedRadioButtonId();

                    if(checkedId == R.id.reg_male_radiobtn){
                        gender = "Male";
                    }
                    else if(checkedId == R.id.reg_female_radiobtn){
                        gender = "Female";
                    }
                    else if(checkedId == R.id.reg_other_radiobtn){
                        gender = "Other";
                    }
                    else {
                        Toast.makeText(getBaseContext(),"Select Gender",Toast.LENGTH_LONG).show();
                    }

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(age) && !TextUtils.isEmpty(contactnumber) && !TextUtils.isEmpty(address)){

                    mRegProgress.setTitle("Creating Account");
                    mRegProgress.setMessage("Please Wait! We are Processing");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();


                    createAccount(name,age,gender,bloodgroup,contactnumber,address,email,password);

                }
                else{

                    Toast.makeText(RegisterPatientActivity.this,"Please fill all field",Toast.LENGTH_LONG).show();

                }

            }
        });
    }

    private void createAccount(final String name, final String age,final String gender, final String bloodgroup, final String contactnumber, final String address, final String email, final String password) {

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(RegisterPatientActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = currentUser.getUid();

                            mUserDetails.child("User_Type").child(uid).child("Type").setValue("Patient");

                            HashMap<String,String> userDetails = new HashMap<>();
                            userDetails.put("Name",name);
                            userDetails.put("Age",age);
                            userDetails.put("Gender",gender);
                            userDetails.put("Blood_Group",bloodgroup);
                            userDetails.put("Contact_N0",contactnumber);
                            userDetails.put("Address",address);
                            userDetails.put("Email",email);
                            userDetails.put("Password",password);

                            mUserDetails.child("Patient_Details").child(uid).setValue(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    mRegProgress.dismiss();
                                    Toast.makeText(RegisterPatientActivity.this,"Account Successfully Created",Toast.LENGTH_SHORT).show();

                                    verifyEmail(email);

                                }
                            });


                        }
                        else {

                            mRegProgress.hide();
                            Toast.makeText(RegisterPatientActivity.this,"Creating Account Failed",Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private void verifyEmail(final String email) {

        AlertDialog.Builder mBuiler = new AlertDialog.Builder(RegisterPatientActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.verify_email, null);

        TextView userEmail = (TextView) mView.findViewById(R.id.verify_email);
        final TextView sentVerication = (TextView) mView.findViewById(R.id.verify_email_sent);
        Button verifyEmail = (Button) mView.findViewById(R.id.verify_button);
        Button continuebutton = (Button) mView.findViewById(R.id.verify_continue);

        userEmail.setText(email);

        verifyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            sentVerication.setText("We have sent Email to "+email);

                        }
                        else {
                            sentVerication.setText("Failed to Sent Email for Verification");
                        }
                    }
                });
            }
        });

        continuebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent main_Intent = new Intent(RegisterPatientActivity.this, HomeActivity.class);
                startActivity(main_Intent);
            }
        });


        mBuiler.setView(mView);
        AlertDialog dialog = mBuiler.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


}
