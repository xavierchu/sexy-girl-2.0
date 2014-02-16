package org.xchu.sexy.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by xavierchu on 14-2-16.
 */
public class HttpUtils {
    public static void downloadFile(String url, String path, String fileName) throws IOException {
        InputStream is = null;
        try {
            HttpURLConnection urlConn = (HttpURLConnection) new URL(url).openConnection();
            is = urlConn.getInputStream();
            FileUtils.write2File(path, fileName, is);
        } finally {
            try {
                if(null != is) {
                    is.close();
                }
            } catch (Exception e) {
            }
        }
    }
}
