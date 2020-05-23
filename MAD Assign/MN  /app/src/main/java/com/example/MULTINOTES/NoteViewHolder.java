package com.example.MULTINOTES;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class NoteViewHolder extends RecyclerView.ViewHolder {
    public TextView GR_note_title;
    public TextView GR_note_text;
    public TextView GR_note_last_updated_date;

    public NoteViewHolder(View view) {
        super(view);
        GR_note_title = view.findViewById(R.id.GR_title_note);
        GR_note_text = view.findViewById(R.id.GR_note_text);
        GR_note_last_updated_date = view.findViewById(R.id.GR_updated_date_last_note);
    }
}
