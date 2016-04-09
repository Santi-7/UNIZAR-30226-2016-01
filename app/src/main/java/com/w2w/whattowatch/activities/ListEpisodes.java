package com.w2w.whattowatch.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.w2w.whattowatch.R;
import com.w2w.whattowatch.data.DbAdapter;
import com.w2w.whattowatch.data.Episode;

import java.util.ArrayList;

public class ListEpisodes extends AppCompatActivity implements ListInterface {

    ArrayList<Episode> episodes;
    private DbAdapter mDbAdapter;
    private long seriesId;
    private String seriesTitle;
    private String seriesDescription;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_episodes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try {
            seriesId = (Long) this.getIntent().getExtras().get("sid");
        } catch (NullPointerException e) {
            seriesId = 0;
        }
        Log.d("ListEpisodes: ", "Started for series: " + seriesId);
        mDbAdapter = new DbAdapter(this);
        mDbAdapter.open();
        list();
    }

    /**
     * Fetches and shows all episodes from the database.
     */
    public void list() {
        Cursor series = mDbAdapter.fetchSeries(seriesId);
        startManagingCursor(series);
        seriesTitle = series.getString(series.getColumnIndexOrThrow(DbAdapter.SERIES_KEY_TITLE));
        getSupportActionBar().setTitle(seriesTitle);
        seriesDescription = series.getString(series.getColumnIndexOrThrow(DbAdapter.SERIES_KEY_DESCRIPTION));

        Cursor eCursor = mDbAdapter.getNumberOfSeasons(seriesId);
        int numOfSeasons;
        try {
            eCursor.moveToFirst();
            numOfSeasons = Integer.parseInt(eCursor.getString(0));
        } catch (CursorIndexOutOfBoundsException f) {
            numOfSeasons = 0;
        }
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),
                numOfSeasons + 1, seriesId);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    /**
     * Starts an activity to create a new episode
     */
    public void create() {
        Log.d("ListSeries", "Create new series");
        Intent i = new Intent(this, EditEpisodes.class);
        i.putExtra(DbAdapter.SERIES_KEY_ID, seriesId);
        startActivityForResult(i, 0);
    }

    /**
     * Starts an activity to edit an episode
     *
     * @param elementId id of the episode that will be edited
     */
    public void edit(long elementId) {
        // TODO: Implement edit()
    }

    /**
     * Deletes the episode elementId
     *
     * @param elementId id of the episode that will be deleted
     */
    public void delete(long elementId) {
        // TODO: Implement delete()
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_episodes2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.create_new_episode) {
            create();
            list();
            return true;
        } else {
            return false;
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SEASON_NUMBER = "section_number";
        private static final String ARG_SERIES_ID = "series_id";

        private DbAdapter mDbAdapter;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int seasonNumber, long seriesId) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SEASON_NUMBER, seasonNumber);
            args.putLong(ARG_SERIES_ID, seriesId);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            mDbAdapter = new DbAdapter(this.getActivity());
            mDbAdapter.open();
            View rootView = inflater.inflate(R.layout.fragment_list_episodes, container, false);
            // Get seriesId and fetch episodes for the season.
            Cursor episodes = mDbAdapter.fetchSeason(getArguments().getLong(ARG_SERIES_ID), getArguments().getInt(ARG_SEASON_NUMBER));
            getActivity().startManagingCursor(episodes);
            // Create an array to specify the fields we want to display in the list (only TITLE)
            String[] from = new String[]{DbAdapter.EPISODE_KEY_EPISODE_NUM, DbAdapter.EPISODE_KEY_NAME}; //, DbAdapter.EPISODE_KEY_NAME
            // and an array of the fields we want to bind those fields to (in this case just text1)
            int[] to = new int[]{R.id.episode_number, R.id.episode_name}; // , R.id.episode_title

            // Now create an array adapter and set it to display using our row
            SimpleCursorAdapter adapter =
                    new SimpleCursorAdapter(this.getActivity(), R.layout.episode_row, episodes, from, to, 0);
            ListView episodeList = (ListView) rootView.findViewById(R.id.episode_list);
            episodeList.setAdapter(adapter);
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private int numberOfSeasons;
        private long seriesId;

        public SectionsPagerAdapter(FragmentManager fm, int seasons, long seriesId) {
            super(fm);
            numberOfSeasons = seasons;
            this.seriesId = seriesId;
        }

        @Override
        public Fragment getItem(int seasonNum) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(seasonNum, seriesId);
        }

        @Override
        public int getCount() {
            return numberOfSeasons;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position > 10) return "S" + position;
            else return "S0" + position;
        }
    }
}
