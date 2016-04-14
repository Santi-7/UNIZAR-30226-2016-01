package com.w2w.whattowatch.activities;

import android.support.v7.app.AppCompatActivity;

abstract class EditAbstract extends AppCompatActivity {

    /**
     * Saves all user inputs to the database
     */
    protected abstract void saveState();

    /**
     * Fills all user input fields with previously existing information from the database.
     */
    protected abstract void populateFields();

    /**
     * Saves state when programs is paused
     */
    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    /**
     * Restores state
     */
    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }
}
