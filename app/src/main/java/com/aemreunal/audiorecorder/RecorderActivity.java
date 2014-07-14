package com.aemreunal.audiorecorder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.aemreunal.audiorecorder.model.RecorderController;

public class RecorderActivity extends Activity implements RecorderController {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


    }

    public void doneButtonTapped(View view) {
        Intent doneIntent = new Intent();
        doneIntent.putExtra("PATH", "<Insert file path here>");
        setResult(RESULT_OK, doneIntent);
        finish();

        /*
        // To cancel:

        Intent returnIntent = new Intent();
        setResult(RESULT_CANCELED, returnIntent);
        finish();
         */
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
}
