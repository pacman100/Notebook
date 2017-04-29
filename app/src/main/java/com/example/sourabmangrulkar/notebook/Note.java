package com.example.sourabmangrulkar.notebook;

import android.graphics.drawable.Drawable;
import android.widget.TextView;

/**
 * Created by Sourab Mangrulkar on 1/14/2017.
 */

public class Note {
    private String title,message;
    private long noteId,dateCreatedMilli;
    private Category category;
    public enum Category {PERSONAL,TECHNICAL,FINANCE,QUOTE};

    public Note(String title,String message,Category category){
        this.title=title;
        this.message=message;
        this.category=category;
        this.noteId=0;
        this.dateCreatedMilli=0;
    }

    public Note(String title,String message,Category category, long noteId,long dateCreatedMilli){
        this.title=title;
        this.message=message;
        this.category=category;
        this.noteId=noteId;
        this.dateCreatedMilli=dateCreatedMilli;
    }

    public String getTitle(){
        return title;
    }

    public String getMessage(){
        return message;
    }

    public Category getCategory(){
        return category;
    }

    public long getNoteId(){
        return noteId;
    }

    public long getDateCreatedMilli(){
        return dateCreatedMilli;
    }

    public String toString(){
        return "ID: "+noteId+" Title:"+title+" Message:"+message+" icon_id: "+category.name()+" Date: "+dateCreatedMilli;
    }

    public int getAssociatedDrawable(){
        return categoryToDrawable(category);
    }

    public static int categoryToDrawable(Category noteCategory){
        switch(noteCategory) {
            case PERSONAL:
                return R.drawable.p;
            case TECHNICAL:
                return R.drawable.t;
            case FINANCE:
                return R.drawable.f;
            case QUOTE:
                return R.drawable.q;
        }
        return R.drawable.p;
    }
}
