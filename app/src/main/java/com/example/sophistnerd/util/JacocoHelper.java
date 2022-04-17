package com.example.sophistnerd.util;

import android.os.Environment;
import android.util.Log;

import com.example.sophistnerd.SophistApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;

public class JacocoHelper {

    private static final String TAG = "JacocoHelper";
    private static JacocoHelper INSTANCE;
    public static JacocoHelper getInstance() {
        if (INSTANCE == null) {
            synchronized (JacocoHelper.class) {
                if(INSTANCE == null) {
                    INSTANCE = new JacocoHelper();
                }
            }
        }
        return INSTANCE;
    }

    private Class rtClzz;
    private Object agent;
    private JacocoHelper() {
        try {
            rtClzz = Class.forName("org.jacoco.agent.rt.RT");
            agent = rtClzz.getMethod("getAgent").invoke(null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成ec文件
     *
     * @param isNew 是否重新创建ec文件
     */
    public void generateEcFile(boolean isNew) {
        OutputStream out = null;
        File mCoverageFilePath = new File(SophistApplication.sContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "coverage.ec");
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

    /**
     * 导出覆盖率数据
     *
     * @return
     */
    public byte[] outputJacocoCoverage() {
        byte[] coverageBytes = new byte[1];
        if (agent != null) {
            try {
                coverageBytes = (byte[]) agent.getClass()
                        .getMethod("getExecutionData", boolean.class)
                        .invoke(agent, false);

                reset();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return coverageBytes;
    }

    /**
     * 重置jacoco数据，jacoco数据是累计收集的
     */
    public void reset() {
        if (agent != null) {
            try {
                agent.getClass()
                        .getMethod("reset")
                        .invoke(agent);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

}
