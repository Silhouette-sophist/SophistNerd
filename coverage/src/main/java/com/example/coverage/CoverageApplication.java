package com.example.coverage;

import android.app.Application;

public class CoverageApplication extends Application {

    private static Application sApplication;

    @Override
    public void onCreate() {
        super.onCreate();

        sApplication = this;
    }

    public static Application getsApplication() {
        return sApplication;
    }
}
