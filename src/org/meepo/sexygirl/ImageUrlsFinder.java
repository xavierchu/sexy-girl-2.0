package org.meepo.sexygirl;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ImageUrlsFinder {

    static HttpClient client = new DefaultHttpClient();

    public static JSONArray findImages(int position, int size) {
        String url = "http://image.baidu.com/channel/listjson?fr=channe&sorttype=1&pn=0&rn=" + size + "&ie=utf8&oe=utf-8&1383572024874l&tag1=%E7%BE%8E%E5%A5%B3&tag2=";

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
        } else if(position == 3) {
            // 唯美
            url = url + "%E5%94%AF%E7%BE%8E";
        } else if(position == 4) {
            // 美臀
            url = url + "%E7%BE%8E%E8%87%80";
        } else if(position == 5) {
            // 日本
            url = url + "%E6%97%A5%E6%9C%AC";
        }

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
