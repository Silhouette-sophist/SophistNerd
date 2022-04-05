package com.example.coverage.util;

import android.os.Environment;
import android.util.Log;

import com.example.coverage.CoverageApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class JacocoHelper {

    private static final String TAG = "JacocoHelper";

    /**
     * 生成ec文件
     *
     * @param isNew 是否重新创建ec文件
     */
    public static void generateEcFile(boolean isNew) {
        OutputStream out = null;
        File mCoverageFilePath = new File(CoverageApplication.getsApplication().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "coverage.ec");
        try {
            if (isNew && mCoverageFilePath.exists()) {
                Log.d(TAG, "清除旧的ec文件");
                mCoverageFilePath.delete();
            }
            if (!mCoverageFilePath.exists()) {
                mCoverageFilePath.createNewFile();
            }
            out = new FileOutputStream(mCoverageFilePath.getPath(), true);
            Object agent = Class.forName("org.jacoco.agent.rt.RT")
                    .getMethod("getAgent")
                    .invoke(null);
            if (agent != null) {
                out.write((byte[]) agent.getClass().getMethod("getExecutionData", boolean.class)
                        .invoke(agent, false));
            }
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
