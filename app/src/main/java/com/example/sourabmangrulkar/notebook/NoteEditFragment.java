package com.example.sourabmangrulkar.notebook;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoteEditFragment extends Fragment {

    private ImageButton iconCategory;
    private EditText title,message;
    private AlertDialog categoryAlertDialogBox,confirmAlertDialogBox;
    private Note.Category chosenCategoryButton;
    private static final String MODIFIED_CATEGORY="Modified Category";
    private boolean newNote = false;
    private long noteId = 0;
    public NoteEditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if(bundle!=null){
            newNote = bundle.getBoolean(NoteDetailActivity.NEW_NOTE_EXTRA,false);
        }

        if(savedInstanceState != null){
            chosenCategoryButton=(Note.Category)savedInstanceState.get(MODIFIED_CATEGORY);
        }
        View fragmentLayout=inflater.inflate(R.layout.fragment_note_edit,container,false);

        title=(EditText) fragmentLayout.findViewById(R.id.editNoteTitle);
        message=(EditText) fragmentLayout.findViewById(R.id.editNoteMessage);
        iconCategory=(ImageButton) fragmentLayout.findViewById(R.id.editNoteIconButton);
        Button saveButton=(Button) fragmentLayout.findViewById(R.id.editSaveButton);

        Intent intent=getActivity().getIntent();

        title.setText(intent.getExtras().getString(MainActivity.NOTE_TITLE_EXTRA,""));
        message.setText(intent.getExtras().getString(MainActivity.NOTE_MESSAGE_EXTRA,""));
        noteId = intent.getExtras().getLong(MainActivity.NOTE_ID_EXTRA, 0);

        if(savedInstanceState!= null){
            iconCategory.setImageResource(Note.categoryToDrawable(chosenCategoryButton));
        }
        else if(!newNote) {
            Note.Category noteCate = (Note.Category) intent.getSerializableExtra(MainActivity.NOTE_CATEGORY_EXTRA);
            iconCategory.setImageResource(Note.categoryToDrawable(noteCate));
            chosenCategoryButton = noteCate;
        }

        //call alert dialog builder method for icon
        categoryAlertDialogBuilder();

        //call alert dialog builder for save button
        confirmAlertDialogBuilder();

        //onClickListener for icon Button
        iconCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryAlertDialogBox.show();
            }
        });

        //OnClickListener for save Button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmAlertDialogBox.show();
            }
        });

        // Inflate the layout for this fragment
        return fragmentLayout;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable(MODIFIED_CATEGORY,chosenCategoryButton);
    }


    private void categoryAlertDialogBuilder(){
        //categories to be displayed in the alert dialog box
        String[] categories=new String[]{"Personal","Technical","Quote","Financial"};

        //creating a alert dialog box builder to build it
        AlertDialog.Builder categoryBuilder=new AlertDialog.Builder(getActivity());

        //setting alert box title
        categoryBuilder.setTitle("Choose Note Type");

        //type of alert dialog box
        categoryBuilder.setSingleChoiceItems(categories, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int item) {

                categoryAlertDialogBox.cancel();
                switch (item){
                    case  0:
                        chosenCategoryButton=Note.Category.PERSONAL;
                        iconCategory.setImageResource(R.drawable.p);
                        break;
                    case 1:
                        chosenCategoryButton=Note.Category.TECHNICAL;
                        iconCategory.setImageResource(R.drawable.t);
                        break;
                    case 2:
                        chosenCategoryButton=Note.Category.QUOTE;
                        iconCategory.setImageResource(R.drawable.q);
                        break;
                    case 3:
                        chosenCategoryButton=Note.Category.FINANCE;
                        iconCategory.setImageResource(R.drawable.f);
                        break;
                }

            }
        });

        //creating alert dialog box using builder
        categoryAlertDialogBox=categoryBuilder.create();
    }

    private void confirmAlertDialogBuilder(){

        //creating a alert dialog box builder to build it
        AlertDialog.Builder confirmBuilder=new AlertDialog.Builder(getActivity());

        //setting alert box title
        confirmBuilder.setTitle("Save Changes?");
        confirmBuilder.setMessage("Do you want to save the changes?");

        //type of alert dialog box
        //positive confirm button
        confirmBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("Edited note","note title: "+title.getText()+" note message: "+message.getText()
                        +" note category: "+ chosenCategoryButton);

                NotebookDBAdapter notebookDBAdapter = new NotebookDBAdapter(getActivity().getBaseContext());
                notebookDBAdapter.open();

                if(newNote) {
                    //if creating a new note then add it to the database
                    notebookDBAdapter.createNote(title.getText().toString(), message.getText().toString(),
                            (chosenCategoryButton == null)? Note.Category.PERSONAL : chosenCategoryButton);
                }
                else {
                    //if updating an already existing note
                    notebookDBAdapter.updateNote(noteId, title.getText().toString(), message.getText().toString(), chosenCategoryButton);
                }

                notebookDBAdapter.close();
                //go back to main activity after saving the note
                Intent intent=new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
            }
        });

        //negative cancel button
        confirmBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //do nothing
            }
        });
        //creating alert dialog box using builder
        confirmAlertDialogBox = confirmBuilder.create();
    }



}
