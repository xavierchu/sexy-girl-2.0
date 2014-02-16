package org.xchu.sexy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.huewu.pla.lib.MultiColumnListView;
import com.huewu.pla.lib.extra.MultiColumnPullToRefreshListView;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.meepo.sexygirl.Constants;
import org.meepo.sexygirl.Image;
import org.meepo.sexygirl.ImageUrlsFinder;
import org.meepo.sexygirl.R;
import org.xchu.sexy.adapter.WaterfallAdapter;
import org.xchu.sexy.utils.NetworkUtils;

import java.io.File;
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

        File cacheDir = StorageUtils.getCacheDirectory(this);
        ImageLoaderConfiguration config =
                new ImageLoaderConfiguration.Builder(getApplicationContext())
                        .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                        .discCacheExtraOptions(480, 800, Bitmap.CompressFormat.JPEG, 75, null)
                        .threadPriority(Thread.NORM_PRIORITY - 1) // default
                        .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                        .denyCacheImageMultipleSizesInMemory()
                        .memoryCache(new UsingFreqLimitedMemoryCache(16 * 1024 * 1024))
                        .memoryCacheSize(16 * 1024 * 1024)
                        .memoryCacheSizePercentage(13) // default
                        .discCache(new UnlimitedDiscCache(cacheDir)) // default
                        .discCacheSize(50 * 1024 * 1024)
                        .discCacheFileCount(3000)
                        .discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                        .imageDownloader(new BaseImageDownloader(this)) // default
                        .writeDebugLogs()
                        .defaultDisplayImageOptions(defaultOptions).build();

        ImageLoader.getInstance().init(config);

        waterfallView = (MultiColumnListView) findViewById(R.id.list);

        WaterfallAdapter adapter = new WaterfallAdapter(images, MainActivity.this, getWindowManager().getDefaultDisplay());
        waterfallView.setAdapter(adapter);
//        waterfallView.setOnRefreshListener(RefreshListener());

//        TextView mainTitle = (TextView) findViewById(R.id.mainTitle);
//        mainTitle.setText(new SimpleDateFormat("MM月dd日").format(new Date()) + "最新图片");
    }

    class ThumbTask extends AsyncTask<String, Void, Void> {

        Callback callback;

        public ThumbTask() {
        }

        public ThumbTask(Callback callback) {
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(String... params) {
            JSONArray imagesJsons;
            if (NetworkUtils.isWifiConnected(MainActivity.this)) {
                toast("系统正在努力为您加载最新的美女图片，请稍后。");
                imagesJsons = ImageUrlsFinder.findImages(position, 300);
            } else {
                toast("您当前使用的非WIFI网络，为了保证您的浏览速度，系统为您精选了最优质的图片，请稍后。");
                imagesJsons = ImageUrlsFinder.findImages(position, 100);
            }
            images.clear();
            for (int i = 0, len = imagesJsons.length(); i < len; i++) {
                try {
                    images.add(Image.toImage(imagesJsons.getJSONObject(i)));
                } catch (JSONException e) {
//                    e.printStackTrace();
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

    private void toast(final String msg) {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    interface Callback<T> {
        void doInFinished(T t);
    }

}
