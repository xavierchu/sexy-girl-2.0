package org.xchu.sexy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.huewu.pla.lib.MultiColumnListView;
import com.huewu.pla.lib.extra.MultiColumnPullToRefreshListView;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.meepo.sexygirl.Constants;
import org.meepo.sexygirl.Image;
import org.meepo.sexygirl.ImageUrlsFinder;
import org.meepo.sexygirl.R;
import org.xchu.sexy.adapter.WaterfallAdapter;

import java.util.ArrayList;

public class MainActivity extends Activity {

    ArrayList<Image> images = new ArrayList<Image>();
    int position = 0;

    private MultiColumnListView waterfallView;//可以把它当成一个listView
    //如果不想用下拉刷新这个特性，只是瀑布流，可以用这个：MultiColumnListView 一样的用法

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Bundle bundle = getIntent().getExtras();

        position = bundle.getInt(Constants.Extra.IMAGE_POSITION, 0);
        // 加载图片URL列表
        new ThumbTask(new Callback() {
            @Override
            public void doInFinished(Object o) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initialize();
                    }
                });
            }
        }).execute();

    }

    private void initialize() {

        //初始化图片加载库
        DisplayImageOptions defaultOptions =
                new DisplayImageOptions.Builder()
                        .cacheOnDisc(true)//图片存本地
                        .cacheInMemory(true)
                        .displayer(new FadeInBitmapDisplayer(50))
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        .imageScaleType(ImageScaleType.EXACTLY) // default
                        .build();
        ImageLoaderConfiguration config =
                new ImageLoaderConfiguration.Builder(getApplicationContext())
                        .memoryCache(new UsingFreqLimitedMemoryCache(16 * 1024 * 1024))
                        .defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config);

        waterfallView = (MultiColumnListView) findViewById(R.id.list);

        WaterfallAdapter adapter = new WaterfallAdapter(images, MainActivity.this, getWindowManager().getDefaultDisplay());
        waterfallView.setAdapter(adapter);
//        waterfallView.setOnRefreshListener(RefreshListener());

//        TextView mainTitle = (TextView) findViewById(R.id.mainTitle);
//        mainTitle.setText(new SimpleDateFormat("MM月dd日").format(new Date()) + "最新图片");
    }


//    private MultiColumnPullToRefreshListView.OnRefreshListener RefreshListener() {
//        return new MultiColumnPullToRefreshListView.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                new ThumbTask(new Callback() {
//                    @Override
//                    public void doInFinished(Object o) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                waterfallView.onRefreshComplete();
//                            }
//                        });
//                    }
//                }).execute();
//            }
//        };
//    }

    class ThumbTask extends AsyncTask<String, Void, Void> {

        Callback callback;

        public ThumbTask() {
        }

        public ThumbTask(Callback callback) {
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(String... params) {
            JSONArray imagesJsons = ImageUrlsFinder.findImages(position);
            images.clear();
            for (int i = 0, len = imagesJsons.length(); i < len; i++) {
                try {
                    images.add(Image.toImage(imagesJsons.getJSONObject(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
//            Collections.sort(images, new Comparator<Image>() {
//                @Override
//                public int compare(Image image, Image image2) {
//                    return image.getPn().compareTo(image2.getPn());
//                }
//            });
            if (null != callback) {
                callback.doInFinished(Void.TYPE);
            }
            return null;
        }
    }

    interface Callback<T> {
        void doInFinished(T t);
    }

}
