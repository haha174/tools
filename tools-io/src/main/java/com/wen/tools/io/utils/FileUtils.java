package com.wen.tools.io.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public class FileUtils {
    public static void clearInfoForFile(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write("");
        fileWriter.flush();
        fileWriter.close();
    }

    public static void clearInfoForFile(File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write("");
        fileWriter.flush();
        fileWriter.close();
    }
    public static String[] getFilePath(String app,String type){
        Calendar instance = Calendar.getInstance();
        int year = instance.get(Calendar.YEAR);
        int month = instance.get(Calendar.MONTH)+1;
        int day = instance.get(Calendar.DATE);
        int hour=instance.get(Calendar.HOUR);
        int minute=instance.get(Calendar.MINUTE);
        int second=instance.get(Calendar.SECOND);
        String sepa =File.separator;
        String path=System.getProperty("java.io.tmpdir") + app+sepa+year+sepa+month+sepa+day+sepa;
        File files = new File(path);
        if (!files.exists()) {
            try {
                files.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String[] str=new String[2];
        String fileName=app+hour+""+minute+""+second+""+(int)(Math.random()*9000+1000)+"."+type;
        str[0]=fileName;
        str[1]=path;

        return str;
    }
}
