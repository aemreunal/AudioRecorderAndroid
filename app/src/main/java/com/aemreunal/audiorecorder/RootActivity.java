package com.aemreunal.audiorecorder;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;


public class RootActivity extends Activity {
    private static final int DURATION = 5;
    private MediaRecorder recorder;
    private String outputFilePath;
    private File outputFile;
    private Button recordButton;
    private boolean isRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);
        recordButton = (Button) findViewById(R.id.recordButton);
        setOutputFilePath();
        createMediaRecorder();
    }

    private void setOutputFilePath() {
        outputFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/test.mp4";
        outputFile = new File(outputFilePath);
        if (outputFile.exists()) {
            System.out.println("Output file exists, deleting it.");
            outputFile.delete();
        }
    }

    private void createMediaRecorder() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
//        recorder.setMaxDuration(DURATION);
        recorder.setOutputFile(outputFilePath);

        recorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
            @Override
            public void onInfo(MediaRecorder mr, int what, int extra) {
                Toast.makeText(RootActivity.this, "File saved as: " + outputFilePath, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void recordButtonTapped(View view) {
        startRecording();
    }

    private void startRecording() {
        if (isRecording) {
            stopRecording();
        }

        try {
            setOutputFilePath();
            recorder.prepare();
            recorder.start();
            recordButton.setText(R.string.stopRecording);
            isRecording = true;
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
