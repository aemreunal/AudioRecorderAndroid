package com.aemreunal.audiorecorder.model;

/*
 * This code belongs to:
 * Ahmet Emre Unal
 * S001974
 * emre.unal@ozu.edu.tr
 */

public interface RecorderController {

    public void switchToReadyToRecordState();

    public void switchToRecordingState();

    public void switchToReadyToListenAndSubmitState();

    public void switchToListeningState();

    public void updateTimerDisplay(long remainingTime);

    public void onRecordingFinished(String filePath);

    public void onPlayingFinished();
}
