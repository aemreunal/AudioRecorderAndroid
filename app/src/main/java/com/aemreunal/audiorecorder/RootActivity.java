package com.aemreunal.audiorecorder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aemreunal.audiorecorder.model.Recorder;
import com.aemreunal.audiorecorder.model.RecorderController;


public class RootActivity extends Activity implements RecorderController {
    public static final int REQUEST_CODE_RECORDER_VIEW = 12345601;
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
        /*
        if (recorder == null) {
            createRecorder();
            if(durationInMS == -1) {
                showDurationError();
                return;
            }
            durationTextField.setEnabled(false);
            jobNameTextField.setEnabled(false);
        }

        if (!recorder.isRecording()) {
            recorder.startRecording();
            switchToRecordingState();
        } else {
            recorder.stopRecording();
            switchToReadyToRecordState();
        }
        */
        Intent intent = new Intent(this, RecorderActivity.class);
        startActivityForResult(intent, REQUEST_CODE_RECORDER_VIEW);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("Result.");
        switch (requestCode) {
            case REQUEST_CODE_RECORDER_VIEW:
                if (resultCode == RESULT_OK) {
                    // This means our Activity returned successfully. For now, Toast this text.
                    String displayedMessage = data.getStringExtra("PATH");
                    Toast.makeText(this, "Result OK! Displayed message: " + displayedMessage, Toast.LENGTH_SHORT).show();
                } else if (resultCode == RESULT_CANCELED) {
                    //Write your code if there's no result
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void createRecorder() {
        // Check duration
        durationInMS = -1;
        String durationInMSAsString = durationTextField.getText().toString();
        if (durationInMSAsString != null && durationInMSAsString != "" && durationInMSAsString.length() != 0) {
            int duration = Integer.parseInt(durationInMSAsString);
            if(duration >= 1 && duration <= 180) {
                durationInMS = duration * 1000; // * 1000 to convert from seconds to ms
            }
        }

        if(durationInMS == -1) {
            return;
        }

        // Check job name
        jobName = jobNameTextField.getText().toString();
        if (jobName == null || jobName == "" || jobName.length() == 0) {
            jobName = "job";
        }

        recorder = new Recorder(this, jobName, durationInMS);
    }

    private void showDurationError() {
        Toast.makeText(this, "Please enter a valid duration value between 1 and 180.", Toast.LENGTH_LONG).show();
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
