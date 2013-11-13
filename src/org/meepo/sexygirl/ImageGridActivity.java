/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.meepo.sexygirl;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.meepo.sexygirl.Constants.Extra;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.zhuamob.android.ZhuamobTracking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * http://image.baidu.com/channel/index?image_id=6582361455#%E7%BE%8E%E5%A5%B3&%E5%AE%85%E7%94%B7%E5%A5%B3%E7%A5%9E&0&0
 */
public class ImageGridActivity extends AbsListViewBaseActivity {

	List<String> imageUrls = new ArrayList<String>();

	DisplayImageOptions options;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_image_grid);

        Bundle bundle = getIntent().getExtras();
        ImageUrlsFinder.IMAGETYPE imagetype;
        if(null == bundle || null == bundle.getString(Extra.TYPE)) {
            imagetype = ImageUrlsFinder.IMAGETYPE.QINGLIANG;
        } else {
            imagetype = ImageUrlsFinder.IMAGETYPE.valueOf(bundle.getString(Extra.TYPE));
        }

//        try {
//            imageUrls.addAll(ImageUrlsFinder.findUrls(imagetype, getResources().getAssets()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        new ThumbTask().execute();

		options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.ic_stub)
			.showImageForEmptyUri(R.drawable.ic_empty)
			.showImageOnFail(R.drawable.ic_error)
			.cacheInMemory(true)
			.cacheOnDisc(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();

		listView = (GridView) findViewById(R.id.gridview);
		((GridView) listView).setAdapter(new ImageAdapter());
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				startImagePagerActivity(position);
			}
		});
	}



    class ThumbTask extends AsyncTask<String,Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            JSONArray images = ImageUrlsFinder.findImages();
            List<String> urls = new ArrayList<String>();
            for(int i = 0, len = images.length(); i < len; i++) {
                try {
                    Log.v("response text", images.getJSONObject(i).getString("thumb_large_url"));
                    urls.add(images.getJSONObject(i).getString("thumb_large_url"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            imageUrls.addAll(urls);
            return null;
        }
    }

    class UrlFinderTask extends AsyncTask<String,Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            imageUrls.addAll(ImageUrlsFinder.findUrls(ImageUrlsFinder.IMAGETYPE.QINGLIANG));
            return null;
        }
    }

	private void startImagePagerActivity(int position) {
		Intent intent = new Intent(this, ImagePagerActivity.class);
		intent.putExtra(Extra.IMAGES, (String[]) imageUrls.toArray(new String[imageUrls.size()]));
		intent.putExtra(Extra.IMAGE_POSITION, position);
		startActivity(intent);
	}

	public class ImageAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return imageUrls.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ImageView imageView;
			if (convertView == null) {
				imageView = (ImageView) getLayoutInflater().inflate(R.layout.item_grid_image, parent, false);
			} else {
				imageView = (ImageView) convertView;
			}

			imageLoader.displayImage(imageUrls.get(position), imageView, options);

			return imageView;
		}
	}

    @Override
    public void switchContent(ImageUrlsFinder.IMAGETYPE type) {
        Intent intent = new Intent(this, ImageGridActivity.class);
        intent.putExtra(Extra.TYPE, type.toString());
        startActivity(intent);
    }


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        ZhuamobTracking.onResume(this);
    }
}