package com.mycompany.advertising.components;

import java.io.File;
import java.nio.file.Path;

/**
 * Created by Amir on 7/31/2020.
 */
public class FileManager {
    public static String getNextFileName(String filename, Path folder) {
        String tempfilename = getFileNameWithNoExtention(filename);
        String extention = getExtention(filename);
        int index = 0;
        File file = new File(folder.toString() + "\\" + filename);
        while (file.exists()) {
            filename = tempfilename + "(" + index + ")." + extention;
            index++;
            file = new File(filename);
        }
        return filename;
    }

    public static String getExtention(String filename) {
        int index = filename.lastIndexOf('.');
        if (index > 0) return filename.substring(index + 1, filename.length());
        else return null;
    }

    public static String getFileNameWithNoExtention(String filename) {
        int index = filename.lastIndexOf('.');
        if (index > 0) return filename.substring(0, index);
        else return filename;
    }
}
