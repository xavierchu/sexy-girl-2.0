package org.xchu.sexy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.meepo.sexygirl.Constants;
import org.meepo.sexygirl.ImagePagerActivity;
import org.meepo.sexygirl.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoqi.zhu on 13-12-16.
 */
public class CategoryActivity extends Activity {
    ListView lv;
    List<String> list;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category);
        lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(new CategoryAdapter(this));
//        lv.setBackgroundColor(0);
        Display display = this.getWindowManager().getDefaultDisplay();
        int dividerHeight = ((display.getHeight() - 200) / list.size()) - 100;
        lv.setDividerHeight(dividerHeight);
        lv.setPadding(100, 100, 100, 100);
    }

    private class CategoryAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private Activity context;

        public CategoryAdapter(Activity context) {
            this.context = context;
            // Cache the LayoutInflate to avoid asking for a new one each time.
            mInflater = LayoutInflater.from(context);

            list = new ArrayList<String>();
            list.add("宅男女神");
            list.add("网络美女");
            list.add("性感诱惑");
            list.add("唯美清纯");

        }

        public int getCount() {
            return list.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_item_icon_text, null);

                holder = new ViewHolder();
                holder.text = (TextView) convertView.findViewById(R.id.text);
//                holder.icon = (ImageView) convertView.findViewById(R.id.icon);
                convertView.setTag(holder);
            } else {
                // Get the ViewHolder back to get fast access to the TextView
                // and the ImageView.
                holder = (ViewHolder) convertView.getTag();
            }

            // Bind the data efficiently with the holder.
            holder.text.setText(list.get(position));
            holder.text.setTextSize(36);
            holder.text.setBackgroundColor(0x33ffffff);
            holder.text.setTextColor(0xeeff0048);
//            holder.icon.setImageBitmap(list.get(position));
            Display display = context.getWindowManager().getDefaultDisplay();
            ViewGroup.LayoutParams para = holder.text.getLayoutParams();
//            para.height = (display.getHeight() - 50) / list.size();
            para.height = 100;
            para.width = display.getWidth() - 200;
//            holder.icon.setLayoutParams(para);
//            holder.icon.setScaleType(ImageView.ScaleType.FIT_XY);
            holder.text.setLayoutParams(para);
//            holder.text.setScaleType(ImageView.ScaleType.FIT_XY);

            holder.text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ConnectivityManager manager = (ConnectivityManager) CategoryActivity.this.getSystemService(CONNECTIVITY_SERVICE);
                    NetworkInfo networkinfo = manager.getActiveNetworkInfo();
                    if (networkinfo == null || !networkinfo.isAvailable()) {
                        new AlertDialog.Builder(CategoryActivity.this).setMessage("没有可以使用的网络，请打开WIFI连接").setPositiveButton("Ok", null).show();
                    } else {
                        startMainActivity(position);
                    }
                }
            });

            return convertView;
        }

        class ViewHolder {
            TextView text;
//            ImageView icon;
        }

        private void startMainActivity(int position) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra(Constants.Extra.IMAGE_POSITION, position);
            context.startActivity(intent);
        }
    }


}

