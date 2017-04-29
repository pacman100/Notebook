package com.example.sourabmangrulkar.notebook;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NoteDetailActivity extends AppCompatActivity {

    public static final String NEW_NOTE_EXTRA="New Note";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        createAndAddFragment();
    }

    private void createAndAddFragment(){
        //grabing the intent
        Intent intent=getIntent();
        //getting the EDIT or VIEW or CREATE as per user's choice
        MainActivity.fragmentToLaunch fragmentToLaunch=(MainActivity.fragmentToLaunch)
                intent.getSerializableExtra(MainActivity.NOTE_FRAGMENT_TO_LAUNCH);

        //grabing the fragment manager and starting fragment transaction for
        // dynamic loading of either NoteViewFragment or NoteEditFragment
        android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();

        switch (fragmentToLaunch){
            case VIEW:
                NoteViewFragment noteViewFragment=new NoteViewFragment();
                fragmentTransaction.add(R.id.note_container,noteViewFragment,"NOTE_VIEW_FRAGMENT");
                setTitle(R.string.fragment_view_title);
                break;

            case EDIT:
                NoteEditFragment noteEditFragment=new NoteEditFragment();
                fragmentTransaction.add(R.id.note_container,noteEditFragment,"NOTE_EDIT_FRAGMENT");
                setTitle(R.string.fragment_edit_title);
                break;

            case CREATE:
                NoteEditFragment noteCreateFragment=new NoteEditFragment();
                Bundle bundle=new Bundle();
                bundle.putBoolean(NEW_NOTE_EXTRA,true);
                noteCreateFragment.setArguments(bundle);
                fragmentTransaction.add(R.id.note_container,noteCreateFragment,"NOTE_CREATE_FRAGMENT");
                setTitle(R.string.fragment_create_title);
                break;
        }
        //commit all the changes done
        fragmentTransaction.commit();

    }
}
