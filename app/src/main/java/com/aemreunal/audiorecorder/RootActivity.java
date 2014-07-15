package com.aemreunal.audiorecorder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.aemreunal.audiorecorder.model.RecorderActivityController;
import com.aemreunal.audiorecorder.view.RecorderActivity;


public class RootActivity extends Activity implements RecorderActivityController {
    private EditText durationTextField;
    private EditText jobNameTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);
        durationTextField = (EditText) findViewById(R.id.durationEditText);
        jobNameTextField = (EditText) findViewById(R.id.jobNameEditText);
    }

    public void showRecorderActivityButtonTapped(View view) {
        Intent intent = new Intent(this, RecorderActivity.class);
        intent.putExtra(RecorderActivityController.DURATION_LONG_EXTRA_KEY, getDuration());
        intent.putExtra(RecorderActivityController.JOB_NAME_STRING_EXTRA_KEY, getJobName());
        startActivityForResult(intent, RecorderActivityController.REQUEST_CODE_RECORDER_VIEW);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_RECORDER_VIEW:
                if (resultCode == RESULT_OK) {
                    String displayedMessage = data.getStringExtra(RecorderActivityController.RECORDING_PATH_STRING_EXTRA_KEY);
                    doSomethingWithRecording(displayedMessage);
                } else if (resultCode == RESULT_CANCELED) {
                    doRecordingCancelledAction();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void doSomethingWithRecording(String recordingPath) {
        Toast.makeText(this, "Result OK! Path of recording: " + recordingPath, Toast.LENGTH_SHORT).show();
    }

    private void doRecordingCancelledAction() {
        Toast.makeText(this, "Recording cancelled.", Toast.LENGTH_SHORT).show();
    }

    private long getDuration() {
        String durationInMSAsString = durationTextField.getText().toString();
        long durationInMS = -1;

        if (durationInMSAsString != null && durationInMSAsString != "" && durationInMSAsString.length() != 0) {
            int duration = Integer.parseInt(durationInMSAsString);
            if(duration >= 1 && duration <= 180) {
                durationInMS = duration * 1000; // * 1000 to convert from seconds to ms
            }
        }

        if(durationInMS == -1) {
            showDurationError();
        }

        return durationInMS;
    }

    private String getJobName() {
        // Check job name
        String jobName = jobNameTextField.getText().toString();
        if (jobName == null || jobName == "" || jobName.length() == 0) {
            jobName = "job";
        }
        return jobName;
    }

    private void showDurationError() {
        Toast.makeText(this, "Please enter a valid duration value between 1 and 180.", Toast.LENGTH_LONG).show();
    }
}
