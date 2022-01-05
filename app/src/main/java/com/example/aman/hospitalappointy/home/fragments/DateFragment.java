package com.example.aman.hospitalappointy.home.fragments;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aman.hospitalappointy.model.DoctorList;
import com.example.aman.hospitalappointy.patient.PatientViewDoctorProfileActivity;
import com.example.aman.hospitalappointy.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

/**
 * Created by Aman on 14-Feb-18.
 */

public class DateFragment extends Fragment {

    private TextView mSelectDate, mSelectedDate, mAvailableDate;
    private RecyclerView recyclerView;

    private int count = 0;

    private Calendar calendar;
    private DatePickerDialog datePickerDialog;

    private String currentUserID = "";

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public DateFragment(){
        //Required Empty public constructor otherwise app will crash
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_date,container,false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.date_doctorList_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));


        mSelectDate = (TextView) rootView.findViewById(R.id.date_select_date);
        mSelectedDate = (TextView) rootView.findViewById(R.id.date_selected_date);
        mAvailableDate = (TextView) rootView.findViewById(R.id.date_avilableDate);
        mSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(rootView.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        String date = dayOfMonth +"-"+ (month+1) +"-"+ year;
                        Toast.makeText(rootView.getContext(), date , Toast.LENGTH_SHORT).show();
                        mSelectedDate.setVisibility(View.VISIBLE);
                        mSelectDate.setText(date);
                        mAvailableDate.setVisibility(View.VISIBLE);

                        showDoctorList(date);

                    }
                },day,month,year);
                datePickerDialog.updateDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() + (3 * 60 * 60 * 1000));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + (15 * 24 * 60 * 60 * 1000));
                datePickerDialog.show();
            }
        });

        return rootView;
    }

    private void showDoctorList(final String date) {

        Query countQuery = mDatabase.child("Appointment");
        countQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final String doctorID = dataSnapshot.getKey().toString();
                mDatabase.child("Appointment").child(doctorID).child(date).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        count = (int) dataSnapshot.getChildrenCount();
                        checkAvailabilityOfDoctor(count, doctorID);
//                        Toast.makeText(getContext(), count+"", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
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

    private void checkAvailabilityOfDoctor(final int count, final String doctorID) {

//        Toast.makeText(getContext(),doctorID+" - UID + Count -"+count, Toast.LENGTH_SHORT).show();
        Query query = mDatabase.child("Doctor_Details").orderByChild("Name");


        FirebaseRecyclerOptions<DoctorList> firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<DoctorList>()
                .setQuery(query, DoctorList.class)
                .build();

        FirebaseRecyclerAdapter<DoctorList,DoctorLisetVH> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<DoctorList, DoctorLisetVH>(firebaseRecyclerOptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull final DoctorLisetVH holder, int position, @NonNull final DoctorList model) {

                        holder.setDoctorName(model.getName());
                        holder.setSpecialization(model.getSpecialization());
                        holder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String name = model.getName().toString();
                                String specialization = model.getSpecialization().toString();
                                String contact = model.getContact().toString();
                                String experience = model.getExperiance().toString();
                                String education = model.getEducation().toString();
                                String shift = model.getShift().toString();

                                Intent doctorProfile_Intent = new Intent(getContext(), PatientViewDoctorProfileActivity.class);
                                doctorProfile_Intent.putExtra("Name",name);
                                doctorProfile_Intent.putExtra("Specialization",specialization);
                                doctorProfile_Intent.putExtra("Contact",contact);
                                doctorProfile_Intent.putExtra("Experiance",experience);
                                doctorProfile_Intent.putExtra("Education",education);
                                doctorProfile_Intent.putExtra("Shift",shift);
                                doctorProfile_Intent.putExtra("UserId",doctorID);
                                startActivity(doctorProfile_Intent);
                            }
                        });
                    }

                    @Override
                    public DoctorLisetVH onCreateViewHolder(ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_doctor_list,parent,false);

                        return new DoctorLisetVH(view);
                    }
                };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    public class DoctorLisetVH extends RecyclerView.ViewHolder {

        View mView;
       public DoctorLisetVH(View itemView) {
           super(itemView);

           mView = itemView;
       }

       public void setDoctorName(String doctorName) {
           TextView userName = (TextView) mView.findViewById(R.id.name_id_single_user);
           userName.setText(doctorName);
       }

        public void setSpecialization(String specialization) {
            TextView userName = (TextView) mView.findViewById(R.id.special_id_single_user);
            userName.setText(specialization);
        }
    }

}
