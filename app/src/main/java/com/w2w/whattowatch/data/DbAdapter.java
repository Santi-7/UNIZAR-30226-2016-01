package com.w2w.whattowatch.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Database adapter. Contains methods for adding, modifying and removing series and episodes from
 * the database.
 */
public class DbAdapter {

    /* Database constants */
    public static final String SERIES_KEY_ID = "_id"; // A series' unique id field
    public static final String SERIES_KEY_TITLE = "title"; // Series title field
    public static final String SERIES_KEY_DESCRIPTION = "description"; // Series description field
    public static final String SERIES_KEY_RATING = "score"; // Series score field

    public static final String EPISODE_KEY_ID = "_id"; // An episode's unique id field
    public static final String EPISODE_KEY_NAME = "name"; // Episode name field
    public static final String EPISODE_KEY_SEASON_NUM = "season"; // Season field
    public static final String EPISODE_KEY_EPISODE_NUM = "number"; // Episode number field
    public static final String EPISODE_KEY_WATCHED = "watched"; // Episode watched field
    public static final String EPISODE_KEY_SERIES = "series"; // Episode series field

    private static final String DATABASE_NAME = "seriesDB"; // Database name
    private static final String DATABASE_SERIES_TABLE = "series"; // Name of the series table
    private static final String DATABASE_EPISODES_TABLE = "episodes"; // Name of the episode table
    private static final int DATABASE_VERSION = 2; /* Database version, if greater than the
        oldest the database will be updated */

    /* SQL statement for creating the series table*/
    private static final String CREATE_SERIES_TABLE =
            "create table " + DATABASE_SERIES_TABLE +
            " (" + SERIES_KEY_ID + " integer primary key autoincrement, " +
                   SERIES_KEY_TITLE + " text not null, " +
                    SERIES_KEY_RATING + " integer, " +
                   SERIES_KEY_DESCRIPTION + " text not null);";

    /* SQL statement for creating the episode table*/
    private static final String CREATE_EPISODE_TABLE =
            "create table " + DATABASE_EPISODES_TABLE +
            " (" + EPISODE_KEY_ID + " integer primary key autoincrement, " +
                   EPISODE_KEY_NAME + " text not null, " +
                   EPISODE_KEY_SEASON_NUM + " integer, " +
                   EPISODE_KEY_EPISODE_NUM + " integer, " +
                   EPISODE_KEY_WATCHED + " boolean, " +
                   EPISODE_KEY_SERIES + " integer references " +
                   DATABASE_SERIES_TABLE + "(" + SERIES_KEY_ID + ") on delete cascade);";

    /* Adapter private variables */
    private Context ctx;
    private SQLiteDatabase sDb;
    private SeriesDatabaseHelper sDbHelper;

    /**
     * Constructor - The context is needed to retrieve/create the database
     * @param ctx Application context from which to retrieve/create the database
     */
    public DbAdapter(Context ctx) {
        this.ctx = ctx;
    }

    /**
     * Retrieves the series database. If it's never been created it creates it.
     * If it can't be created an exception will be thrown.
     *
     * @return this DbAdapter
     */
    public DbAdapter open() {
        sDbHelper = new SeriesDatabaseHelper(ctx);
        sDb = sDbHelper.getWritableDatabase(); // Gets the database (either retrieved or created)
        sDb.execSQL("PRAGMA foreign_keys = ON;"); // Allows using foreign keys
        return this;
    }

    /**
     * Closes the database helper.
     */
    public void close() {
        sDbHelper.close();
    }

    //////////////////////////////////////////////////////////////
    ///////////////////METHODS FOR MANAGING SERIES////////////////
    //////////////////////////////////////////////////////////////

    /**
     * Creates a new series in the database.
     * 
     * @param title of the series to be created.
     *              title != null and title.length() > 0
     * @param description of the series to be created.
     *              description != null
     * @return id of the newly created series in the database or -1 to indicate error.
     */
    public long createSeries(String title, String description, String rating) {
        if (title == null || title.equals("") || description == null) {
            return -1;
        }
        ContentValues initialValues = new ContentValues();
        initialValues.put(SERIES_KEY_TITLE, title);
        initialValues.put(SERIES_KEY_DESCRIPTION, description);
        initialValues.put(SERIES_KEY_RATING, rating);
        return sDb.insert(DATABASE_SERIES_TABLE, null, initialValues);
    }

