package com.w2w.whattowatch.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.w2w.whattowatch.data.DbAdapter;

abstract class EditAbstract extends AppCompatActivity {

    /**
     * Saves all user inputs to the database
     */
    abstract void saveState();

    /**
     * Fills all user input fields with previously existing information from the database.
     */
    abstract void populateFields();


    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }
}
