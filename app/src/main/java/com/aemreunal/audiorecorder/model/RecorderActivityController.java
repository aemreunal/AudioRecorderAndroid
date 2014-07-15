package com.aemreunal.audiorecorder.model;

/*
 * This code belongs to:
 * Ahmet Emre Unal
 * S001974
 * emre.unal@ozu.edu.tr
 */

public interface RecorderActivityController {
    public static final int REQUEST_CODE_RECORDER_VIEW = 12345601;

    public static final String DURATION_LONG_EXTRA_KEY = "DURATION";
    public static final String JOB_NAME_STRING_EXTRA_KEY = "JOB_NAME";
    public static final String RECORDING_PATH_STRING_EXTRA_KEY = "PATH";

    public static final long DURATION_LONG_DEFAULT_VALUE = -1;
}
