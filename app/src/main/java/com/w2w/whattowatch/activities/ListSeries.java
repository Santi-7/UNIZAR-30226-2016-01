package com.w2w.whattowatch.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.w2w.whattowatch.R;
import com.w2w.whattowatch.data.DbAdapter;

public class ListSeries extends AppCompatActivity implements ListInterface {

    /* Constants to create or edit a series in the activity created. */
    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;

    /* Constants for the context menu*/
    private static final int EDIT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;

    private DbAdapter mDbAdapter; /* Database adapter */
    private ListView mList; /* View that holds all the series in the UI */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_series);
        // This might end up being unnecessary
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

        // Series list management
        mDbAdapter = new DbAdapter(this);
        mDbAdapter.open();
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

    /**
     * Starts a ListEpisodes activity for the series with id seriesId
     * @param seriesId id of the series which info will be displayed on the to-be-started activity.
     */
    private void showSeries(long seriesId) {
        Intent intent = new Intent(this, ListEpisodes.class);
        intent.putExtra("sid", seriesId);
        startActivity(intent);
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
        switch (id) {
            case R.id.create_new_series:
                create();
                list();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Method that creates an options menu when a user clicks and holds on a series
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(Menu.NONE, EDIT_ID, Menu.NONE, R.string.edit_series);
        menu.add(Menu.NONE, DELETE_ID, Menu.NONE, R.string.delete_series);
    }

    /**
     * Method called when a contextmenu option is selected
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                delete(info.id);
                list();
                return true;
            case EDIT_ID:
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                edit(info.id);
                return true;
        }
        list();
        return super.onContextItemSelected(item);
    }

    /**
     * Fetches and shows all series from the database.
     */
    public void list() {
        // Get all of the series from the database and create the item list
        Cursor seriesCursor = mDbAdapter.fetchAllSeries();
        startManagingCursor(seriesCursor);

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{DbAdapter.SERIES_KEY_TITLE};

        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{R.id.text};

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter notes =
                new SimpleCursorAdapter(this, R.layout.series_row, seriesCursor, from, to, 0);
        mList.setAdapter(notes);
    }

    /**
     * Starts an activity to create a new series
     */
    public void create() {
        Log.d("ListSeries", "Create new series");
        Intent i = new Intent(this, EditSeries.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    /**
     * Starts an activity to edit a series
     *
     * @param elementId id of the series that will be edited
     */
    // TODO: This method creates another note instead of editing it
    public void edit(long elementId) {
        Intent i = new Intent(this, EditSeries.class);
        i.putExtra(DbAdapter.SERIES_KEY_ID, elementId);
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    /**
     * Deletes the series elementId
     *
     * @param elementId id of the series that will be deleted
     */
    public void delete(long elementId) {
        // Series are refreshed if the current series has been correctly deleted.
        if (mDbAdapter.deleteSeries(elementId)) {
            list();
        }
    }

    /**
     * Method that runs when an activity returns (for now just list again after editing)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        list();
    }
}
