package com.example.MULTINOTES;

import java.io.Serializable;
import java.util.Date;

public class Note implements Comparable<Note>, Serializable {

    private String GR_title1;
    private String GR_text1;
    private Date GR_lastUpdatedTime1;

    public Note(String GR_title1, String GR_text1, Date GR_lastUpdatedTime1) {
        this.GR_title1 = GR_title1;
        this.GR_text1 = GR_text1;
        this.GR_lastUpdatedTime1 = GR_lastUpdatedTime1;
    }

    public String getTitle() {
        return GR_title1;
    }

    public void setTitle(String GR_title1) {
        this.GR_title1 = GR_title1;
    }

    public String getText() {
        return GR_text1;
    }

    public void setText(String GR_text1) {
        this.GR_text1 = GR_text1;
    }

    public Date getLastUpdatedTime() {
        return GR_lastUpdatedTime1;
    }

    public void setLastUpdatedTime(Date GR_lastUpdatedTime1) {
        this.GR_lastUpdatedTime1 = GR_lastUpdatedTime1;
    }

    @Override
    public int compareTo(Note note) {
        if (note == null || getLastUpdatedTime() == null || note.getLastUpdatedTime() == null) {
            return 0;
        }
        return note.getLastUpdatedTime().compareTo(this.GR_lastUpdatedTime1);
    }

    @Override
    public String toString() {
        return "Title=" + GR_title1 + ", Text=" + GR_text1 + ", lastUpdatedTime=" + GR_lastUpdatedTime1;
    }
}
