package com.w2w.whattowatch.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.w2w.whattowatch.R;
import com.w2w.whattowatch.data.DbAdapter;

/**
 * Abstract class that implements some methods and defines others to be implemented by
 * the classes ListSeries and ListEpisodes
 */
public abstract class ListAbstract extends AppCompatActivity {

    /* Constants for the context menu. */
    protected static int EDIT_ID = Menu.FIRST;
    protected static int DELETE_ID = Menu.FIRST + 1;

    /* Constants to create or edit an element in the activity created. */
    protected int ACTIVITY_CREATE = 0;
    protected int ACTIVITY_EDIT = 1;

    protected DbAdapter mDbAdapter;       // Database adapter

    /**
     * Fetches and shows the elements corresponding to the class that implements this
     * interface.
     */
    protected abstract void list();

    /**
     * Starts an activity to create an element corresponding to the class that implements
     * this interface.
     */
    protected abstract void create();

    /**
     * Starts an activity to edit an element corresponding to the class that implements
     * this interface.
     * 
     * @param elementId id of the element that will be edited.
     */
    protected abstract void edit(long elementId);

    /**
     * Deletes the episode elementId.
     *
     * @param elementId id of the episode that will be deleted.
     */
    protected abstract void delete(long elementId);


    /**
     * Method that creates an options menu when a user clicks and holds on a series.
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, EDIT_ID, Menu.NONE, R.string.edit_series);
        menu.add(Menu.NONE, DELETE_ID, Menu.NONE, R.string.delete_series);
    }

    /**
     * Method called when a ContextMenu option is selected.
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int i = item.getItemId();
        // User has selected to delete the element.
        if (i == DELETE_ID) {
            AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            delete(info.id);
            return true;
        // User has selected to edit the element.
        } else if (i == EDIT_ID) {
            AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            edit(info.id);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * Method that runs when a child activity ends. It lists again all the elements
     * contained in the activity since they've just been modified by adding or editing one.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // Refresh elements list
        list();
    }

    public DbAdapter getmDbAdapter(){
        return mDbAdapter;
    }

}
