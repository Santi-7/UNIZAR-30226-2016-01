package com.w2w.whattowatch.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.w2w.whattowatch.R;
import com.w2w.whattowatch.data.DbAdapter;

public class EditSeries extends AppCompatActivity implements EditInterface {

    private EditText titleField; // Title text field
    private EditText descField;  // Description text field
    private Long seriesId;       // Series ID, only set when this activity
                                 // is called to edit a series
    private DbAdapter dBAdapter; // Database adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Database adapter.
        dBAdapter = new DbAdapter(this);
        dBAdapter.open();
        // TODO: Title doesn't work. There is no title. Fix it
        setContentView(R.layout.activity_edit_series);
        setTitle(R.string.edit_series);
        // If editing, it retrieves series' fields (title and description).
        titleField = (EditText) findViewById(R.id.title_field);
        descField = (EditText) findViewById(R.id.description_field);
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
     * Saves all user inputs to the database as a series
     */
    public void saveState() {
        String title = titleField.getText().toString();
        String description = descField.getText().toString();
        // The series hasn't been yet created.
        if (seriesId == null) {
            // If title or description null, [create] will return negative value.
            long idTmp = dBAdapter.createSeries(title, description);
            // The series has been correctly created.
            if (idTmp > 0) seriesId = idTmp;
        // The series has already been created.
        } else {
            dBAdapter.updateSeries(title, description, seriesId);
        }
    }

    /**
     * Fills all user input fields with previously existing information from the database.
     */
    public void populateFields() {
        if (seriesId != null) {
            Cursor series = dBAdapter.fetchSeries(seriesId);
            startManagingCursor(series);
            // Title of the series.
            titleField.setText(series.getString(
                    series.getColumnIndexOrThrow(DbAdapter.SERIES_KEY_TITLE)));
            // Description of the series.
            descField.setText(series.getString(
                    series.getColumnIndexOrThrow(DbAdapter.SERIES_KEY_DESCRIPTION)));
        }
    }
}
