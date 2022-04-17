package com.example.sophistnerd.util;

import android.os.Environment;

import com.example.sophistnerd.SophistApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileHelper {

    public static String writeCoverage(byte[] coverages) {
        File coverageFilePath = new File(
                SophistApplication.sContext
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
