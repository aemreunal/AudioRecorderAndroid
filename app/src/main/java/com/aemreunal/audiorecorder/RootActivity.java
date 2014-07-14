package com.aemreunal.audiorecorder;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;


public class RootActivity extends Activity {
    public static final int AUDIO_ENCODING_BIT_RATE = 128000;
    public static final int AUDIO_SAMPLING_RATE = 16000;
    public static final int TIMER_UPDATE_FREQUENCY_MS = 200;

    private Button recordButton;
    private EditText durationTextField;
    private EditText jobNameTextField;

    private MediaRecorder recorder;
    private String outputFilePath;
    private File outputFile;

    private Timer timer;
    private long durationInMS;
    private long elapsedTime;

    private boolean isRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);
        recordButton = (Button) findViewById(R.id.recordButton);
        durationTextField = (EditText) findViewById(R.id.durationEditText);
        jobNameTextField = (EditText) findViewById(R.id.jobNameEditText);
    }

    private void createMediaRecorder() {
        setOutputFilePath();
        createOutputFile();
        recorder = new MediaRecorder();
        setRecorderSettings();
    }

    private void createTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                elapsedTime += TIMER_UPDATE_FREQUENCY_MS;
                System.out.println("Timer fired! Current time: " + elapsedTime);
                if (elapsedTime >= durationInMS) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            stopRecording();
                            timer.cancel();
                        }
                    });
                    System.out.println("Stopping timer.");
                }
            }
        }, TIMER_UPDATE_FREQUENCY_MS, TIMER_UPDATE_FREQUENCY_MS);
    }

    private void setOutputFilePath() {
        Calendar calendar = Calendar.getInstance();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Environment.getExternalStorageDirectory().getAbsolutePath());
        stringBuilder.append("/");

        String jobName = jobNameTextField.getText().toString();
        if (jobName == null || jobName == "" || jobName.length() == 0) {
            jobName = "job";
        }
        stringBuilder.append(jobName);
        stringBuilder.append("-android-");

        stringBuilder.append(calendar.get(Calendar.YEAR));
        stringBuilder.append("-");
        stringBuilder.append(calendar.get(Calendar.MONTH));
        stringBuilder.append("-");
        stringBuilder.append(calendar.get(Calendar.DAY_OF_MONTH));
        stringBuilder.append("-");
        stringBuilder.append(calendar.get(Calendar.HOUR));
        stringBuilder.append("-");
        stringBuilder.append(calendar.get(Calendar.MINUTE));
        stringBuilder.append("-");
        stringBuilder.append(calendar.get(Calendar.SECOND));

        stringBuilder.append(".mp4");

        outputFilePath = stringBuilder.toString();
        System.out.println("Output file path set to: " + outputFilePath);
    }

    private void createOutputFile() {
        outputFile = new File(outputFilePath);
        if (outputFile.exists()) {
            System.out.println("Output file exists, deleting it.");
            outputFile.delete();
        }
    }

    private void setRecorderSettings() {
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setAudioEncodingBitRate(AUDIO_ENCODING_BIT_RATE);
        recorder.setAudioSamplingRate(AUDIO_SAMPLING_RATE);
        recorder.setOutputFile(outputFilePath);
        recorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
            @Override
            public void onInfo(MediaRecorder mr, int what, int extra) {
                Toast.makeText(RootActivity.this, "File saved as: " + outputFilePath, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void recordButtonTapped(View view) {
        // Check duration
        durationInMS = 5000;

        if (!isRecording) {
            createMediaRecorder();
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void startRecording() {
        try {
            recorder.prepare();
            recorder.start();
            recordButton.setText(R.string.stopRecording);
            isRecording = true;
            createTimer();
        } catch (IllegalStateException e) {
            System.err.println("Unable to prepare recorder! IllegalState Exception");
        } catch (IOException e) {
            System.err.println("Unable to prepare recorder! IO Exception");
            System.out.println(e.getMessage());
        }
    }

    private void stopRecording() {
        recorder.stop();
        recordButton.setText(R.string.startRecording);
    }
}
