package com.example.sourabmangrulkar.notebook;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainActivityListFragment extends ListFragment {
    private ArrayList<Note> notes;
    private NoteAdapter noteAdapter;
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

       /* String values[]=new String[] {"Android","Windows","IOS","Linux"};
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,values);
        setListAdapter(adapter);*/

        /*notes=new ArrayList<Note>();
        notes.add(new Note("This is Note-1","This is the body of Note-1",Note.Category.PERSONAL));
        notes.add(new Note("This is Note-2","This is the body of Note-2",Note.Category.TECHNICAL));
        notes.add(new Note("This is Note-3","This is the body of Note-3",Note.Category.FINANCE));
        notes.add(new Note("This is Note-4","This is the body of Note-4",Note.Category.QUOTE));
        notes.add(new Note("This is Note-5","This is the body of Note-5",Note.Category.PERSONAL));
        notes.add(new Note("This is Note-6","This is the body of Note-6",Note.Category.FINANCE));
        notes.add(new Note("This is Note-7","This is the body of Note-7",Note.Category.TECHNICAL));
        notes.add(new Note("This is Note-8","This is the body of Note-8",Note.Category.QUOTE));
        notes.add(new Note("This is Note-9 and this is a very long title","This is the body of Note-9 and the body is very vvery very long",Note.Category.PERSONAL));
        notes.add(new Note("This is Note-10","This is the body of Note-10",Note.Category.PERSONAL));*/

        NotebookDBAdapter notebookDBAdapter = new NotebookDBAdapter(getActivity().getBaseContext());
        notebookDBAdapter.open();
        notes = notebookDBAdapter.getAllNotes();
        notebookDBAdapter.close();

        noteAdapter=new NoteAdapter(getActivity(),notes);
        setListAdapter(noteAdapter);

        registerForContextMenu(getListView());

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l,v,position,id);
        launchNoteDetailActivity(MainActivity.fragmentToLaunch.VIEW,position);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu,View v,ContextMenu.ContextMenuInfo contextMenuInfo){
        super.onCreateContextMenu(menu,v,contextMenuInfo);
        MenuInflater menuInflater=getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.long_press_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int note_position=info.position;
        Note note = (Note) getListAdapter().getItem(note_position);
       //returns to us the id of the menu item we select
        switch(item.getItemId())
        {
            case R.id.edit:
                //do stuff
                launchNoteDetailActivity(MainActivity.fragmentToLaunch.EDIT,note_position);
                Log.d("Menu_Item_Selected","Edit");
                return true;

            case R.id.delete:
                //delete note
                NotebookDBAdapter notebookDBAdapter = new NotebookDBAdapter(getActivity().getBaseContext());
                notebookDBAdapter.open();
                notebookDBAdapter.deleteNote(note.getNoteId());

                notes.clear();
                notes.addAll(notebookDBAdapter.getAllNotes());
                noteAdapter.notifyDataSetChanged();

                notebookDBAdapter.close();
        }

        return super.onContextItemSelected(item);
    }

    private void launchNoteDetailActivity(MainActivity.fragmentToLaunch ftl,int position){
        //Getting the note which has been clicked using it's position
        Note note=(Note) getListAdapter().getItem(position);

        //creating intent to launch NoteDetailActivity from MainActivityListFragment
        Intent intent=new Intent(getActivity(),NoteDetailActivity.class);

        //passing information of widgets to the NoteDetailActivity
        intent.putExtra(MainActivity.NOTE_TITLE_EXTRA,note.getTitle());
        intent.putExtra(MainActivity.NOTE_MESSAGE_EXTRA,note.getMessage());
        intent.putExtra(MainActivity.NOTE_ID_EXTRA,note.getNoteId());
        intent.putExtra(MainActivity.NOTE_CATEGORY_EXTRA,note.getCategory());

        //Deciding which fragment to launch in NoteDetailActivity
        switch (ftl){
            case VIEW:
                intent.putExtra(MainActivity.NOTE_FRAGMENT_TO_LAUNCH,MainActivity.fragmentToLaunch.VIEW);
                break;

            case EDIT:
                intent.putExtra(MainActivity.NOTE_FRAGMENT_TO_LAUNCH,MainActivity.fragmentToLaunch.EDIT);
                break;
        }

        //starting the intent to launch NoteDetailActivity
        startActivity(intent);

    }

}
