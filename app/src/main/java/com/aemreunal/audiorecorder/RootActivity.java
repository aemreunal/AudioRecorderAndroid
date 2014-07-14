package com.aemreunal.audiorecorder;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aemreunal.audiorecorder.model.Recorder;
import com.aemreunal.audiorecorder.model.RecorderController;


public class RootActivity extends Activity implements RecorderController {
    private Button recordButton;
    private EditText durationTextField;
    private EditText jobNameTextField;

    private Recorder recorder;
    private long durationInMS;
    private String jobName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);
        recordButton = (Button) findViewById(R.id.recordButton);
        durationTextField = (EditText) findViewById(R.id.durationEditText);
        jobNameTextField = (EditText) findViewById(R.id.jobNameEditText);
    }

    public void recordButtonTapped(View view) {
        if (recorder == null) {
            createRecorder();
        }

        if (!recorder.isRecording()) {
            recorder.startRecording();
            switchToRecordingState();
        } else {
            recorder.stopRecording();
            switchToReadyToRecordState();
        }
    }

    private void createRecorder() {
        // Check duration
        String durationInMSAsString = durationTextField.getText().toString();
        if (durationInMSAsString == null || durationInMSAsString == "" || durationInMSAsString.length() == 0) {
            showDurationError();
            durationInMSAsString = getString(R.string.defaultDurationText);
        }
        durationInMS = Integer.parseInt(durationInMSAsString) * 1000; // * 1000 to convert from seconds to ms

        // Check job name
        jobName = jobNameTextField.getText().toString();
        if (jobName == null || jobName == "" || jobName.length() == 0) {
            jobName = "job";
        }

        recorder = new Recorder(this, jobName, durationInMS);
    }

    private void showDurationError() {
        Toast.makeText(this, "Please enter a valid duration value between 1 and 180. Defaulting to 5 seconds.", Toast.LENGTH_LONG).show();
    }

    public void switchToRecordingState() {
        recordButton.setText(R.string.stopRecording);
    }

    public void switchToReadyToRecordState() {
        recordButton.setText(R.string.startRecording);
    }

    public void updateTimerDisplay(long remainingTime) {
        System.out.println("Timer fired! Remaining time: " + remainingTime);
    }

    public void saveRecording(String filePath) {
        // Do something with the file.
        Toast.makeText(this, "File saved as: " + filePath, Toast.LENGTH_LONG).show();
    }
}
