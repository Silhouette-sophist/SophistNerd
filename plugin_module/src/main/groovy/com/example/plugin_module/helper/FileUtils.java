package com.example.plugin_module.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {

    public static void copyDirectory(File directory, File dest) {

        File[] childFiles = directory.listFiles();
        for (File childFile : childFiles) {
            if (childFile.isFile()) {
                copyFile(childFile, new File(dest, childFile.getName()));
            } else {
                File destDirectory = new File(dest, childFile.getName());
                if(!destDirectory.exists()) {
                    destDirectory.mkdir();
                }
                copyDirectory(childFile, destDirectory);
            }
        }
    }

    public static void copyFile(File source, File dest) {
        try {
            FileReader fileReader = new FileReader(source);
            FileWriter fileWriter = new FileWriter(dest);
            char[] buffer = new char[256];
            int readSize = 0;
            while((readSize = fileReader.read(buffer)) > 0) {
                fileWriter.write(buffer, 0, readSize);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
