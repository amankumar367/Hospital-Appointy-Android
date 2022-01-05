package com.example.aman.hospitalappointy.model;

/**
 * Created by Aman on 01-Mar-18.
 */

public class DoctorList{

    private String Name;
    private String Email;
    private String Address;
    private String Education;
    private String Experiance;
    private String Specialization;
    private String Contact;
    private String Shift;


    public DoctorList(){

    }

    public DoctorList(String name, String email, String address, String eduction, String spcialization,String contact, String experiance, String shift) {
        this.Name = name;
        this.Email = email;
        this.Address = address;
        this.Education = eduction;
        this.Specialization = spcialization;
        this.Contact = contact;
        this.Experiance = experiance;
        this.Shift = shift;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        this.Address = address;
    }

    public String getEducation() {
        return Education;
    }

    public void setEducation(String education) {
        this.Education = education;
    }

    public String getSpecialization() {
        return Specialization;
    }

    public void setSpecialization(String specialization) {
        this.Specialization = specialization;
    }


    public String getExperiance() {
        return Experiance;
    }

    public void setExperiance(String experiance) {
        this.Experiance = experiance;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        this.Contact = contact;
    }

    public String getShift() {
        return Shift;
    }

    public void setShift(String shift) {
        this.Shift = shift;
    }
}
