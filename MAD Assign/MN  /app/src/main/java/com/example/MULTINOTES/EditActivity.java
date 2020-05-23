package com.example.MULTINOTES;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

public class EditActivity extends AppCompatActivity {
    private static final String TAG = "EditActivity";
    private EditText GR_titleView1 = null;
    private EditText GR_textView1 = null;
    private int GR_notePosition1;
    private boolean GR_isExistingNote1;

    private String GR_existingTitleValue1 = "";
    private String GR_existingTextValue1 = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        GR_titleView1 = findViewById(R.id.GR_new_title_note);
        GR_textView1 = findViewById(R.id.GR_new_text_note);
        GR_textView1.setMovementMethod(new ScrollingMovementMethod());
        GR_textView1.setGravity(Gravity.TOP);
        GR_textView1.setTextIsSelectable(true);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("IS_EXISTING_NOTE")) {
            GR_isExistingNote1 = intent.getBooleanExtra("IS_EXISTING_NOTE", false);
            if (GR_isExistingNote1) {
                Note cur_exitingNote = (Note) intent.getSerializableExtra("EXISTING_NOTE");
               GR_notePosition1 = intent.getIntExtra("EXISTING_NOTE_POSITION", 0);
                if (cur_exitingNote != null) {
                    GR_existingTitleValue1 = cur_exitingNote.getTitle();
                    GR_existingTextValue1 = cur_exitingNote.getText();
                    GR_titleView1.setText(GR_existingTitleValue1);
                    GR_textView1.setText(GR_existingTextValue1);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu GR_menu) {
        getMenuInflater().inflate(R.menu.menu_save_note, GR_menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem GR_item) {
        switch (GR_item.getItemId()) {
            case R.id.menu_save_note:
                validateAndSaveNote(false);
                return true;
            default:
                return super.onOptionsItemSelected(GR_item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: ");
        outState.putInt("EXISTING_NOTE_POS", GR_notePosition1);
        outState.putBoolean("IS_EXISTING_NOTE", GR_isExistingNote1);
        outState.putString("TITLE_VAL_EXISTING_NOTE", GR_existingTitleValue1);
        outState.putString("TEXT_VAL_EXISTING_NOTE", GR_existingTextValue1);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedState) {
        Log.d(TAG, "onRestoreInstanceState: ");
        super.onRestoreInstanceState(savedState);
        GR_notePosition1 = savedState.getInt("EXISTING_NOTE_POS");
        GR_isExistingNote1 = savedState.getBoolean("IS_EXISTING_NOTE");
        GR_existingTitleValue1 = savedState.getString("TITLE_VAL_EXISTING_NOTE");
        GR_existingTextValue1 = savedState.getString("TEXT_VAL_EXISTING_NOTE");
    }

    @Override
    public void onBackPressed() {
        validateAndSaveNote(true);
    }

    public void navigateBackToMainActivity(Intent datareq){
        setResult(RESULT_OK, datareq);
        finish();
        // The_current_activity_is_closed_and_returns_us_to_the_orginal_activity

    }

    public void simplyExitActivity(){
        Intent datareq = new Intent(); // Used_to hold_results_data_to_be_returned_to_original_activity
        datareq.putExtra("NOTE_CHANGED", false);
        navigateBackToMainActivity(datareq);
    }

    private void saveExistingNote() {
        String GR_titleEntered1 = GR_titleView1.getText().toString();
        String GR_textEntered1 = GR_textView1.getText().toString();
        Intent datareq = new Intent(); // Used_to_hold_results-data_to_be_returned_to_original_activity
        Note GR_existingNote1 = new Note(GR_titleEntered1, GR_textEntered1, new Date());
        datareq.putExtra("EXISTING_NOTE", GR_existingNote1);
        datareq.putExtra("EXISTING_NOTE_POSITION", GR_notePosition1);
        datareq.putExtra("NOTE_CHANGED", true);
        navigateBackToMainActivity(datareq);
    }

    private void saveNewNote() {
        String GR_titleEntered1 = GR_titleView1.getText().toString();
        String GR_textEntered1 = GR_textView1.getText().toString();
        Intent datareq = new Intent();
        //
        // to_return_to_the_orginal_activity_this_is_used_to_hold_the_results
        Note newNote = new Note(GR_titleEntered1, GR_textEntered1, new Date());
        datareq.putExtra("NEW_NOTE", newNote);
        navigateBackToMainActivity(datareq);
    }

    private void validateAndSaveNote(boolean backButtonPressed) {
        String GR_titleEntered1 = GR_titleView1.getText().toString();
        String GR_textEntered1 = GR_textView1.getText().toString();
        if (GR_isExistingNote1) {
            // Existing_Note\
            if(isTitleEntered(GR_titleEntered1)){
                if(isTitleChanged(GR_titleEntered1) || isTextChanged(GR_textEntered1)){
                    if(backButtonPressed){
                        // This_is_to_save_when_back_button_is_pressed
                        shawConfirmationDialog(false);
                    }
                    else{
                        // This_is_to_save_when_save_button_is_clicked
                        saveExistingNote();
                    }
                }
                else{
                    // Simply_exit_the_activity_with_out-any_changes_on_the_existing note
                    Toast.makeText(this, "Not_changed_existing_code, exiting_activity_simply.", Toast.LENGTH_LONG).show();
                    simplyExitActivity();
                }
            }
            else{
                // When _title_is_not_entered_simply_exit_the_activity_with_out-saving_and_show_Toast_message
                Toast.makeText(this, "Not_entered_Title,_exiting_page", Toast.LENGTH_LONG).show();
                simplyExitActivity();
            }

        } else { // New_Note
            if (isTitleEntered(GR_titleEntered1)) {
                if(backButtonPressed){
                    // Pressing_back_button_this_will_save_data
                    shawConfirmationDialog(true);
                }
                else{
                    //When_save_button_is_clicked_this_will_save_data
                    saveNewNote();
                }
            } else {

                // Simply_exit_the_activity_without_saving_when_title_is_not_entered_and_toast_message-is_displayed
                Toast.makeText(this, "Not_entered_Title, page_exiting", Toast.LENGTH_LONG).show();
                simplyExitActivity();
            }
        }

    }

    public void shawConfirmationDialog(final boolean isNewNote){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String titleEntered = GR_titleView1.getText().toString();

        builder.setIcon(R.drawable.outline_save_black_18dp);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(isNewNote){
                    saveNewNote();
                }
                else{
                    saveExistingNote();
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                simplyExitActivity();
            }
        });
        builder.setMessage("Note is not saved!\n please save the note '"+titleEntered+"'?");


        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean isTitleEntered(String GR_titleEntered1){
        if(!TextUtils.isEmpty(GR_titleEntered1)){
            return true;
        }
        return false;
    }

    private boolean isTitleChanged(String GR_titleEntered1){
        if (!TextUtils.isEmpty(GR_titleEntered1) &&
                !TextUtils.isEmpty(GR_existingTitleValue1) &&
                !TextUtils.equals(GR_titleEntered1,GR_existingTitleValue1)){
            return true;
        }
        return false;
    }

    private boolean isTextChanged(String GR_textEntered1){
        if (GR_textEntered1 == null) {
            GR_textEntered1 = "";
        }
        if (GR_existingTextValue1 == null) {
            GR_existingTextValue1 = "";
        }
        if (!TextUtils.equals(GR_textEntered1,GR_existingTextValue1)){
            return true;
        }
        return false;
    }

}
