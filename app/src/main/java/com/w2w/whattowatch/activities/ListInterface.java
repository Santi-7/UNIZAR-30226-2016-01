package com.w2w.whattowatch.activities;

import android.view.Menu;

/**
 * Interface for activities that show and modify elements from a database
 */
public interface ListInterface {

    /* Constants to create or edit a episode in the activity created. */
    int ACTIVITY_CREATE = 0;
    int ACTIVITY_EDIT = 1;

    /* Constants for the context menu*/
    int EDIT_ID = Menu.FIRST;
    int DELETE_ID = Menu.FIRST + 1;

    /**
     * Fetches and shows the elements corresponding to the class that implements this
     * interface.
     */
    void list();

    /**
     * Starts an activity to create an element corresponding to the class that implements this
     * interface.
     */
    void create();

    /**
     * Starts an activity to edit an element corresponding to the class that implements this
     * interface.
     * @param elementId id of the element that will be edited
     */
    void edit(long elementId);

    /**
     * Deletes an element corresponding to the class that implements this
     * interface.
     * @param elementId id of the element that will be deleted
     */
    void delete(long elementId);
}
