package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class FileUtil {

    private static void saveContent(String str, String filePath) {


        try {
            byte[] b = str.getBytes();
            //"/home/log/log.txt"
            File file = new File("/log/" + filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(new Date().toString().getBytes());
            for (int i = 0; i < 5; i++) {
                fos.write(b);
            }
            fos.flush();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        saveContent("测试来了","ttttt");

    }
}
