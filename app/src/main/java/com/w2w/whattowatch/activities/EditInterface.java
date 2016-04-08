package com.w2w.whattowatch.activities;

/**
 * Interface for activities that allow information inputs from the user and later writes them to
 * a database.
 */
public interface EditInterface {

    /**
     * Saves all user inputs to the database
     */
    void saveState();

    /**
     * Fills all user input fields with previously existing information from the database.
     */
    void populateFields();
}
