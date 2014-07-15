package com.aemreunal.audiorecorder;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.aemreunal.audiorecorder.model.Recorder;
import com.aemreunal.audiorecorder.model.RecorderActivityController;
import com.aemreunal.audiorecorder.model.RecorderController;

public class RecorderActivity extends Activity implements RecorderController {
    private Activity parentActivity; // Must implement the RecorderActivityController interface

    private Recorder recorder;
    private long durationInMS;
    private String jobName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder);
        setActionBarProperties();
        getExtras();
    }

    private void setActionBarProperties() {
        ActionBar actionBar = getActionBar();
        if(actionBar == null) {
            return;
        }
        getActionBar().setDisplayHomeAsUpEnabled(false);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayUseLogoEnabled(false);
        getActionBar().setDisplayShowHomeEnabled(false);
    }

    private void getExtras() {
        parentActivity = getParent();
        if (!RecorderActivityController.class.isAssignableFrom(parentActivity.getClass())) {
            throw new IllegalArgumentException("Parent activity hasn't implemented RecorderActivityController interface!");
        }
        Intent requestIntent = getIntent();
        durationInMS = requestIntent.getLongExtra(RecorderActivityController.DURATION_LONG_EXTRA_KEY, RecorderActivityController.DURATION_LONG_DEFAULT_VALUE);
        jobName = requestIntent.getStringExtra(RecorderActivityController.JOB_NAME_STRING_EXTRA_KEY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recorder, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void switchToReadyToRecordState() {

    }

    @Override
    public void switchToRecordingState() {

    }

    @Override
    public void updateTimerDisplay(long remainingTime) {

    }

    @Override
    public void saveRecording(String filePath) {

    }

    public void doneButtonTapped(View view) {
        Intent doneIntent = new Intent();
        doneIntent.putExtra(RecorderActivityController.RECORDING_PATH_STRING_EXTRA_KEY, "<Insert file path here>");
        setResult(RESULT_OK, doneIntent);
        finish();
    }

    public void cancelButtonTapped(MenuItem item) {
        Intent returnIntent = new Intent();
        setResult(RESULT_CANCELED, returnIntent);
        finish();
    }
}
