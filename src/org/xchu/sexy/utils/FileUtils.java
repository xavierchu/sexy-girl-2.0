package org.xchu.sexy.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by xavierchu on 14-2-16.
 */
public class FileUtils {

    public static String getSDPath() {
        return Environment.getExternalStorageDirectory() + "/";
    }

    //判断文件是否已经存在
    public static boolean isFileExist(String filename) {
        File file = new File(getSDPath() + filename);
        return file.exists();
    }


    public static File createFolderIfNotExist(String filePath, boolean isFolder) {
        String paths[] = filePath.split("\\\\");
        String dir = paths[0];
        int dept = 2;
        if(isFolder) {
            dept = 1;
        }
        for (int i = 0; i < paths.length - dept; i++) {
            try {
                dir = dir + "/" + paths[i + 1];
                File dirFile = new File(dir);
                if (!dirFile.exists()) {
                    dirFile.mkdir();
                    Log.i("SG", "创建目录为：" + dir);
                }
            } catch (Exception err) {
                Log.e("SG", "ELS - Chart : 文件夹创建发生异常");
            }
        }
        return new File(filePath);
    }

    //将一个InputStream里的数据写进SD卡
    public static File write2File(String path, String fileName, InputStream inputstream) throws IOException {
        File file = null;
        OutputStream outputstream = null;
        try {
            File folder = createFolderIfNotExist(path, true);
            file = new File(folder, fileName);
            outputstream = new FileOutputStream(file);
            byte[] buffer = new byte[4 * 1024];
            while (inputstream.read(buffer) != -1) {
                outputstream.write(buffer);
            }
            outputstream.flush();
        } finally {
            try {
                if(null != outputstream) {
                    outputstream.close();
                }
            } catch (Exception e) {
            }
        }
        return file;
    }

}
