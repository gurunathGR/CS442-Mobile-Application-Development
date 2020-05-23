package com.example.MULTINOTES;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.JsonWriter;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity // interfaces are noted here!
        implements View.OnClickListener, View.OnLongClickListener {
    private static final String TAG = "MainActivity";
    private static final String TITLE = "MULTI NOTES";

    private static final int REQUEST_CODE_NEW_NOTE = 1;
    private static final int REQUEST_CODE_EXISTING_NOTE = 2;

    private final List<Note> GR_noteList1 = new ArrayList<>();  // Main data content is here
    private RecyclerView GR_recyclerView1; // recyclerview Layouts
    private NoteAdapter GR_nAdapter1;

    // data sent to the GR_recyclerview1 adapter

    private boolean GR_noteListChanged1 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GR_recyclerView1 = findViewById(R.id.GRrecyler);
        GR_nAdapter1 = new NoteAdapter(GR_noteList1, this);
        GR_recyclerView1.setAdapter(GR_nAdapter1);
        GR_recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        loadFile();
        updateTitle();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_add_note:
                Toast.makeText(this, "Add_Note_menu_selected._Before_opening_new_note_screen", Toast.LENGTH_SHORT).show();
                addNewNote();
                return true;
            case R.id.menu_app_info:
                Toast.makeText(this, "App_Info_menu_selected._Before_opening_app_info_screen", Toast.LENGTH_SHORT).show();
                showAppInfo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        saveToFile();
        super.onPause();
    }

    /**
     * This_method_gets_called_when_New_Note_icon_is_clicked
     */
    private void addNewNote() {
        openEditActitiy(false, 0, null);
    }

    // From_OnClickListener
    @Override
    public void onClick(View v) {  // click_listener_called_by_ViewHolder_clicks
        if (GR_noteList1 != null) {
            int GR_notePosition1 = GR_recyclerView1.getChildLayoutPosition(v);
            openEditActitiy(true, GR_notePosition1, GR_noteList1.get(GR_notePosition1));
        }
    }

    private void openEditActitiy(boolean GR_existingNote1, int GR_notePosition1, Note GR_note1) {
        int GR_requstCode1 = REQUEST_CODE_NEW_NOTE;
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("IS_EXISTING_NOTE", GR_existingNote1);
        // For_an_existing_note,_pass_existing-note_object_&_its-position
        if (GR_existingNote1) {
            GR_requstCode1 = REQUEST_CODE_EXISTING_NOTE;
            if (GR_note1 != null) {
                Toast.makeText(this, "Passing EXISTING_NOTE object to EditActivity " +
                        "for an existing note on click", Toast.LENGTH_SHORT).show();
                // Passing_EXISTING_NOTE_object_to_EditActivity_for_an_existing_note_on_click
                intent.putExtra("EXISTING_NOTE", GR_note1);
                intent.putExtra("EXISTING_NOTE_POSITION", GR_notePosition1);
            }
        }
        startActivityForResult(intent, GR_requstCode1);
    }

    @Override
    public void onActivityResult(int GR_requestCode1, int GR_resultCode1, Intent GR_data1) {
        if (GR_requestCode1 == REQUEST_CODE_NEW_NOTE) {
            if (GR_resultCode1 == RESULT_OK) {
                Note newNote = (Note) GR_data1.getSerializableExtra("NEW_NOTE");
                if (newNote != null) {
                    GR_noteList1.add(newNote);
                    Log.d(TAG, "onActivityResult: newNoteAdded: " + newNote.toString());
                    reloadRecycler();
                }
            } else {
                Log.d(TAG, "onActivityResult: result Code: " + GR_resultCode1);
            }

        } else if (GR_requestCode1 == REQUEST_CODE_EXISTING_NOTE) {
            if (GR_resultCode1 == RESULT_OK) {
                boolean isNoteChanged = GR_data1.getBooleanExtra("NOTE_CHANGED", false);
                if (isNoteChanged) {
                    Note existingNote = (Note) GR_data1.getSerializableExtra("EXISTING_NOTE");
                    int notePosition = GR_data1.getIntExtra("EXISTING_NOTE_POSITION", 0);
                    if (existingNote != null) {
                        GR_noteList1.set(notePosition, existingNote);
                        Log.d(TAG, "onActivityResult: ExistingNoteEdited: " + existingNote.toString());
                        reloadRecycler();
                    }
                }
                // Do_nothing_if_existing_note_is_not_changed
            } else {
                Log.d(TAG, "onActivityResult: ExistingNoteEdited: " + GR_resultCode1);
            }
        }
    }

    private void reloadRecycler() {
        GR_noteListChanged1 = true;
        Collections.sort(GR_noteList1);
        updateTitle();
        GR_nAdapter1.notifyDataSetChanged();
    }

    // From_OnLongClickListener
    @Override
    public boolean onLongClick(View v) {  // long_click_listener_called_by_ViewHolder_long_clicks
        int pos = GR_recyclerView1.getChildLayoutPosition(v);
        if (GR_noteList1 != null) {
            String noteTitle = "";
            Note note = GR_noteList1.get(pos);
            if (note != null) {
                noteTitle = note.getTitle();
            }
            shawConfirmationDialogForDeleteNote(pos, noteTitle);
            return true;
        }
        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: ");
        outState.putBoolean("IS_NOTE_LIST_CHANGED", GR_noteListChanged1);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedState) {
        Log.d(TAG, "onRestoreInstanceState: ");
        super.onRestoreInstanceState(savedState);
        GR_noteListChanged1 = savedState.getBoolean("IS_NOTE_LIST_CHANGED");
    }

    public void shawConfirmationDialogForDeleteNote(final int notePosition, final String noteTitle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setIcon(R.drawable.outline_save_black_18dp);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (GR_noteList1 != null) {
                    GR_noteList1.remove(notePosition);
                    reloadRecycler();
                    Toast.makeText(MainActivity.this, "Note '" + noteTitle + "' Deleted_from_the_list", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Do nothing
            }
        });
        builder.setMessage("Delete Note '" + noteTitle + "'?");


        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void loadFile() {
        Log.d(TAG, "loadFile: Loading_JSON_File");
        try {
            InputStream is = getApplicationContext().
                    openFileInput(getString(R.string.GR_multi_note_json_file));
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            JSONObject jsonObject = new JSONObject(sb.toString());
            // fetch JSONArray named noteList
            JSONArray notesJsonArray = jsonObject.getJSONArray("NoteList");
            if (notesJsonArray != null && notesJsonArray.length() > 0) {
                for (int i = 0; i < notesJsonArray.length(); i++) {
                    JSONObject noteJson = notesJsonArray.getJSONObject(i);
                    if (noteJson != null) {
                        GR_noteList1.add(new Note(noteJson.getString("Title"), noteJson.getString("Text"),
                                stringToDate(noteJson.getString("LastUpdatedDate"))));
                    }
                }
            }
            if (GR_noteList1 != null && GR_noteList1.size() > 0) {
                Collections.sort(GR_noteList1);
            }
        } catch (FileNotFoundException e) {
            Toast.makeText(this, getString(R.string.no_file_available), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveToFile() {
        Log.d(TAG, "saveFile: Saving_JSON_File");
        try {
            // Saving to json file only when RecylerView list has been Changed
            if (GR_noteListChanged1 && GR_noteList1 != null) {
                FileOutputStream fos = getApplicationContext().
                        openFileOutput(getString(R.string.GR_multi_note_json_file), Context.MODE_PRIVATE);
                JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, getString(R.string.encoding)));
                writer.setIndent("  ");

                writer.beginObject();
                writer.name("NoteList");
                writer.beginArray();

                if (GR_noteList1.size() > 0) {
                    for (Note note : GR_noteList1) {
                        if (note != null) {
                            writer.beginObject();
                            writer.name("Title").value(note.getTitle());
                            writer.name("Text").value(note.getText());
                            writer.name("LastUpdatedDate").value(dateToString(note.getLastUpdatedTime()));
                            writer.endObject();
                        }
                    }
                }
                writer.endArray();
                writer.endObject();
                writer.flush();
                writer.close();
                GR_noteListChanged1= false;
                Toast.makeText(this, getString(R.string.GR_saved), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error_occurred_while_saving_json_file", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    private void showAppInfo() {
        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, MainActivity.class.getSimpleName());
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        Toast.makeText(this, "The_back_button_was_pressed_-_Bye!", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }

    private Date stringToDate(String GR_lastUpdatedTimeStr1) {
        try {
            // Date format example "FRI Sep 24, 12:05 PM"
            if (!TextUtils.isEmpty(GR_lastUpdatedTimeStr1)) {
                SimpleDateFormat formatter = (SimpleDateFormat) DateFormat.getDateTimeInstance();
                formatter.applyPattern("EEE, d MMM yyyy HH:mm:ss");
                return formatter.parse(GR_lastUpdatedTimeStr1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String dateToString(Date GR_lastUpdatedTime1) {
        try {
            // Date_format_of_example_"Thu Sep 19, 10:05 PM"
            if (GR_lastUpdatedTime1 != null) {
                SimpleDateFormat formatter = (SimpleDateFormat) DateFormat.getDateTimeInstance();
                formatter.applyPattern("EEE, d MMM yyyy HH:mm:ss");
                return formatter.format(GR_lastUpdatedTime1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private void updateTitle() {
        // setting_title
        if (GR_noteList1 != null && GR_noteList1.size() > 0) {
            setTitle(TITLE + " (" + GR_noteList1.size() + ")");
        } else {
            setTitle(TITLE);
        }
    }
}