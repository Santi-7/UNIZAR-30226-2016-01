package com.w2w.whattowatch.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.w2w.whattowatch.R;
import com.w2w.whattowatch.data.DbAdapter;

public class ListSeries extends AppCompatActivity implements ListInterface {

    /* Constants to create or edit a series in the activity created. */
    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;

    private DbAdapter mDbHelper; /* Adapter to database */
    private ListView mList; /* List with all the series shown */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_series);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_series, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Fetches and shows all series from the database.
     */
    public void list() {
        // Get all of the series from the database and create the item list
        Cursor seriesCursor = mDbHelper.fetchAllSeries();
        startManagingCursor(seriesCursor);

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[] { DbAdapter.SERIES_KEY_TITLE };

        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[] { R.id.text1 };

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter notes =
                new SimpleCursorAdapter(this, R.layout.notes_row, seriesCursor, from, to);
        mList.setAdapter(notes);
    }

    /**
     * Starts an activity to create a new series
     */
    public void create() {
        Intent i = new Intent(this, EditSeries.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    /**
     * Starts an activity to edit a series
     * @param elementId id of the series that will be edited
     */
    public void edit(long elementId) {
        Intent i = new Intent(this, EditSeries.class);
        i.putExtra(DbAdapter.SERIES_KEY_ID, elementId);
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    /**
     * Deletes the series elementId
     * @param elementId id of the series that will be deleted
     */
    public void delete(long elementId) {
        // Series are refreshed if the current series has been correctly deleted.
        if (mDbHelper.deleteSeries(elementId)) {
            list();
        }
    }

}