    /**
     * Updates the title and description of an existing series.
     * 
     * @param title new title for the series.
     *              title != null and title.length() > 0
     * @param description of the series to be created.
     *              description != null
     * @param seriesId of the series that will be updated.
     *              seriesId > 0
     * @return true if and only if the series title could be updated.
     */
    public boolean updateSeries(String title, String description, String rating, long seriesId) {
        if (title == null || title.equals("") || description == null || seriesId <= 0) {
            return false;
        }
        ContentValues args = new ContentValues();
        args.put(SERIES_KEY_TITLE, title);
        args.put(SERIES_KEY_DESCRIPTION, description);
        args.put(SERIES_KEY_RATING, rating);
        return sDb.update(DATABASE_SERIES_TABLE, args, SERIES_KEY_ID + "=" + seriesId, null) > 0;
    }

    /**
     * Deletes the series [seriesId] from the database.
     * 
     * @param seriesId id of the series that will be deleted.
     *                 seriesId > 0
     * @return true if and only if the series could be deleted.
     */
    public boolean deleteSeries(long seriesId) {
        return seriesId > 0 &&
                sDb.delete(DATABASE_SERIES_TABLE, SERIES_KEY_ID + "=" + seriesId, null) > 0;
    }

    /**
     * Retrieves the series [seriesId] from the database.
     * 
     * @param seriesId id of the series to be retrieved.
     * @return Cursor positioned at the series with id [seriesId]
     * @throws SQLException if series could not be found/retrieved
     */
    public Cursor fetchSeries(long seriesId) throws SQLException {
       Cursor mCursor =
                sDb.query(true, DATABASE_SERIES_TABLE, new String[]
                                {SERIES_KEY_ID, SERIES_KEY_TITLE, SERIES_KEY_DESCRIPTION, SERIES_KEY_RATING},
                          SERIES_KEY_ID + "=" + seriesId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * Retrieves all series from the database ordered by title if orderByRating is false
     * and by rating if it's true.
     * 
     * @return Cursor positioned at the head of all the series in the database.
     */
    public Cursor fetchAllSeries(boolean orderByRating) {
        String query = "SELECT * FROM " + DATABASE_SERIES_TABLE + " ORDER BY " +
                (orderByRating ? SERIES_KEY_RATING + " DESC" : SERIES_KEY_TITLE + " COLLATE NOCASE");
        return sDb.rawQuery(query, null);
    }

    //////////////////////////////////////////////////////////////
    //////////////////METHODS FOR MANAGING EPISODES///////////////
    //////////////////////////////////////////////////////////////

    /**
     * Creates a new episodes in the database.
     * 
     * @param name of the new episode.
     *             name != null
     * @param season of the new episode.
     *             season > 0
     * @param number of the new episode.
     *             number > 0
     * @param series the new episode belongs to.
     *             series > 0
     * @return id of the new episode or -1 to indicate error.
     */
    public long createEpisode(String name, int season, int number, long series) {
        if (name == null || season <= 0 || number <= 0 || series <= 0) {
            return -1;
        }
        ContentValues initialValues = new ContentValues();
        initialValues.put(EPISODE_KEY_NAME, name);
        initialValues.put(EPISODE_KEY_SEASON_NUM, season);
        initialValues.put(EPISODE_KEY_EPISODE_NUM, number);
        initialValues.put(EPISODE_KEY_SERIES, series);
        return sDb.insert(DATABASE_EPISODES_TABLE, null, initialValues);
    }

    /**
     * Updates the information of an episode [episodeId]
     * 
     * @param name of the updated episode.
     *             name != null
     * @param season of the updated episode.
     *             season > 0
     * @param number of the updated episode.
     *             number > 0
     * @param episodeId of the episode that will be updated.
     *             episodeId > 0
     * @return true if and only if the episode was updated correctly.
     */
    public boolean updateEpisode(String name, int season, int number, long episodeId) {
        if (name == null || season <= 0 || number <= 0 || episodeId <= 0) {
            return false;
        }
        ContentValues args = new ContentValues();
        args.put(EPISODE_KEY_NAME, name);
        args.put(EPISODE_KEY_SEASON_NUM, season);
        args.put(EPISODE_KEY_EPISODE_NUM, number);
        return sDb.update(DATABASE_EPISODES_TABLE, args,
                EPISODE_KEY_ID + "=" + episodeId, null) > 0;
    }

    /**
     * Changes the state of the episode. From watched to unwatched and vice-versa.
     *
     * @param episodeId id of the episode which state will be toggled.
     */
    public void toggleWatched(long episodeId) {
        // Since watched is a boolean value it's not necessary to query the database to change it
        sDb.execSQL("update " + DATABASE_EPISODES_TABLE + " set " + EPISODE_KEY_WATCHED + " = CASE "
                + EPISODE_KEY_WATCHED + " when 0 then 1 when 1 then 0 else 1 end where "
                + EPISODE_KEY_ID + " = " + episodeId);
    }


    /**
     * Deletes the episode [episodeId] from the database.
     * 
     * @param episodeId id of the episode that will be deleted.
     *                  episodeId > 0
     * @return true if and only if the episode could be deleted from the database.
     */
    public boolean deleteEpisode(long episodeId) {
        return episodeId > 0 &&
                sDb.delete(DATABASE_EPISODES_TABLE, EPISODE_KEY_ID + "=" + episodeId, null) > 0;
    }

    /**
     * Retrieves the episode [episodeId] from the database.
     * 
     * @param episodeId id of the episode to be retrieved.
     * @return Cursor positioned at the episode with id [episodeId]
     */
    public Cursor fetchEpisode(long episodeId) {
        Cursor mCursor =
                sDb.query(true, DATABASE_EPISODES_TABLE, new String[]
                                {EPISODE_KEY_ID, EPISODE_KEY_NAME, EPISODE_KEY_SEASON_NUM,
                                        EPISODE_KEY_EPISODE_NUM, EPISODE_KEY_SERIES},
                          EPISODE_KEY_ID + "=" + episodeId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * Get the number of seasons of a series.
     * 
     * @param series series which number of seasons will be returned
     * @return number of seasons of series
     */
    public Cursor getSeasons(long series) {
        String query = "SELECT DISTINCT " + EPISODE_KEY_SEASON_NUM + " FROM " +
                DATABASE_EPISODES_TABLE + " WHERE " + EPISODE_KEY_SERIES + " = " + series
                + " ORDER BY " + EPISODE_KEY_SEASON_NUM;
        return sDb.rawQuery(query, null);
    }

    /**
     * Returns a cursor to an especific season of a series.
     */
    public Cursor fetchEpisodesFromSeason(long series, int season) {
        String query = "SELECT * FROM " + DATABASE_EPISODES_TABLE +
                " WHERE " + EPISODE_KEY_SERIES + " = " + series + " AND " + EPISODE_KEY_SEASON_NUM +
                " = " + season + " ORDER BY " + EPISODE_KEY_EPISODE_NUM;
        return sDb.rawQuery(query, null);
    }

    /**
     * Class to manage the series database creating or updating when needed.
     */
    private static class SeriesDatabaseHelper extends SQLiteOpenHelper {

        /**
         * Constructor - Retrieves, creates or updates the database that holds all the information
         * for all series.
         * @param context in which the database exists or will be created.
         */
        SeriesDatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        /**
         * Creates both the series table and the episode table in the database.
         * 
         * @param db database where the tables will be created.
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_SERIES_TABLE);
            db.execSQL(CREATE_EPISODE_TABLE);
        }

        /**
         * Drops all tables and re creates them.
         *
         * @param db database which tables will be dropped and recreated
         * @param newVersion the database's new version. It's just an ID for future updates
         * @param oldVersion the database's old version.
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(this.getClass().toString(), "Upgrading database from version " + oldVersion +
                    " to " + newVersion + ", will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_SERIES_TABLE );
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_EPISODES_TABLE );
            onCreate(db);
        }
    }

    public SQLiteDatabase getsDb(){
        return sDb;
    }

}
