package com.w2w.whattowatch.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.w2w.whattowatch.R;
import com.w2w.whattowatch.data.DbAdapter;

public class EditSeries extends EditAbstract {

    private EditText titleField; // Title text field
    private EditText descField;  // Description text field
    private Spinner ratingSpinner;// Score that holds all possible scores for a series
    private Long seriesId;       // Series ID, only set when this activity
                                 // is called to edit a series
    private DbAdapter dBAdapter; // Database adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Database adapter.
        dBAdapter = new DbAdapter(this);
        dBAdapter.open();
        setContentView(R.layout.activity_edit_series);
        setTitle(R.string.edit_series);
        // If editing, it retrieves series' fields (title and description).
        titleField = (EditText) findViewById(R.id.title_field);
        descField = (EditText) findViewById(R.id.description_field);
        ratingSpinner = (Spinner) findViewById(R.id.score_spinner);
        // Save button.
        Button confirmButton = (Button) findViewById(R.id.save_button);

        seriesId = (savedInstanceState == null) ? null :
                   (Long) savedInstanceState.getSerializable(DbAdapter.SERIES_KEY_ID);
        if (seriesId == null) {
            Bundle extras = getIntent().getExtras();
            seriesId = (extras != null) ? extras.getLong(DbAdapter.SERIES_KEY_ID) :
                        null;
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
        outState.putSerializable(DbAdapter.SERIES_KEY_ID, seriesId);
    }

    /////////////////////////////////////// EditAbstract ///////////////////////////////////////

    /**
     * Saves all user inputs to the database as a series
     */
    protected void saveState() {
        String title = titleField.getText().toString();
        String description = descField.getText().toString();
        String rating;
        int choice = ratingSpinner.getSelectedItemPosition();
        rating = choice == 0 ? null : choice + "";

        // The series hasn't been yet created.
        if (seriesId == null) {
            // If title or description null, [create] will return negative value.
            long idTmp = dBAdapter.createSeries(title, description, rating);
            // The series has been correctly created.
            if (idTmp > 0) seriesId = idTmp;
        // The series has already been created.
        } else {
            dBAdapter.updateSeries(title, description, rating, seriesId);
        }
    }

    /**
     * Fills all user input fields with previously existing information from the database.
     */
    protected void populateFields() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.scores_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        ratingSpinner.setAdapter(adapter);
        if (seriesId != null) {
            String categoria;
            Cursor series = dBAdapter.fetchSeries(seriesId);
            startManagingCursor(series);
            // Title of the series.
            titleField.setText(series.getString(
                    series.getColumnIndexOrThrow(DbAdapter.SERIES_KEY_TITLE)));
            // Description of the series.
            descField.setText(series.getString(
                    series.getColumnIndexOrThrow(DbAdapter.SERIES_KEY_DESCRIPTION)));

            categoria = series.getString(series.getColumnIndexOrThrow(DbAdapter.SERIES_KEY_RATING));
            if (categoria==null){
                ratingSpinner.setSelection(0);
            }
            else{
                ratingSpinner.setSelection(adapter.getPosition(categoria));
            }
        }
    }
}
