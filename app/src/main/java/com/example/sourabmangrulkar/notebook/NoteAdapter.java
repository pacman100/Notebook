package com.example.sourabmangrulkar.notebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sourab Mangrulkar on 1/14/2017.
 */

public class NoteAdapter extends ArrayAdapter<Note> {
    public static class ViewHolder{
        TextView  note_title,note_message;
        ImageView note_img;
    }
    public NoteAdapter(Context context, ArrayList<Note> notes){
        super(context,0,notes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Note note=getItem(position);
        ViewHolder viewHolder;

        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.list_view,parent,false);
            viewHolder= new ViewHolder();
            viewHolder.note_title=(TextView)convertView.findViewById(R.id.listItemNoteTitle);
            viewHolder.note_message=(TextView)convertView.findViewById(R.id.listItemNoteBody);
            viewHolder.note_img=(ImageView) convertView.findViewById(R.id.listItemNoteImg);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolder)convertView.getTag();
        }

        viewHolder.note_title.setText(note.getTitle());
        viewHolder.note_message.setText(note.getMessage());
        viewHolder.note_img.setImageResource(note.getAssociatedDrawable());

        return convertView;

    }
}
