package com.example.aman.hospitalappointy.patient;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aman.hospitalappointy.R;
import com.example.aman.hospitalappointy.auth.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

public class BookAppointmentActivity extends AppCompatActivity implements View.OnClickListener{

    private String date, time = "", shift;
    private TextView selectDate;
    private Toolbar mToolbar;
    private Button mConfirm;
    private  int flagChecked=0;

    private LinearLayout morningLayout, eveningLayout;

    private Calendar calendar;
    private DatePickerDialog datePickerDialog;

    private CardView c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13,c14,c15,c16,c17,c18,c19,c20,c21,c22,c23,c24,c25,c26,c27,c28,c29,c30;
    private DatabaseReference mDataBaseRef = FirebaseDatabase.getInstance().getReference().child("Appointment");
    private DatabaseReference mPatientDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.patient_bookAppointment);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Book Appointment");

        morningLayout = (LinearLayout) findViewById(R.id.morning_shift);
        eveningLayout = (LinearLayout) findViewById(R.id.evening_shift);
        shift = getIntent().getStringExtra("Shift");

        if(shift == "Morning"){
            morningLayout.setVisibility(View.VISIBLE);
            eveningLayout.setVisibility(View.GONE);
        }else {
            eveningLayout.setVisibility(View.VISIBLE);
            morningLayout.setVisibility(View.GONE);
        }

        mConfirm = (Button) findViewById(R.id.confirm_appointment);
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(flagChecked!=0)
                {
                    mDataBaseRef.child(getIntent().getStringExtra("DoctorUserId")).child(date).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int i = 1;
                            for(i=1;i<=30;i++)
                            {
                                if(dataSnapshot.hasChild(String.valueOf(i)))
                                {
                                    if(dataSnapshot.child(String.valueOf(i)).child("PatientID").getValue().toString().equals(mAuth.getCurrentUser().getUid()))
                                    {
                                        Toast.makeText(BookAppointmentActivity.this, "You Have Already An Appointment ", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            }
                            if(i>30)
                            {
                                setTime(flagChecked);
                                mDataBaseRef.child(getIntent().getStringExtra("DoctorUserId")).child(date).child(String.valueOf(flagChecked)).child("PatientID").setValue(mAuth.getCurrentUser().getUid().toString());
                                mPatientDatabase.child("Doctor_Details").child(getIntent().getStringExtra("DoctorUserId")).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String doctorName = dataSnapshot.child("Name").getValue().toString();

                                        HashMap<String,String> details = new HashMap<>();
                                        details.put("Doctor_ID",getIntent().getStringExtra("DoctorUserId"));
                                        details.put("Date",date);
                                        details.put("Time",time);

                                        mPatientDatabase.child("Booked_Appointments").child(mAuth.getCurrentUser().getUid()).push().setValue(details);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                                startActivity(new Intent(BookAppointmentActivity.this, PatientViewBookedAppointmentActivity.class));

                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
                else{
                    Toast.makeText(BookAppointmentActivity.this, "Please Select Time Slot", Toast.LENGTH_SHORT).show();
                }
            }
        });

        c1 = (CardView) findViewById(R.id.time1);
        c2 = (CardView) findViewById(R.id.time2);
        c3 = (CardView) findViewById(R.id.time3);
        c4 = (CardView) findViewById(R.id.time4);
        c5 = (CardView) findViewById(R.id.time5);
        c6 = (CardView) findViewById(R.id.time6);
        c7 = (CardView) findViewById(R.id.time7);
        c8 = (CardView) findViewById(R.id.time8);
        c9 = (CardView) findViewById(R.id.time9);
        c10 = (CardView) findViewById(R.id.time10);
        c11 = (CardView) findViewById(R.id.time11);
        c12 = (CardView) findViewById(R.id.time12);
        c13 = (CardView) findViewById(R.id.time13);
        c14 = (CardView) findViewById(R.id.time14);
        c15 = (CardView) findViewById(R.id.time15);
        c16 = (CardView) findViewById(R.id.time16);
        c17 = (CardView) findViewById(R.id.time17);
        c18 = (CardView) findViewById(R.id.time18);
        c19 = (CardView) findViewById(R.id.time19);
        c20 = (CardView) findViewById(R.id.time20);
        c21 = (CardView) findViewById(R.id.time21);
        c22 = (CardView) findViewById(R.id.time22);
        c23 = (CardView) findViewById(R.id.time23);
        c24 = (CardView) findViewById(R.id.time24);
        c25 = (CardView) findViewById(R.id.time25);
        c26 = (CardView) findViewById(R.id.time26);
        c27 = (CardView) findViewById(R.id.time27);
        c28 = (CardView) findViewById(R.id.time28);
        c29 = (CardView) findViewById(R.id.time29);
        c30 = (CardView) findViewById(R.id.time30);


        selectDate = (TextView) findViewById(R.id.bookAppointment_selectDate);

        date = getIntent().getStringExtra("Date").toString();
        selectDate.setText(date);
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(BookAppointmentActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        date = dayOfMonth +"-"+ (month+1) +"-"+ year;
                       // Toast.makeText(Patient_BookAppointmentActivity.this, date , Toast.LENGTH_SHORT).show();
                        selectDate.setText(date);
                        onStart();


                    }
                },day,month,year);
                datePickerDialog.updateDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() + (3 * 60 * 60 * 1000));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + (15 * 24 * 60 * 60 * 1000));
                datePickerDialog.show();
            }
        });




    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.time1:
                checkIsBooked(1);
                break;
            case R.id.time2:
                checkIsBooked(2);
                break;
            case R.id.time3:
                checkIsBooked(3);
                break;
            case R.id.time4:
                checkIsBooked(4);
                break;
            case R.id.time5:
                checkIsBooked(5);
                break;
            case R.id.time6:
                checkIsBooked(6);
                break;
            case R.id.time7:
                checkIsBooked(7);
                break;
            case R.id.time8:
                checkIsBooked(8);
                break;
            case R.id.time9:
                checkIsBooked(9);
                break;
            case R.id.time10:
                checkIsBooked(10);
                break;
            case R.id.time11:
                checkIsBooked(11);
                break;
            case R.id.time12:
                checkIsBooked(12);
                break;
            case R.id.time13:
                checkIsBooked(13);
                break;
            case R.id.time14:
                checkIsBooked(14);
                break;
            case R.id.time15:
                checkIsBooked(15);
                break;
            case R.id.time16:
                checkIsBooked(16);
                break;
            case R.id.time17:
                checkIsBooked(17);
                break;
            case R.id.time18:
                checkIsBooked(18);
                break;
            case R.id.time19:
                checkIsBooked(19);
                break;
            case R.id.time20:
                checkIsBooked(20);
                break;
            case R.id.time21:
                checkIsBooked(21);
                break;
            case R.id.time22:
                checkIsBooked(22);
                break;
            case R.id.time23:
                checkIsBooked(23);
                break;
            case R.id.time24:
                checkIsBooked(24);
                break;
            case R.id.time25:
                checkIsBooked(25);
                break;
            case R.id.time26:
                checkIsBooked(26);
                break;
            case R.id.time27:
                checkIsBooked(27);
                break;
            case R.id.time28:
                checkIsBooked(28);
                break;
            case R.id.time29:
                checkIsBooked(29);
                break;
            case R.id.time30:
                checkIsBooked(30);
                break;
                
            default:
                break;
        }
    }

    private void checkIsBooked(int i) {


        if(flagChecked!=0) {
          setDefaultColor(flagChecked);
          flagChecked = i;
          setColorGreen(i);
        }
        else {
            flagChecked=i;
            setColorGreen(i);
        }


    }

    private void setDefaultColor(int i) {

        switch (i) {
            case 1: c1.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c1.setEnabled(true);
                break;
            case 2:
                c2.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c2.setEnabled(true);
                break;
            case 3:
                c3.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c3.setEnabled(true);
                break;
            case 4:
                c4.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c4.setEnabled(true);
                break;
            case 5:
                c5.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c5.setEnabled(true);
                break;
            case 6:
                c6.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c6.setEnabled(true);
                break;
            case 7:
                c7.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c7.setEnabled(true);
                break;
            case 8:
                c8.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c8.setEnabled(true);
                break;
            case 9:
                c9.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c9.setEnabled(true);
                break;
            case 10:
                c10.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c10.setEnabled(true);
                break;
            case 11:
                c11.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c11.setEnabled(true);
                break;
            case 12:
                c12.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c12.setEnabled(true);
                break;
            case 13:
                c13.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c13.setEnabled(true);
                break;
            case 14:
                c14.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c14.setEnabled(true);
                break;
            case 15:
                c15.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c15.setEnabled(true);
                break;
            case 16:
                c16.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c16.setEnabled(true);
                break;
            case 17:
                c17.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c17.setEnabled(true);
                break;
            case 18:
                c18.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c18.setEnabled(true);
                break;
            case 19:
                c19.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c19.setEnabled(true);
                break;
            case 20:
                c20.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c20.setEnabled(true);
                break;
            case 21:
                c21.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c21.setEnabled(true);
                break;
            case 22:
                c22.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c22.setEnabled(true);
                break;
            case 23:
                c23.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c23.setEnabled(true);
                break;
            case 24:
                c24.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c24.setEnabled(true);
                break;
            case 25:
                c25.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c25.setEnabled(true);
                break;
            case 26:
                c26.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c26.setEnabled(true);
                break;
            case 27:
                c27.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c27.setEnabled(true);
                break;
            case 28:
                c28.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c28.setEnabled(true);
                break;
            case 29:
                c29.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c29.setEnabled(true);
                break;
            case 30:
                c30.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c30.setEnabled(true);
                break;
            default:
                break;
        }
    }

    private void setColorRed(int i) {

        switch (i) {
            case 1: c1.setCardBackgroundColor(Color.RED);
                c1.setEnabled(false);
                break;
            case 2:
                c2.setCardBackgroundColor(Color.RED);
                c2.setEnabled(false);
                break;
            case 3:
                c3.setCardBackgroundColor(Color.RED);
                c3.setEnabled(false);
                break;
            case 4:
                c4.setCardBackgroundColor(Color.RED);
                c4.setEnabled(false);
                break;
            case 5:
                c5.setCardBackgroundColor(Color.RED);
                c5.setEnabled(false);
                break;
            case 6:
                c6.setCardBackgroundColor(Color.RED);
                c6.setEnabled(false);
                break;
            case 7:
                c7.setCardBackgroundColor(Color.RED);
                c7.setEnabled(false);
                break;
            case 8:
                c8.setCardBackgroundColor(Color.RED);
                c8.setEnabled(false);
                break;
            case 9:
                c9.setCardBackgroundColor(Color.RED);
                c9.setEnabled(false);
                break;
            case 10:
                c10.setCardBackgroundColor(Color.RED);
                c10.setEnabled(false);
                break;
            case 11:
                c11.setCardBackgroundColor(Color.RED);
                c11.setEnabled(false);
                break;
            case 12:
                c12.setCardBackgroundColor(Color.RED);
                c12.setEnabled(false);
                break;
            case 13:
                c13.setCardBackgroundColor(Color.RED);
                c13.setEnabled(false);
                break;
            case 14:
                c14.setCardBackgroundColor(Color.RED);
                c14.setEnabled(false);
                break;
            case 15:
                c15.setCardBackgroundColor(Color.RED);
                c15.setEnabled(false);
                break;
            case 16:
                c16.setCardBackgroundColor(Color.RED);
                c16.setEnabled(false);
                break;
            case 17:
                c17.setCardBackgroundColor(Color.RED);
                c17.setEnabled(false);
                break;
            case 18:
                c18.setCardBackgroundColor(Color.RED);
                c18.setEnabled(false);
                break;
            case 19:
                c19.setCardBackgroundColor(Color.RED);
                c19.setEnabled(false);
                break;
            case 20:
                c20.setCardBackgroundColor(Color.RED);
                c20.setEnabled(false);
                break;
            case 21:
                c21.setCardBackgroundColor(Color.RED);
                c21.setEnabled(false);
                break;
            case 22:
                c22.setCardBackgroundColor(Color.RED);
                c22.setEnabled(false);
                break;
            case 23:
                c23.setCardBackgroundColor(Color.RED);
                c23.setEnabled(false);
                break;
            case 24:
                c24.setCardBackgroundColor(Color.RED);
                c24.setEnabled(false);
                break;
            case 25:
                c25.setCardBackgroundColor(Color.RED);
                c25.setEnabled(false);
                break;
            case 26:
                c26.setCardBackgroundColor(Color.RED);
                c26.setEnabled(false);
                break;
            case 27:
                c27.setCardBackgroundColor(Color.RED);
                c27.setEnabled(false);
                break;
            case 28:
                c28.setCardBackgroundColor(Color.RED);
                c28.setEnabled(false);
                break;
            case 29:
                c29.setCardBackgroundColor(Color.RED);
                c29.setEnabled(false);
                break;
            case 30:
                c30.setCardBackgroundColor(Color.RED);
                c30.setEnabled(false);
                break;
            default:
                break;
        }
    }

    private void setColorGreen(int i) {

        switch (i) {
            case 1: c1.setCardBackgroundColor(Color.GREEN);
                break;
            case 2:
                c2.setCardBackgroundColor(Color.GREEN);
                break;
            case 3:
                c3.setCardBackgroundColor(Color.GREEN);
                break;
            case 4:
                c4.setCardBackgroundColor(Color.GREEN);
                break;
            case 5:
                c5.setCardBackgroundColor(Color.GREEN);
                break;
            case 6:
                c6.setCardBackgroundColor(Color.GREEN);
                break;
            case 7:
                c7.setCardBackgroundColor(Color.GREEN);
                break;
            case 8:
                c8.setCardBackgroundColor(Color.GREEN);
                break;
            case 9:
                c9.setCardBackgroundColor(Color.GREEN);
                break;
            case 10:
                c10.setCardBackgroundColor(Color.GREEN);
                break;
            case 11:
                c11.setCardBackgroundColor(Color.GREEN);
                break;
            case 12:
                c12.setCardBackgroundColor(Color.GREEN);
                break;
            case 13:
                c13.setCardBackgroundColor(Color.GREEN);
                break;
            case 14:
                c14.setCardBackgroundColor(Color.GREEN);
                break;
            case 15:
                c15.setCardBackgroundColor(Color.GREEN);
                break;
            case 16:
                c16.setCardBackgroundColor(Color.GREEN);
                break;
            case 17:
                c17.setCardBackgroundColor(Color.GREEN);
                break;
            case 18:
                c18.setCardBackgroundColor(Color.GREEN);
                break;
            case 19:
                c19.setCardBackgroundColor(Color.GREEN);
                break;
            case 20:
                c20.setCardBackgroundColor(Color.GREEN);
                break;
            case 21:
                c21.setCardBackgroundColor(Color.GREEN);
                break;
            case 22:
                c22.setCardBackgroundColor(Color.GREEN);
                break;
            case 23:
                c23.setCardBackgroundColor(Color.GREEN);
                break;
            case 24:
                c24.setCardBackgroundColor(Color.GREEN);
                break;
            case 25:
                c25.setCardBackgroundColor(Color.GREEN);
                break;
            case 26:
                c26.setCardBackgroundColor(Color.GREEN);
                break;
            case 27:
                c27.setCardBackgroundColor(Color.GREEN);
                break;
            case 28:
                c28.setCardBackgroundColor(Color.GREEN);
                break;
            case 29:
                c29.setCardBackgroundColor(Color.GREEN);
                break;
            case 30:
                c30.setCardBackgroundColor(Color.GREEN);
                break;
            default:
                break;
        }
    }

    private void setTime(int i) {

        switch (i) {
            case 1:
                time = "08:00 AM";
                break;
            case 2:
                time = "08:20 AM";
                break;
            case 3:
                time = "08:40 AM";
                break;
            case 4:
                time = "09:00 AM";
                break;
            case 5:
                time = "09:20 AM";
                break;
            case 6:
                time = "09:40 AM";
                break;
            case 7:
                time = "10:00 AM";
                break;
            case 8:
                time = "10:20 AM";
                break;
            case 9:
                time = "10:40 AM";
                break;
            case 10:
                time = "11:00 AM";
                break;
            case 11:
                time = "11:20 AM";
                break;
            case 12:
                time = "11:40 AM";
                break;
            case 13:
                time = "02:00 PM";
                break;
            case 14:
                time = "02:20 PM";
                break;
            case 15:
                time = "02:40 PM";
                break;
            case 16:
                time = "03:00 PM";
                break;
            case 17:
                time = "03:20 PM";
                break;
            case 18:
                time = "03:40 PM";
                break;
            case 19:
                time = "04:00 PM";
                break;
            case 20:
                time = "04:20 PM";
                break;
            case 21:
                time = "04:40 PM";
                break;
            case 22:
                time = "05:00 PM";
                break;
            case 23:
                time = "05:20 PM";
                break;
            case 24:
                time = "05:40 PM";
                break;
            case 25:
                time = "06:00 PM";
                break;
            case 26:
                time = "06:20 PM";
                break;
            case 27:
                time = "06:40 PM";
                break;
            case 28:
                time = "09:00 PM";
                break;
            case 29:
                time = "09:20 PM";
                break;
            case 30:
                time = "09:40 PM";
                break;
            default:
                break;
        }
    }



    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Toast.makeText(this, "You are not Logged In.....Login first for further process", Toast.LENGTH_SHORT).show();

            Intent login_Intent = new Intent(BookAppointmentActivity.this, LoginActivity.class);
            startActivity(login_Intent);
        }else {
            flagChecked=0;
            mDataBaseRef.child(getIntent().getStringExtra("DoctorUserId").toString()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.hasChild(date)){

                        mDataBaseRef.child(getIntent().getStringExtra("DoctorUserId").toString()).child(date).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (int i =1;i<=30;i++) {

                                    if(dataSnapshot.hasChild(String.valueOf(i)))
                                    {
                                        setColorRed(i);

                                    }
                                    else
                                    {
                                        setDefaultColor(i);
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }else {
                        for (int i =1;i<=30;i++)
                        {
                            setDefaultColor(i);
                        }
                       // Toast.makeText(Patient_BookAppointmentActivity.this, "all time is available on this date", Toast.LENGTH_SHORT).show();
                        // mDataBaseRef.child(doctorUserId).child(date).child(slot).child("PatientID").setValue(userId);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }
}
