package com.w2w.whattowatch.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.w2w.whattowatch.R;
import com.w2w.whattowatch.data.DbAdapter;

public class EditEpisodes extends AppCompatActivity implements EditInterface {

    private EditText nameField; // Name text field
    private EditText seasonField;  // Season number text field
    private EditText numberField;  // Number of the episode text field
    private Long seriesId;         // Series the episode belongs to
    private Long episodeId;       // Episode ID, only set when this activity
    // is called to edit an episode
    private DbAdapter dBAdapter; // Database adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_episodes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dBAdapter = new DbAdapter(this);
        dBAdapter.open();

        nameField = (EditText) findViewById(R.id.name_field);
        seasonField = (EditText) findViewById(R.id.season_field);
        numberField = (EditText) findViewById(R.id.number_field);

        seriesId = (savedInstanceState == null) ? null :
                (Long) savedInstanceState.getSerializable(DbAdapter.SERIES_KEY_ID);
        if (seriesId == null) {
            Bundle extras = getIntent().getExtras();
            seriesId = (extras != null) ? extras.getLong(DbAdapter.SERIES_KEY_ID)
                    : null;
        }
        Button confirmButton = (Button) findViewById(R.id.save_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                setResult(RESULT_OK);
                checkAndFinish();
            }

        });
    }

    // TODO: Remove menu from this class.
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
        if (id == 1) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkAndFinish() {
        boolean error = false;
        try {
            nameField.getText().toString();
            String season = seasonField.getText().toString();
            String number = numberField.getText().toString();
            Log.d("READ", season + " " + number);
        } catch (NullPointerException e) {
            Log.d("READ", "NULL STUFF " + seasonField + " " + numberField);
            error = true;
        }
        if (error) {
            Log.d("TODO", " " + nameField + seasonField + numberField);
            Snackbar.make(findViewById(R.id.layout), "Must fill all fields", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else {
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(DbAdapter.EPISODE_KEY_ID, episodeId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }
    /**
     * Saves all user inputs to the database as an episode
     */
    public void saveState(){
        String name = nameField.getText().toString();
        int season = Integer.parseInt(seasonField.getText().toString());
        int number = Integer.parseInt(numberField.getText().toString());

        if (episodeId == null) {
            Log.d("NEW EPISODE", name + " " + season + " " + number + " " + seriesId);
            long idTmp = dBAdapter.createEpisode(name, season, number, seriesId);
            if (idTmp > 0) episodeId = idTmp;
        } else {
            dBAdapter.updateEpisode(name, season, number, seriesId, episodeId);
        }
    }

    /**
     * Fills all user input fields with previously existing information from the database.
     */
    public void populateFields(){
        // TODO: implement populateFields
    }

}
