package com.example.aman.hospitalappointy.model;

import com.google.firebase.database.Query;

/**
 * Created by Aman on 07-Apr-18.
 */

public class BookedAppointmentList {

    private String Date;
    private String Time;
    private String Doctor_ID;
    private String PatientID;

    public BookedAppointmentList() {
    }

    public BookedAppointmentList(String date, String time, String doctor_ID, String patientID) {
        this.Date = date;
        this.Time = time;
        this.Doctor_ID = doctor_ID;
        this.PatientID = patientID;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        this.Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        this.Time = time;
    }

    public String getDoctor_ID() {
        return Doctor_ID;
    }

    public void setDoctor_ID(String doctor_ID) {
        this.Doctor_ID = doctor_ID;
    }

    public String getPatientID() {
        return PatientID;
    }

    public void setPatientID(String patientID) {
        this.PatientID = patientID;
    }
}

