package com.example.MULTINOTES;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder>  {
    private static final String TAG = "NoteAdapter";
    private List<Note> GR_noteList;
    private MainActivity GR_mainAct;

    public NoteAdapter(List<Note> GR_noteList, MainActivity ma) {
        this.GR_noteList = GR_noteList;
        GR_mainAct = ma;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING_NEW_MyViewHolder");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_row, parent, false);

        itemView.setOnClickListener(GR_mainAct);
        itemView.setOnLongClickListener(GR_mainAct);

        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: FILLING_VIEW_HOLDER_Note " + position);

        Note note = GR_noteList.get(position);

        holder.GR_note_title.setText(note.getTitle());
        holder.GR_note_text.setText(trimText(note.getText()));
        holder.GR_note_last_updated_date.setText(dateToString(note.getLastUpdatedTime()));
    }

    private String dateToString(Date GR_lastUpdatedTime1) {
        try {
            // Date format  example "Fri Sep 14, 11:08 AM"
            if (GR_lastUpdatedTime1 != null) {
                SimpleDateFormat formatter = (SimpleDateFormat) DateFormat.getDateTimeInstance();
                formatter.applyPattern("EEE MMM dd, hh:mm aaa");
                return formatter.format(GR_lastUpdatedTime1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private String dateToString(LocalDateTime GR_lastUpdatedTime1) {
        Log.d(TAG, "dateToString: lastUpdatedTime="+GR_lastUpdatedTime1);
        // Data format of example "Thu Sep 19, 10:05 PM"
        if(GR_lastUpdatedTime1!=null){
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd, hh:mm aaa",Locale.US);
            return formatter.format(GR_lastUpdatedTime1);
        }
        return null;
    }

    private String trimText(String GR_text1) {
        if(GR_text1!=null){
            // Here it checks If text is more than 80 characters, and if so it trims
            // to 80 characters and adds .... to that
            if(GR_text1.length()>80){
                return GR_text1.substring(0,80).concat("...");
            }
            else{
                // Here If the text is 80 or less than 80 characters, then the whole text is returned
                return GR_text1;
            }
        }
        else{
            return "";
        }
    }

    @Override
    public int getItemCount() {
        return GR_noteList.size();
    }

}
