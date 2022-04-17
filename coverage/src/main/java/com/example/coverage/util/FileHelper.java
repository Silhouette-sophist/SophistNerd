package com.example.coverage.util;

import android.os.Environment;

import com.example.coverage.CoverageApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileHelper {

    public static String writeCoverage(byte[] coverages) {
        File coverageFilePath = new File(
                CoverageApplication.getsApplication()
                        .getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                TimeHelper.getFormatTime() + "-coverage.ec");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(coverageFilePath);
            fileOutputStream.write(coverages);
            return coverageFilePath.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
