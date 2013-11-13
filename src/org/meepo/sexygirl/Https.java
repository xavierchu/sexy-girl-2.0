package org.meepo.sexygirl;

import android.util.Log;

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

/**
 * Created by xavierchu on 13-11-4.
 */
public class Https {

    static  HttpClient httpclient = new DefaultHttpClient();

    public static void get() {
        String url = "http://image.baidu.com/channel/listjson?fr=channel&tag1=%E7%BE%8E%E5%A5%B3&tag2=%E5%AE%85%E7%94%B7%E5%A5%B3%E7%A5%9E&sorttype=0&pn=150&rn=60&ie=utf8&oe=utf-8&1383572024874";
        JSONArray array = findUrls(url);
        for(int i = 0, len = array.length(); i < len; i++) {
            try {
                Log.v("response text", array.getJSONObject(i).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public static JSONArray findUrls(String baseUrl) {
        List<String> urls = new ArrayList<String>();
        HttpGet mget = new HttpGet(baseUrl);
        try {
//            String resp = client.execute(mget, new BasicResponseHandler());
            HttpResponse response = httpclient.execute(mget);
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
