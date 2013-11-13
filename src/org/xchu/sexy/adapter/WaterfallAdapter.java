package org.xchu.sexy.adapter;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import org.meepo.sexygirl.Constants;
import org.meepo.sexygirl.Image;
import org.meepo.sexygirl.ImagePagerActivity;
import org.meepo.sexygirl.R;

import java.util.ArrayList;
import java.util.List;


public class WaterfallAdapter extends BaseAdapter {


    ArrayList<Image> list;
    Activity activity;
    Display display;
	private Drawable drawable;
	
	public WaterfallAdapter(ArrayList<Image> list , Activity activity, Display display) {
		this.list = list;
		this.activity = activity;
        this.display = display;
		drawable = activity.getResources().getDrawable(R.drawable.load_default);
	}


	@Override
	public int getCount() {
		return list != null ? list.size() : 0;
	}

	@Override
	public Object getItem(int index) {
		return list.get(index);
	}

	@Override
	public long getItemId(int index) {
		return index;
	}

	@Override
	public View getView(final int position, View view, ViewGroup group) {
		final Holder holder;
		// 得到View
		if (view == null) {
			holder = new Holder();
			LayoutInflater inflater = LayoutInflater.from(activity);
			view = inflater.inflate(R.layout.image_item, null);
			holder.ivIcon = (ImageView) view.findViewById(R.id.row_icon);
			holder.pbLoad = (ProgressBar) view.findViewById(R.id.pb_load);

			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}
		
		String url = list.get(position).getThumbUrl();
		ImageLoader.getInstance().displayImage(url, holder.ivIcon,
				new ImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						
						//这儿初先初始化出来image所占的位置的大小，先把瀑布流固定住，这样瀑布流就不会因为图片加载出来后大小变化了
						FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) holder.ivIcon.getLayoutParams();
                        DisplayMetrics metrics = new DisplayMetrics();
                        display.getMetrics(metrics);
						//多屏幕适配
						int dWidth = metrics.widthPixels;;
						int dHeight = metrics.heightPixels;
						float wscale = dWidth / 480.0f;
						float hscale = dHeight / 800.0f;
                        int imgHeight = list.get(position).getThumbHeight();
                        int imgWidth = list.get(position).getThumbWidth() + 6;
						lp.height = (int) (imgHeight * hscale);
						lp.width = (int) (imgWidth * wscale);
						holder.ivIcon.setLayoutParams(lp);
						
						holder.ivIcon.setImageDrawable(drawable);
						holder.pbLoad.setVisibility(View.VISIBLE);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						String message = null;
						switch (failReason.getType()) {
						case IO_ERROR:
							message = "Input/Output error";
							break;
						case DECODING_ERROR:
							message = "can not be decoding";
							break;
						case NETWORK_DENIED:
							message = "Downloads are denied";
							break;
						case OUT_OF_MEMORY:
							message = "内存不足";
							Toast.makeText(activity, message, Toast.LENGTH_SHORT)
									.show();
							break;
						case UNKNOWN:
							message = "Unknown error";
							Toast.makeText(activity, message, Toast.LENGTH_SHORT)
									.show();
							break;
						}
						holder.pbLoad.setVisibility(View.GONE);
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						holder.pbLoad.setVisibility(View.GONE);
					}

					@Override
					public void onLoadingCancelled(String paramString,
							View paramView) {
					}
				});


		holder.ivIcon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
                startImagePagerActivity(position);
			}
		});
		
		return view;
	}


    private void startImagePagerActivity(int position) {
        Intent intent = new Intent(activity, ImagePagerActivity.class);
        intent.putParcelableArrayListExtra(Constants.Extra.IMAGES, list);
        intent.putExtra(Constants.Extra.IMAGE_POSITION, position);
        activity.startActivity(intent);
    }


}

class Holder {
	public ImageView ivIcon;
	public ProgressBar pbLoad;
}