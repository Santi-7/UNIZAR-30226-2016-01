package com.w2w.whattowatch.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.w2w.whattowatch.R;

public class ListEpisodes extends AppCompatActivity implements ListInterface{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_series);
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

        long seriesId = (Long) this.getIntent().getExtras().get("sid");
        Log.d("ListEpisodes: ", "Started for series: " + seriesId);
        //TODO: Implement UI, this includes using fragments.
        //TODO: Retrieve info for seriesId and fill the UI

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
            case R.id.create_new_episode:
                create();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Fetches and shows all episodes from the database.
     */
    public void list(){
        // TODO: Implement list()
    }

    /**
     * Starts an activity to create a new episode
     */
    public void create(){
        // TODO: Implement create()
    }

    /**
     * Starts an activity to edit an episode
     * @param elementId id of the episode that will be edited
     */
    public void edit(long elementId){
        // TODO: Implement edit()
    }

    /**
     * Deletes the episode elementId
     * @param elementId id of the episode that will be deleted
     */
    public void delete(long elementId){
        // TODO: Implement delete()
    }
}
