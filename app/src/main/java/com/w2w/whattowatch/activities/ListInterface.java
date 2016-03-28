package com.w2w.whattowatch.activities;

/**
 * Interface for activities that show and modify elements from a database
 */
public interface ListInterface {
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
