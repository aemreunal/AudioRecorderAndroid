package com.aemreunal.audiorecorder.model;

/*
 * This code belongs to:
 * Ahmet Emre Unal
 * S001974
 * emre.unal@ozu.edu.tr
 */

import android.os.Environment;

import java.util.Calendar;

public class RecorderFileNameHelper {

    public static String getOutputFilePath(String jobName) {
        Calendar calendar = Calendar.getInstance();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(Environment.getExternalStorageDirectory().getAbsolutePath());
        stringBuilder.append("/");

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

        return stringBuilder.toString();
    }
}
