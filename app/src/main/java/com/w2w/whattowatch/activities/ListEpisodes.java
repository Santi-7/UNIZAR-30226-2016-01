package com.w2w.whattowatch.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.w2w.whattowatch.R;
import com.w2w.whattowatch.data.DbAdapter;

import java.util.ArrayList;

public class ListEpisodes extends ListAbstract {

    private long seriesId;              // Identifier of current Series
    private String seriesTitle;         // Title of current Series

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a  {@link FragmentPagerAdapter}
     * derivative, which will keep every loaded fragment in memory.
     * If this becomes too memory intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SeasonPagerAdapter mSectionsPagerAdapter;

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
        mDbAdapter = new DbAdapter(this);
        mDbAdapter.open();
        list();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_episodes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.create_new_episode:
                create();
                return true;
            case R.id.edit_series:
                editSeries();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewPager.getAdapter().notifyDataSetChanged();
    }

    /////////////////////////////////////// ListAbstract ///////////////////////////////////////

    /**
     * Fetches and shows all episodes from the database.
     */
    protected void list() {
        Cursor series = mDbAdapter.fetchSeries(seriesId);
        startManagingCursor(series);
        seriesTitle = series.getString(series.getColumnIndexOrThrow(DbAdapter.SERIES_KEY_TITLE));
        getSupportActionBar().setTitle(seriesTitle);

        Cursor eCursor = mDbAdapter.getSeasons(seriesId);
        ArrayList<Integer> seasons = new ArrayList<>();
        eCursor.moveToFirst();
        for (int i = 0; i < eCursor.getCount(); i++) {
            seasons.add(eCursor.getInt(eCursor.getColumnIndex(DbAdapter.EPISODE_KEY_SEASON_NUM)));
            eCursor.moveToNext();
        }
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SeasonPagerAdapter(getSupportFragmentManager(),
                seasons, seriesId);
        mSectionsPagerAdapter.notifyDataSetChanged();
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    /**
     * Starts an activity to create a new episode.
     */
    protected void create() {
        Intent i = new Intent(this, EditEpisodes.class);
        i.putExtra(DbAdapter.EPISODE_KEY_ID, Long.valueOf(0));
        i.putExtra(DbAdapter.EPISODE_KEY_SERIES, seriesId);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    /**
     * Starts an activity to edit an episode.
     *
     * @param elementId id of the episode that will be edited.
     */
    protected void edit(long elementId) {
        Intent i = new Intent(this, EditEpisodes.class);
        i.putExtra(DbAdapter.EPISODE_KEY_SERIES, seriesId);
        i.putExtra(DbAdapter.EPISODE_KEY_ID, elementId);
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    /**
     * Deletes the episode elementId.
     *
     * @param elementId id of the episode that will be deleted.
     */
    protected void delete(long elementId) {
        // Episodes are refreshed if the current episode has been correctly deleted.
        if (mDbAdapter.deleteEpisode(elementId)) {
            list();
        }
    }

    /**
     * Starts an activity to edit the current series (with identifier [seriesId]).
     */
    protected void editSeries() {
        Intent i = new Intent(this, EditSeries.class);
        i.putExtra(DbAdapter.SERIES_KEY_ID, seriesId);
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Fragment containing all episodes that correspond to the same season of a given
     * series.
     */
    public static class SeasonFragment extends Fragment {

        /**
         * These arguments can only be passed via bundle. They match to season number and series Id.
         */
        private static final String ARG_TAB_NUMBER = "section_number";
        private static final String ARG_SERIES_ID = "series_id";
        private static final String ARG_SEASONS_ARRAY = "seasons_array";

        private ArrayList<Integer> seasons;
        private int season = -1;
        private int tab = -1;

        public SeasonFragment() { }

        /**
         * Returns a new instance of this fragment for the given season.
         */
        protected static SeasonFragment newInstance(ArrayList<Integer> seasons, int tabNumber,
                                                 long seriesId) {
            SeasonFragment fragment = new SeasonFragment();
            Bundle args = new Bundle();
            args.putIntegerArrayList(ARG_SEASONS_ARRAY, seasons);
            args.putInt(ARG_TAB_NUMBER, tabNumber);
            args.putLong(ARG_SERIES_ID, seriesId);
            fragment.setArguments(args);
            return fragment;
        }

        /**
         * Fetches and shows all episodes on this fragment
         *
         * @param inflater           to instantiate the season view
         * @param container          to match the tabs (internal to android)
         * @param savedInstanceState argument container, since this class' constructor can't have
         *                           parameters
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            DbAdapter mDbAdapter = new DbAdapter(this.getActivity());
            mDbAdapter.open();
            tab = getArguments().getInt(ARG_TAB_NUMBER);
            long series = getArguments().getLong(ARG_SERIES_ID);
            if (tab == 0) {
                View rootView = inflater.inflate(R.layout.fragment_description, container, false);
                Cursor descriptionCursor = mDbAdapter.fetchSeries(series);
                getActivity().startManagingCursor(descriptionCursor);
                String description = descriptionCursor.getString(
                        descriptionCursor.getColumnIndexOrThrow(DbAdapter.SERIES_KEY_DESCRIPTION));
                ((TextView) rootView.findViewById(R.id.description)).setText(description);
                return rootView;
            } else {
                seasons = getArguments().getIntegerArrayList(ARG_SEASONS_ARRAY);
                season = seasons.get(tab - 1);
                View rootView = inflater.inflate(R.layout.fragment_list_episodes, container, false);
                // Get seriesId and fetch episodes for the season.
                Cursor episodes = mDbAdapter.fetchSeason(getArguments().getLong(ARG_SERIES_ID),
                        season);
                getActivity().startManagingCursor(episodes);
                // Create an array to specify the fields we want to display in the list
                // (the episode number, and if exists, also the name).
                String[] from = new String[]
                        {DbAdapter.EPISODE_KEY_EPISODE_NUM, DbAdapter.EPISODE_KEY_NAME};
                // and an array of the fields we want to bind those fields to.
                int[] to = new int[]{R.id.episode_number, R.id.episode_name};

                // Now create an array adapter and set it to display using our row.
                SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                        this.getActivity(), R.layout.episode_row, episodes, from, to, 0);
                ListView episodeList = (ListView) rootView.findViewById(R.id.episode_list);
                episodeList.setAdapter(adapter);
                registerForContextMenu(episodeList);
                return rootView;
            }
        }

        /**
         * Method that creates an options menu when a user clicks and holds on a series.
         */
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v,
                                        ContextMenu.ContextMenuInfo menuInfo) {
            super.onCreateContextMenu(menu, v, menuInfo);
            menu.clear();
            menu.add(Menu.NONE, EDIT_ID, Menu.NONE, R.string.edit_episode);
            menu.add(Menu.NONE, DELETE_ID, Menu.NONE, R.string.delete_episode);
        }

        /**
         * Method called when a ContextMenu option is selected.
         */
        @Override
        public boolean onContextItemSelected(MenuItem item) {
            int i = item.getItemId();
            if (i == DELETE_ID) {
                AdapterView.AdapterContextMenuInfo info =
                        (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                ((ListAbstract) getActivity()).delete(info.id);
                return true;
            } else if (i == EDIT_ID) {
                AdapterView.AdapterContextMenuInfo info =
                        (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                ((ListAbstract) getActivity()).edit(info.id);
                return true;
            }

            return super.onContextItemSelected(item);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the seasons.
     */
    protected class SeasonPagerAdapter extends FragmentStatePagerAdapter {

        private ArrayList<Integer> seasons; // Holds all the seasons for the series
        private long seriesId;              // Id of the series

        public SeasonPagerAdapter(FragmentManager fm, ArrayList<Integer> seasons, long seriesId) {
            super(fm);
            this.seasons = seasons;
            this.seriesId = seriesId;
        }

        @Override
        public Fragment getItem(int seasonNum) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return SeasonFragment.newInstance(seasons, seasonNum, seriesId);
        }

        /**
         * Forces all fragments to reload on update
         */
        @Override
        public int getItemPosition(Object object) {
            // There are more efficient implementations.
            return POSITION_NONE;
        }

        /**
         * @return amount of tabs in the view.
         */
        public int getCount() {
            // Since the description is in the first tab the count is one more
            // than the number of seasons
            return seasons.size() + 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // The first tab holds the description
            if (position == 0) return "Description";
            else {
                // The title contains the number of the season for the tab
                int season = seasons.get(position - 1);
                if (season >= 10) return "S" + season;
                else return "S0" + season;
            }
        }
    }
}
