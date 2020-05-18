package com.wen.tools.domain.utils;


import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FileUtils {


    public static void copyFileUsingFileChannels(File source, File dest) throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            inputChannel.close();
            outputChannel.close();
        }
    }
    public static void copyFileUsingFileStreams(File source, File dest)  throws IOException {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(source);
            output = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } finally {
            input.close();
            output.close();
        }
    }
    public static String[] readFileByLine(File file){
        List<String> result=new ArrayList<>();
        try (FileInputStream inputStream = new FileInputStream(file);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream)))
        {
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                result.add(str);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result.toArray(new String[result.size()]);
    }

    public static void clearInfoForFile(String fileName) throws IOException {
        File file = new File(fileName);
        mkdirParentPath(file);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write("");
        fileWriter.flush();
        fileWriter.close();
    }


    public static void writeToFile(String fileName, String content) throws IOException {
        File file = new File(fileName);
        mkdirParentPath(file);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(content);
        fileWriter.flush();
        fileWriter.close();
    }

    public static void removeDir(String dir) {
        File file = new File(dir);
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File f : files) {
                    removeDir(f.getPath());
                }
            }
            file.delete();
        }
    }

    public static void createDir(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static void mkdirParentPath(File file) throws IOException {
        if (file.isDirectory()) {
            file.mkdirs();
        } else {
            file.getParentFile().mkdirs();
        }
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

    public static String[] getFilePath(String app, String type) {
        Calendar instance = Calendar.getInstance();
        int year = instance.get(Calendar.YEAR);
        int month = instance.get(Calendar.MONTH) + 1;
        int day = instance.get(Calendar.DATE);
        int hour = instance.get(Calendar.HOUR);
        int minute = instance.get(Calendar.MINUTE);
        int second = instance.get(Calendar.SECOND);
        String sepa = File.separator;
        String path = System.getProperty("java.io.tmpdir") + app + sepa + year + sepa + month + sepa + day + sepa;
        File files = new File(path);
        if (!files.exists()) {
            try {
                files.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String[] str = new String[2];
        String fileName = app + hour + "" + minute + "" + second + "" + (int) (Math.random() * 9000 + 1000) + "." + type;
        str[0] = fileName;
        str[1] = path;

        return str;
    }
}
