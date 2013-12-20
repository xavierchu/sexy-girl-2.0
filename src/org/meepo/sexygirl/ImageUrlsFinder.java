package org.meepo.sexygirl;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ImageUrlsFinder {

    public static final String WEB_ADDR = "http://www.22mm.cc";

    private static final String BASE_URL = "http://www.22mm.cc/mm/";

    static HttpClient client = new DefaultHttpClient();

    public enum IMAGETYPE {
        QINGLIANG, XINGAN, WENYI, SUREN;

        public IMAGETYPE getValue(String str) {
            if ("QINGLIANG".equals(str)) {
                return QINGLIANG;
            } else if ("XINGAN".equals(str)) {
                return XINGAN;
            } else if ("WENYI".equals(str)) {
                return WENYI;
            } else if ("SUREN".equals(str)) {
                return SUREN;
            } else {
                return QINGLIANG;
            }
        }

        public String getStr(IMAGETYPE type) {
            switch (type) {
                case QINGLIANG:
                    return "QINGLIANG";
                case XINGAN:
                    return "XINGAN";
                case WENYI:
                    return "WENYI";
                case SUREN:
                    return "SUREN";
                default:
                    return "QINGLIANG";
            }
        }
    }

    public static List<String> findUrls(IMAGETYPE imagetype) {
        switch (imagetype) {
            case QINGLIANG:
                return findUrls(BASE_URL + "qingliang/");
            case XINGAN:
                return findUrls(BASE_URL + "jingyan/");
            case WENYI:
                return findUrls(BASE_URL + "bagua/");
            case SUREN:
                return findUrls(BASE_URL + "suren/");
            default:
                return null;
        }
    }

    public static List<String> findUrls(IMAGETYPE imagetype, android.content.res.AssetManager assets) throws IOException {
        switch (imagetype) {
            case QINGLIANG:
                return getFromAssets("urls/qingliang.txt", assets);
            case XINGAN:
                return getFromAssets("urls/jingyan.txt", assets);
            case WENYI:
                return getFromAssets("urls/bagua.txt", assets);
            case SUREN:
                return getFromAssets("urls/suren.txt", assets);
            default:
                return null;
        }
    }

    public static List<String> getFromAssets(String fileName, android.content.res.AssetManager assets) throws IOException {
        InputStreamReader inputReader = new InputStreamReader(assets.open(fileName));
        BufferedReader bufReader = new BufferedReader(inputReader);
        String result = "";
        String line;
        HashSet<String> list = new HashSet<String>();
        while ((line = bufReader.readLine()) != null) {
            result += line + "\n";
            list.add(line);
        }
        Log.i("sexy", result);
        return new ArrayList<String>(list);
    }


    public static List<String> findUrls(String baseUrl) {
        List<String> urls = new ArrayList<String>();
        HttpGet mget = new HttpGet(baseUrl);
        try {
//            String resp = client.execute(mget, new BasicResponseHandler());
            HttpResponse response = client.execute(mget);
            if (response.getStatusLine().getStatusCode() != 200) {
                return urls;
            }
            String resp = EntityUtils.toString(response.getEntity(), "utf-8");
            Matcher matcher = Pattern.compile(
                    "<a href=\"([^\"]+)\" title=\"([^\"]+)\"").matcher(resp);
            while (matcher.find()) {
                if (matcher.groupCount() != 2) {
                    continue;
                }
                urls.addAll(findSeriesUrls(matcher.group(2), WEB_ADDR + matcher.group(1)));
            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return urls;
    }

    public static List<String> findSeriesUrls(String title, String url) {
        List<String> urls = new ArrayList<String>();
        for (int i = 1; i <= 20; i++) {
            String sUrl;
            int j = 0;
            if (i == 1) {
                sUrl = url;
            } else {
                sUrl = url.substring(0, url.length() - 5) + "-" + i + ".html";
            }
            HttpGet mget = new HttpGet(sUrl);
            try {
                String resp = client.execute(mget, new BasicResponseHandler());
                Matcher matcher = Pattern.compile("arrayImg\\[\\d+\\]=\"([^\"]+)\"")
                        .matcher(resp);
                while (matcher.find()) {
                    if (matcher.groupCount() != 1) {
                        continue;
                    }
                    String imgUrl = matcher.group(1).replaceAll("\\/big\\/", "/pic/");
                    System.out.println("----- " + imgUrl);
                    urls.add(imgUrl);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }

    public static JSONArray findImages(int position) {
        String url = "http://image.baidu.com/channel/listjson?fr=channe&sorttype=1&pn=0&rn=300&ie=utf8&oe=utf-8&1383572024874l&tag1=%E7%BE%8E%E5%A5%B3&tag2=";

        if(position == 0) {
            // 宅男女神
           // url = "http://image.baidu.com/channel/listjson?fr=channel&tag1=%E7%BE%8E%E5%A5%B3&tag2=%E5%AE%85%E7%94%B7%E5%A5%B3%E7%A5%9E&sorttype=1&pn=0&rn=300&ie=utf8&oe=utf-8&1383572024874";
            url = url + "%E5%AE%85%E7%94%B7%E5%A5%B3%E7%A5%9E";
        } else if(position == 1) {
            // 网络美女
            url = url + "%E7%BD%91%E7%BB%9C%E7%BE%8E%E5%A5%B3";
        } else if(position == 2) {
            // 性感
            url = url + "%E6%80%A7%E6%84%9F";
        } else {
            // 唯美
            url = url + "%E5%94%AF%E7%BE%8E";
        }
        Log.d("url+++", url);


        return findImages(url);
    }
    public static JSONArray findImages(String baseUrl) {
        List<String> urls = new ArrayList<String>();
        HttpGet mget = new HttpGet(baseUrl);
        try {
//            String resp = client.execute(mget, new BasicResponseHandler());
            HttpResponse response = client.execute(mget);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("invalid url");
            }
            String resp = EntityUtils.toString(response.getEntity(), "utf-8");
            JSONTokener parser = new JSONTokener(resp);
            JSONObject data = (JSONObject) parser.nextValue();
            return data.getJSONArray("data");
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
