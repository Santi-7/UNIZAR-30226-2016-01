package com.w2w.whattowatch.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.w2w.whattowatch.R;
import com.w2w.whattowatch.data.DbAdapter;

public class ListSeries extends ListAbstract {

    private ListView mList;       // View that holds all the series in the UI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_series);
        // The toolbar holds the title of the activity and the menu
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

    /////////////////////////////////////// ListAbstract ///////////////////////////////////////

    /**
     * Fetches and shows all series from the database.
     */
    /*protected void list() {
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
    }*/

    /**
     * Fetches and shows all series from the database.
     */
    protected void list() {
        // Get all of the series from the database and create the item list.
        Cursor seriesCursor = mDbAdapter.fetchAllSeries();
        startManagingCursor(seriesCursor);
        CursorAdapterAdapter notes =
                new CursorAdapterAdapter(this, R.layout.series_row, seriesCursor, 0);
        mList.setAdapter(notes);
    }

    /**
     * Starts an activity to create a new series.
     */
    protected void create() {
        Intent i = new Intent(this, EditSeries.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    /**
     * Starts an activity to edit a series.
     *
     * @param elementId id of the series that will be edited.
     */
    protected void edit(long elementId) {
        Intent i = new Intent(this, EditSeries.class);
        i.putExtra(DbAdapter.SERIES_KEY_ID, elementId);
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    /**
     * Deletes the series elementId.
     *
     * @param elementId id of the episode that will be deleted.
     */
    protected void delete(long elementId) {
        // Episodes are refreshed if the current episode has been correctly deleted.
        if (mDbAdapter.deleteSeries(elementId)) {
            list();
        }
    }

    /**
     * Starts a ListEpisodes activity for the series with id seriesId.
     *
     * @param seriesId id of the series which info will be displayed on the to-be-started activity.
     */
    protected void showSeries(long seriesId) {
        Intent intent = new Intent(this, ListEpisodes.class);
        intent.putExtra("sid", seriesId);
        startActivity(intent);
    }

    static class CursorAdapterAdapter extends ResourceCursorAdapter {
        public CursorAdapterAdapter(Context context, int layout, Cursor c, int flags) {
            super(context, layout, c, flags);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView titleView = (TextView) view.findViewById(R.id.series_title);
            String series_title = cursor.getString(cursor.getColumnIndex(DbAdapter.SERIES_KEY_TITLE));
            titleView.setText(series_title);

            ImageView image = (ImageView) view.findViewById(R.id.series_score);
            String score = cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter.SERIES_KEY_SCORE));
            score = score == null ? "0" : score;
            int score_img = 0;
            switch (score) {
                case ("0"):
                    score_img = R.drawable.full_rating;
                    break;
                case ("1"):
                    score_img = R.drawable.full_rating;
                    break;
                case ("2"):
                    score_img = R.drawable.full_rating;
                    break;
                case ("3"):
                    score_img = R.drawable.full_rating;
                    break;
                case ("4"):
                    score_img = R.drawable.full_rating;
                    break;
                case ("5"):
                    score_img = R.drawable.full_rating;
            }
            //int resId=context.getResources().getIdentifier(score_img, "drawable", context.getPackageName());
            image.setImageResource(score_img);
        }
    }
}
