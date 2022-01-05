package com.example.aman.hospitalappointy.home;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.aman.hospitalappointy.home.fragments.DateFragment;
import com.example.aman.hospitalappointy.home.fragments.DoctorFragment;
import com.example.aman.hospitalappointy.home.fragments.SpecializationFragment;

/**
 * Created by Aman on 14-Feb-18.
 */

public class SectionPagerAdapter extends FragmentPagerAdapter{
    public SectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                SpecializationFragment specializationFragment = new SpecializationFragment();
                return specializationFragment;

            case 1:
                DoctorFragment doctorFragment = new DoctorFragment();
                return doctorFragment;

            case 2:
                DateFragment dateFragment = new DateFragment();
                return dateFragment;

            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "SPECIALIZATION";
            case 1:
                return "DOCTOR";
            case 2:
                return "DATE";

            default:
                return null;
        }
    }
}
