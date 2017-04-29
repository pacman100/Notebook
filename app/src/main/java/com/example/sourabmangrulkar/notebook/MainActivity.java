package com.example.sourabmangrulkar.notebook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    public static final String NOTE_ID_EXTRA="com.example.sourabmangrulkar.notebook.Note Identifier";
    public static final String NOTE_TITLE_EXTRA="com.example.sourabmangrulkar.notebook.Note Title";
    public static final String NOTE_MESSAGE_EXTRA="com.example.sourabmangrulkar.notebook.Note Message";
    public static final String NOTE_CATEGORY_EXTRA="com.example.sourabmangrulkar.notebook.Note Category";
    public static final String NOTE_FRAGMENT_TO_LAUNCH="com.example.sourabmangrulkar.notebook.fragment to launch";

    public enum fragmentToLaunch{VIEW,EDIT,CREATE}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/



        loadPreferences();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //create an intent to open NoteDetailActivity
            Intent intent=new Intent(this, AppPreferences.class);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.action_add_note) {
            //create an intent to open NoteDetailActivity
            Intent intent=new Intent(this, NoteDetailActivity.class);
            //send the enum CREATE so that a new modified NoteEditFragment can be put dynamically in NoteDetailActivity
            intent.putExtra(NOTE_FRAGMENT_TO_LAUNCH , fragmentToLaunch.CREATE);
            //start activity with intent
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        boolean isBackgroundDark = sharedPreferences.getBoolean("bg_color", false);
        if(isBackgroundDark) {
            LinearLayout mainLayout = (LinearLayout) findViewById(R.id.content_main);
            mainLayout.setBackgroundColor(Color.parseColor("#3c3f41"));
        }

        String appTitle = sharedPreferences.getString("title","Notebook");
        setTitle(appTitle);

    }
}
