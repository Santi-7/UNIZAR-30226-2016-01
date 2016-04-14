package com.w2w.whattowatch.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.w2w.whattowatch.R;
import com.w2w.whattowatch.data.DbAdapter;

public class EditEpisodes extends EditAbstract {

    private EditText nameField;     // Name text field
    private EditText seasonField;   // Season number text field
    private EditText numberField;   // Number of the episode text field
    private Long seriesId;          // Series the episode belongs to
    private Long episodeId;         // Episode ID, only set when this activity
                                    // is called to edit an episode
    private DbAdapter dBAdapter;    // Database adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Database adapter.
        dBAdapter = new DbAdapter(this);
        dBAdapter.open();
        setContentView(R.layout.activity_edit_episodes);
        setTitle(R.string.edit_episode);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // If editing, it retrieves series' fields (name, seasonNumber and episodeNumber).
        nameField = (EditText) findViewById(R.id.name_field);
        seasonField = (EditText) findViewById(R.id.season_field);
        numberField = (EditText) findViewById(R.id.number_field);
        // Save button.
        Button confirmButton = (Button) findViewById(R.id.save_button);

        episodeId = (savedInstanceState == null) ? null :
                    (Long) savedInstanceState.getSerializable(DbAdapter.EPISODE_KEY_ID);
        if (episodeId == null) {
            Bundle extras = getIntent().getExtras();
            episodeId = extras.getLong(DbAdapter.EPISODE_KEY_ID);
            if (episodeId == 0) episodeId = null;
            seriesId = extras.getLong(DbAdapter.EPISODE_KEY_SERIES);
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(DbAdapter.EPISODE_KEY_ID, episodeId);
    }

    /////////////////////////////////////// EditAbstract ///////////////////////////////////////

    /**
     * Saves all user inputs to the database as an episode.
     */
    public void saveState() {
        String name = nameField.getText().toString();
        int season = 0, number = 0;
        try {
            season = Integer.parseInt(seasonField.getText().toString());
            number = Integer.parseInt(numberField.getText().toString());
        }
        // Season or number aren't numbers. Database will reject the query.
        catch (NumberFormatException e) { }
        // The episode hasn't been yet created.
        if (episodeId == null) {
            // If fields are incorrect, [create] will return negative value.
            long idTmp = dBAdapter.createEpisode(name, season, number, seriesId);
            // The episode has been correctly created.
            if (idTmp > 0) episodeId = idTmp;
            // TODO: else: Show pop-up
        }
        // The episode has already been created.
        else {
            dBAdapter.updateEpisode(name, season, number, episodeId);
            // TODO: update = false: Show pop-up
        }
    }

    /**
     * Fills all user input fields with previously existing information from the database.
     */
    public void populateFields() {
        if (episodeId != null) {
            Cursor episode = dBAdapter.fetchEpisode(episodeId);
            startManagingCursor(episode);
            // Name of the episode.
            nameField.setText(episode.getString(
                    episode.getColumnIndexOrThrow(DbAdapter.EPISODE_KEY_NAME)));
            // Season number of the episode.
            seasonField.setText(episode.getString(
                    episode.getColumnIndexOrThrow(DbAdapter.EPISODE_KEY_SEASON_NUM)));
            // Number of the episode.
            numberField.setText(episode.getString(
                    episode.getColumnIndexOrThrow(DbAdapter.EPISODE_KEY_EPISODE_NUM)));
        }
    }
}
