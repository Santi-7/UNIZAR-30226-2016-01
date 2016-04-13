package com.w2w.whattowatch.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.w2w.whattowatch.R;
import com.w2w.whattowatch.data.DbAdapter;

public class ListSeries extends ListAbstract {

    private ListView mList;       // View that holds all the series in the UI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_series);
        // This might end up being unnecessary
        // TODO: Check if we can delete it.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Series list management
        mDbAdapter = new DbAdapter(this);
        mDbAdapter.open();
        // Show all the series
        mList = (ListView) findViewById(R.id.series_list);
        mList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        showSeries(id);
                    }
                });
        list();
        registerForContextMenu(mList);
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
        switch (item.getItemId()) {
            case R.id.create_new_series:
                create();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    /////////////////////////////////////// ListInterface ///////////////////////////////////////

    /**
     * Fetches and shows all series from the database.
     */
    public void list() {
        // Get all of the series from the database and create the item list.
        Cursor seriesCursor = mDbAdapter.fetchAllSeries();
        startManagingCursor(seriesCursor);
        // Create an array to specify the fields we want to display in the list (only TITLE).
        String[] from = new String[]{DbAdapter.SERIES_KEY_TITLE};
        // and an array of the fields we want to bind those fields to (series_title).
        int[] to = new int[]{R.id.series_title};
        // Now create an array adapter and set it to display using our row.
        SimpleCursorAdapter notes =
                new SimpleCursorAdapter(this, R.layout.series_row, seriesCursor, from, to, 0);
        mList.setAdapter(notes);
    }

    /**
     * Starts an activity to create a new series.
     */
    public void create() {
        Log.d("ListSeries", "Create new series");
        Intent i = new Intent(this, EditSeries.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    /**
     * Starts an activity to edit a series.
     *
     * @param elementId id of the series that will be edited.
     */
    public void edit(long elementId) {
        Intent i = new Intent(this, EditSeries.class);
        i.putExtra(DbAdapter.SERIES_KEY_ID, elementId);
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    /**
     * Deletes the series elementId.
     *
     * @param elementId id of the episode that will be deleted.
     */
    public void delete(long elementId) {
        // Episodes are refreshed if the current episode has been correctly deleted.
        if (mDbAdapter.deleteSeries(elementId)) {
            list();
        }
    }

    /**
     * Starts a ListEpisodes activity for the series with id seriesId.
     * @param seriesId id of the series which info will be displayed on the to-be-started activity.
     */
    private void showSeries(long seriesId) {
        Intent intent = new Intent(this, ListEpisodes.class);
        intent.putExtra("sid", seriesId);
        startActivity(intent);
    }
}
