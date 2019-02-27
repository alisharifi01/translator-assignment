package com.dorsa.translator.util;

public class FileNameUtil {

    public static String eliminateExtension(String fileName) {
        if(!fileName.contains("."))
            return fileName;
        return fileName.substring(0,fileName.lastIndexOf('.'));
    }
}
