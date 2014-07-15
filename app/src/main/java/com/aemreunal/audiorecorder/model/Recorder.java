package com.aemreunal.audiorecorder.model;

/*
 * This code belongs to:
 * Ahmet Emre Unal
 */

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Recorder {
    public static final int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;
    public static final int AUDIO_ENCODER = MediaRecorder.AudioEncoder.AAC;
    public static final int OUTPUT_FORMAT = MediaRecorder.OutputFormat.MPEG_4;
    public static final int AUDIO_ENCODING_BIT_RATE = 128000;
    public static final int AUDIO_SAMPLING_RATE = 44100;
    public static final int TIMER_UPDATE_FREQUENCY_MS = 100;

    private String name;
    private long durationInMS;

    private MediaRecorder mediaRecorder;
    private String outputFilePath;
    private File outputFile;
    private MediaPlayer mediaPlayer;

    private Timer timer;
    private long elapsedTime;

    private boolean isRecording = false;
    private boolean isPlaying = false;

    private Activity parentActivity; // Must implement RecorderController interface

    public Recorder(Activity parentActivity, String name, long durationInMS) {
        if (!RecorderController.class.isAssignableFrom(parentActivity.getClass())) {
            throw new IllegalArgumentException("Parent activity hasn't implemented RecorderController interface!");
        }
        this.parentActivity = parentActivity;
        this.name = name;
        this.durationInMS = durationInMS;
        if (durationInMS < 1) {
            throw new IllegalArgumentException("Invalid recording duration argument provided!");
        }
        this.mediaRecorder = new MediaRecorder();
        this.mediaPlayer = new MediaPlayer();
    }

    public void startRecording() {
        initMediaRecorder();
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording = true;
            createTimer();
        } catch (IllegalStateException e) {
            System.err.println("Unable to prepare mediaRecorder! IllegalState Exception");
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println("Unable to prepare mediaRecorder! IO Exception");
            System.err.println(e.getMessage());
        }
    }

    public void stopRecording() {
        if (timer != null) {
            timer.cancel();
        }
        if (isRecording) {
            mediaRecorder.stop();
            mediaRecorder.reset();
            outputFile.delete();
            isRecording = false;
        }
    }

    public void startPlaying() {
        initMediaPlayer();
        try {
            mediaPlayer.prepare();
            mediaPlayer.start();
            isPlaying = true;
        } catch (IllegalStateException e) {
            showError(e);
        } catch (IOException e) {
            showError(e);
        }
    }

    public void stopPlaying() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.reset();
    }

    public void stop() {
        stopPlaying();
        stopRecording();
        if (outputFile != null && outputFile.exists()) {
            outputFile.delete();
        }
    }

    private void onRecordingFinished() {
        isRecording = false;
        stopRecording();
        destroyMediaRecorder();
        initMediaPlayer();
        ((RecorderController) parentActivity).onRecordingFinished(outputFilePath);
    }

    private void onPlayingFinished() {
        isPlaying = false;
        stopPlaying();
        ((RecorderController) parentActivity).onPlayingFinished();
    }

    private void initMediaRecorder() {
        createOutputFile();
        setRecorderSettings();
    }

    private void createOutputFile() {
        outputFilePath = RecorderFileNameHelper.getOutputFilePath(name);
        outputFile = new File(outputFilePath);
        if (outputFile.exists()) {
            System.out.println("Output file exists, deleting it.");
            outputFile.delete();
        }
    }

    private void setRecorderSettings() {
        mediaRecorder.setAudioSource(AUDIO_SOURCE);
        mediaRecorder.setOutputFormat(OUTPUT_FORMAT);
        mediaRecorder.setAudioEncoder(AUDIO_ENCODER);
        mediaRecorder.setAudioEncodingBitRate(AUDIO_ENCODING_BIT_RATE);
        mediaRecorder.setAudioSamplingRate(AUDIO_SAMPLING_RATE);
        mediaRecorder.setOutputFile(outputFilePath);
        mediaRecorder.setMaxDuration((int) durationInMS);
        mediaRecorder.setOnInfoListener(new RecorderInfoListener());
    }

    private void createTimer() {
        elapsedTime = 0;
        timer = new Timer();
        timer.scheduleAtFixedRate(new UpdateTimerDisplayTask(), TIMER_UPDATE_FREQUENCY_MS, TIMER_UPDATE_FREQUENCY_MS);
    }

    private void initMediaPlayer() {
        mediaPlayer.setOnCompletionListener(new PlayerInfoListener());
        try {
            mediaPlayer.setDataSource(outputFilePath);
        } catch (IOException e) {
            showError(e);
        }
    }

    private void destroyMediaRecorder() {
        mediaRecorder.stop();
        mediaRecorder.reset();
        mediaRecorder.release();
        mediaRecorder = null;
    }

    private void showError(Exception e) {
        System.err.println("Unable to prepare MediaPlayer! An exception occurred.");
        System.err.println(e.getMessage());
        e.printStackTrace();
        Toast.makeText(parentActivity, "Unable to play recording, an exception occurred.", Toast.LENGTH_LONG).show();
    }

    public boolean isRecording() {
        return isRecording;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    private class RecorderInfoListener implements MediaRecorder.OnInfoListener {
        @Override
        public void onInfo(MediaRecorder mr, int what, int extra) {
            if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                onRecordingFinished();
            }
        }
    }

    private class UpdateTimerDisplayTask extends TimerTask {
        @Override
        public void run() {
            elapsedTime += TIMER_UPDATE_FREQUENCY_MS;
            ((RecorderController) parentActivity).updateTimerDisplay(durationInMS - elapsedTime);
            if (elapsedTime >= durationInMS) {
                timer.cancel();
            }
        }
    }

    private class PlayerInfoListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            onPlayingFinished();
        }
    }
}
