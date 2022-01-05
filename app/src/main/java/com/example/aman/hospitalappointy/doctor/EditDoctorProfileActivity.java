package com.example.aman.hospitalappointy.doctor;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.aman.hospitalappointy.R;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditDoctorProfileActivity extends AppCompatActivity {

    private TextView mName, mEmail, mSpecialization, mExperiance, mAge, mContact, mAddress, mEducation;
    private Toolbar mToolbar;

    private String name,specialization,experiance,education,email,age,contact,address,update;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Doctor_Details");
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_doctor_profile);

        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.doctor_editProfile_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mName = (TextView) findViewById(R.id.edit_doctor_name);
        mSpecialization = (TextView) findViewById(R.id.edit_doctor_specialization);
        mExperiance = (TextView) findViewById(R.id.edit_doctor_experiance);
        mEducation = (TextView) findViewById(R.id.edit_doctor_education);
        mEmail = (TextView) findViewById(R.id.edit_doctor_email);
        mAge = (TextView) findViewById(R.id.edit_doctor_age);
        mContact = (TextView) findViewById(R.id.edit_doctor_contact);
        mAddress = (TextView) findViewById(R.id.edit_doctor_address);

        name = getIntent().getStringExtra("Name").toString();
        specialization = getIntent().getStringExtra("Specialization").toString();
        experiance = getIntent().getStringExtra("Experiance").toString();
        education = getIntent().getStringExtra("Education").toString();
        email = getIntent().getStringExtra("Email").toString();
        age = getIntent().getStringExtra("Age").toString();
        contact = getIntent().getStringExtra("Contact").toString();
        address = getIntent().getStringExtra("Address").toString();

        mName.setText(name);
        mSpecialization.setText(specialization);
        mExperiance.setText(experiance);
        mEducation.setText(education);
        mEmail.setText(email);
        mAge.setText(age);
        mContact.setText(contact);
        mAddress.setText(address);
    }

    public void update(View view){

        switch (view.getId()){

            case R.id.edit_name:
                alertDialog(name,"Name");
                break;

            case R.id.edit_experiance:
                alertDialog(experiance,"Experience");
                break;

            case R.id.edit_education:
                alertDialog(education,"Education");
                break;
            case R.id.edit_address:
                alertDialog(address,"Address");
                break;
            case R.id.edit_age:
                alertDialog(age,"Age");
                break;
            case R.id.edit_contact:
                alertDialog(contact,"Contact");
                break;

            case R.id.final_update:
                updateDoctorProfile();
                break;

            default:
                break;
        }

    }

    private void updateDoctorProfile() {

        String currentUser = mAuth.getCurrentUser().getUid().toString();

       mDatabase.child(currentUser).child("Name").setValue(name);
       mDatabase.child(currentUser).child("Experiance").setValue(experiance);
       mDatabase.child(currentUser).child("Education").setValue(education);
       mDatabase.child(currentUser).child("Address").setValue(address);
       mDatabase.child(currentUser).child("Contact").setValue(contact);
       mDatabase.child(currentUser).child("Age").setValue(age);

       startActivity(new Intent(EditDoctorProfileActivity.this, DoctorProfileActivity.class));



    }

    private void alertDialog(String text, final String detail){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View view = getLayoutInflater().inflate(R.layout.udate_dialog, null);


        TextView textView = (TextView) view.findViewById(R.id.update_textView);
        final EditText editText = (EditText) view.findViewById(R.id.editText);

        textView.setText(detail);
        editText.setText(text, TextView.BufferType.EDITABLE);

        builder.setView(view);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                update = editText.getText().toString();

                if(detail.equals("Name")){
                    mName.setText(update);
                    name = mName.getText().toString();
                }
                else if(detail.equals("Experience")){
                    mExperiance.setText(update);
                    experiance = mExperiance.getText().toString();
                }
                else if(detail.equals("Education")){
                    mEducation.setText(update);
                    education = mEducation.getText().toString();
                }
                else if(detail.equals("Address")){
                    mAddress.setText(update);
                    address = mAddress.getText().toString();
                }
                else if(detail.equals("Age")){
                    mAge.setText(update);
                    age = mAge.getText().toString();
                }
                else if(detail.equals("Contact")){
                    mContact.setText(update);
                    contact = mContact.getText().toString();
                }
                else {

                }

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

}
