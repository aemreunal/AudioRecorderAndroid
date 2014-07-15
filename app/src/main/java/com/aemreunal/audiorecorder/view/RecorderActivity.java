package com.aemreunal.audiorecorder.view;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aemreunal.audiorecorder.R;
import com.aemreunal.audiorecorder.model.Recorder;
import com.aemreunal.audiorecorder.model.RecorderActivityController;
import com.aemreunal.audiorecorder.model.RecorderController;

public class RecorderActivity extends Activity implements RecorderController {
    private Button recordButton;
    private MenuItem submitButton;
    private ProgressWheel progressWheel;
    private TextView timeLeftCounter;

    private Recorder recorder;
    private long recordingDurationInMS;
    private String jobName;

    private String recordingFilePath;

    private boolean recordingComplete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder);
        getActivityElements();
        setActionBarProperties();
        getExtras();
        recorder = new Recorder(this, jobName, recordingDurationInMS);
        switchToReadyToRecordState();
    }

    private void getActivityElements() {
        recordButton = (Button) findViewById(R.id.recordButton);
        timeLeftCounter = (TextView) findViewById(R.id.timeLeftCounter);
        progressWheel = (ProgressWheel) findViewById(R.id.pw_spinner);
    }

    private void setActionBarProperties() {
        ActionBar actionBar = getActionBar();
        if (actionBar == null) {
            return;
        }
        getActionBar().setDisplayHomeAsUpEnabled(false);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayUseLogoEnabled(false);
        getActionBar().setDisplayShowHomeEnabled(false);
    }

    private void getExtras() {
        Intent requestIntent = getIntent();
        recordingDurationInMS = requestIntent.getLongExtra(RecorderActivityController.DURATION_LONG_EXTRA_KEY, RecorderActivityController.DURATION_LONG_DEFAULT_VALUE);
        jobName = requestIntent.getStringExtra(RecorderActivityController.JOB_NAME_STRING_EXTRA_KEY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recorder, menu);
        getSubmitMenuItem(menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void getSubmitMenuItem(Menu menu) {
        submitButton = menu.findItem(R.id.submitMenuItem);
        submitButton.setEnabled(false);
    }

    public void recordButtonTapped(View view) {
        if (!recordingComplete) {
            if (!recorder.isRecording()) {
                switchToRecordingState();
            } else {
                switchToReadyToRecordState();
            }

        } else {
            if (!recorder.isPlaying()) {
                switchToListeningState();
            } else {
                switchToReadyToListenAndSubmitState();
            }
        }
    }

    @Override
    public void switchToReadyToRecordState() {
        recorder.stopRecording();
        recordButton.setText(R.string.startRecording);
        setTimeDisplay(String.valueOf(((int) recordingDurationInMS) / 1000), 360);
    }

    @Override
    public void switchToRecordingState() {
        recorder.startRecording();
        recordButton.setText(R.string.stopRecording);
    }

    @Override
    public void switchToReadyToListenAndSubmitState() {
        recorder.stopPlaying();
        submitButton.setEnabled(true);
        timeLeftCounter.setText("0");
        recordButton.setText(R.string.startListening);
        setTimeDisplay("0", 360);
    }

    @Override
    public void switchToListeningState() {
        recorder.startPlaying();
        recordButton.setText(R.string.stopListening);
    }

    @Override
    public void updateTimerDisplay(final long remainingTime) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(recorder.isRecording()) {
                    int remainingTimeAsCounter = (((int) remainingTime) / 1000) + 1;
                    double progress = (recordingDurationInMS - remainingTime) / (double) recordingDurationInMS;
                    progress *= 360.0;
                    setTimeDisplay(String.valueOf(remainingTimeAsCounter), (int) progress);
                }
            }
        });
    }

    private void setTimeDisplay(String timeLeft, int progress) {
        timeLeftCounter.setText(timeLeft);
        timeLeftCounter.invalidate();
        progressWheel.setProgress(progress);
        progressWheel.invalidate();
    }

    @Override
    public void onRecordingFinished(String recordingFilePath) {
        this.recordingFilePath = recordingFilePath;
        recordingComplete = true;
        switchToReadyToListenAndSubmitState();
    }

    @Override
    public void onPlayingFinished() {
        switchToReadyToListenAndSubmitState();
    }

    public void cancelButtonTapped(MenuItem item) {
        recorder.stop();
        Intent returnIntent = new Intent();
        setResult(RESULT_CANCELED, returnIntent);
        finish();
    }

    public void submitButtonTapped(MenuItem item) {
        Intent doneIntent = new Intent();
        doneIntent.putExtra(RecorderActivityController.RECORDING_PATH_STRING_EXTRA_KEY, recordingFilePath);
        setResult(RESULT_OK, doneIntent);
        finish();
    }
}
