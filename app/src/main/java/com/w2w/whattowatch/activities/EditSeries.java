package com.w2w.whattowatch.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.w2w.whattowatch.R;
import com.w2w.whattowatch.data.DbAdapter;

public class EditSeries extends AppCompatActivity implements EditInterface {

    private EditText titleField; // Title text field
    private EditText descField;  // Description text field
    private Long seriesId;       // Series ID, only set when this activity is called to edit a
    // series

    private DbAdapter dBAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_series);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Floating Button, again, probably unnecessary
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //
        dBAdapter = new DbAdapter(this);
        dBAdapter.open();
        // TODO: check whether this activity was started to create or to update
        // TODO: populate fields
        titleField = (EditText) findViewById(R.id.title_field);
        descField = (EditText) findViewById(R.id.description_field);

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

    /**
     * Calls saveState() when the UI button is pressed
     * @param view [button], unused
     */
    public void buttonPressed(View view) {
        saveState();
        setResult(RESULT_OK);
        // For testing purposes:
        finish(); // TODO: Don't finish if title (or description?) where null.
        // To implement the task above, saveState could return a boolean value
    }

    /**
     * Saves all user inputs to the database as a series
     */
    public void saveState(){
        String title = titleField.getText().toString();
        String description = descField.getText().toString();
        // TODO: Check if title and or description are null, show warning on screen?
        // Warning example above, "Snackbar.make...."
        if (seriesId == null) {
            long idTmp = dBAdapter.createSeries(title, description);
            if (idTmp > 0) seriesId = idTmp;
        } else {
            dBAdapter.updateSeries(title, description, seriesId);
        }
    }

    /**
     * Fills all user input fields with previously existing information from the database.
     * @param elementId id of the series which information will be retrieved to the input
     *      fields from the database.
     */
    public void populateFields(long elementId){

    }
}
