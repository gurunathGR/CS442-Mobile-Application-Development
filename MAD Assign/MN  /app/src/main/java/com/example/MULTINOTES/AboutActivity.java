package com.example.MULTINOTES;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    @Override
    public void onBackPressed() {

        // the current activity closes by pressing the back arrrow  and orginal activity is returned

        Intent datareq = new Intent();
        setResult(RESULT_OK, datareq);
        super.onBackPressed();
    }
}
