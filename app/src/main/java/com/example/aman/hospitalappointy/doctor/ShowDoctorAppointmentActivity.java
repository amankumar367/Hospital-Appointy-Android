package com.example.aman.hospitalappointy.doctor;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.aman.hospitalappointy.R;
import com.example.aman.hospitalappointy.model.BookedAppointmentList;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class ShowDoctorAppointmentActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView recyclerView;
    private String userID, date = "", time, name, type;
    private TextView selectDate, selectedDate;
    private int count = 0;

    private Calendar calendar;
    private DatePickerDialog datePickerDialog;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_doctor_appointment);

        mToolbar = (Toolbar) findViewById(R.id.doctor_appointment_layout);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("My Appointment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userID = mAuth.getCurrentUser().getUid().toString();

        selectDate = (TextView) findViewById(R.id.showAppointment_selecteDate);
        selectedDate = (TextView) findViewById(R.id.showAppointment_selectedDate);

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(ShowDoctorAppointmentActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date = dayOfMonth +"-"+ (month+1) +"-"+ year;
                        selectedDate.setVisibility(View.VISIBLE);
                        selectDate.setText(date);

                        showAppointment();

                    }
                },day,month,year);
                datePickerDialog.updateDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() + (3 * 60 * 60 * 1000));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + (15 * 24 * 60 * 60 * 1000));
                datePickerDialog.show();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.appointments_show);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void showAppointment() {
        Query query = mDatabase.child("Appointment").child(userID).child(date);

        FirebaseRecyclerOptions<BookedAppointmentList> firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<BookedAppointmentList>()
                .setQuery(query, BookedAppointmentList.class)
                .build();

        FirebaseRecyclerAdapter<BookedAppointmentList, DoctorShowAppointmentVH> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<BookedAppointmentList, DoctorShowAppointmentVH>(firebaseRecyclerOptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull final DoctorShowAppointmentVH holder, final int position, @NonNull final BookedAppointmentList model) {

                        final String patientID = model.getPatientID().toString();
                        final String slot = getRef(position).getKey().toString();
                        final String[] name = new String[1];

                        holder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                Toast.makeText(Doctor_ShowAppointmentActivity.this, slot, Toast.LENGTH_SHORT).show();
                                alertDialog(patientID, slot);
                            }
                        });

                        mDatabase.child("User_Type").child(patientID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                type = dataSnapshot.child("Type").getValue().toString();
                                if(type.equals("Patient")){
                                    mDatabase.child("Patient_Details").child(patientID).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            name[0] = dataSnapshot.child("Name").getValue().toString();

                                            changeSlotToTime(slot);
                                            holder.setName(name[0]);
                                            holder.setTime(time);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }else {
                                    mDatabase.child("Doctor_Details").child(patientID).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            name[0] = dataSnapshot.child("Name").getValue().toString();

                                            changeSlotToTime(slot);
                                            holder.setName(name[0]);
                                            holder.setTime(time);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public DoctorShowAppointmentVH onCreateViewHolder(ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.single_show_appointment,parent,false);
                        return new DoctorShowAppointmentVH(view);
                    }
                };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    private void alertDialog(final String patientID, final String slot) {

        count = 0;
        AlertDialog.Builder builder = new AlertDialog.Builder(ShowDoctorAppointmentActivity.this);
        builder.setIcon(R.drawable.question).setTitle("Cancel Appointment");
        builder.setMessage("Are You Sure! Want to Cancel Appointment");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

//                    Toast.makeText(Doctor_ShowAppointmentActivity.this, userID+" = UserID "+date+" = Date "+slot+" = Slot", Toast.LENGTH_SHORT).show();


                Query query = mDatabase.child("Booked_Appointments").child(patientID).orderByChild("Date");
                query.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        String myParentNode = dataSnapshot.getKey();

                        for (DataSnapshot child: dataSnapshot.getChildren())
                        {
                            String key = child.getKey().toString();
                            String value = child.getValue().toString();

                            if(value.equals(userID)){
//                                    Toast.makeText(Doctor_ShowAppointmentActivity.this, key+" - "+value, Toast.LENGTH_SHORT).show();
                                count = count + 1;

                            }
                            if(value.equals(date)){
//                                    Toast.makeText(Doctor_ShowAppointmentActivity.this, key+" - "+value, Toast.LENGTH_SHORT).show();
                                count = count + 1;

                            }
                        }
                        if(count == 2){
//                                Toast.makeText(Doctor_ShowAppointmentActivity.this, Integer.toString(count), Toast.LENGTH_SHORT).show();
                            mDatabase.child("Appointment").child(userID).child(date).child(slot).removeValue();
                            mDatabase.child("Booked_Appointments").child(patientID).child(myParentNode).removeValue();

                        }

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void changeSlotToTime(String slot) {

        switch (slot) {
            case "1":
                time = "08:00 AM";
                break;
            case "2":
                time = "08:20 AM";
                break;
            case "3":
                time = "08:40 AM";
                break;
            case "4":
                time = "09:00 AM";
                break;
            case "5":
                time = "09:20 AM";
                break;
            case "6":
                time = "09:40 AM";
                break;
            case "7":
                time = "10:00 AM";
                break;
            case "8":
                time = "10:20 AM";
                break;
            case "9":
                time = "10:40 AM";
                break;
            case "10":
                time = "11:00 AM";
                break;
            case "11":
                time = "11:20 AM";
                break;
            case "12":
                time = "11:40 AM";
                break;
            case "13":
                time = "02:00 PM";
                break;
            case "14":
                time = "02:20 PM";
                break;
            case "15":
                time = "02:40 PM";
                break;
            case "16":
                time = "03:00 PM";
                break;
            case "17":
                time = "03:20 PM";
                break;
            case "18":
                time = "03:40 PM";
                break;
            case "19":
                time = "04:00 PM";
                break;
            case "20":
                time = "04:20 PM";
                break;
            case "21":
                time = "04:40 PM";
                break;
            case "22":
                time = "05:00 PM";
                break;
            case "23":
                time = "05:20 PM";
                break;
            case "24":
                time = "05:40 PM";
                break;
            case "25":
                time = "06:00 PM";
                break;
            case "26":
                time = "06:20 PM";
                break;
            case "27":
                time = "06:40 PM";
                break;
            case "28":
                time = "09:00 PM";
                break;
            case "29":
                time = "09:20 PM";
                break;
            case "30":
                time = "09:40 PM";
                break;
            default:
                break;
        }
    }

    public class DoctorShowAppointmentVH extends RecyclerView.ViewHolder{

        View mView;

        public DoctorShowAppointmentVH(View itemView) {
            super(itemView);

            mView =itemView;
        }

        public void setTime(String time) {
            TextView textView = (TextView) mView.findViewById(R.id.single_patient_time);
            textView.setText(time);
        }

        public void setName(String name) {
            TextView mName = (TextView) mView.findViewById(R.id.single_patientName);
            mName.setText(name);
        }
    }

}
