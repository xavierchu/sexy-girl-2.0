package org.xchu.sexy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobads.appoffers.OffersManager;
import com.baidu.mobads.appoffers.PointsChangeListener;

import org.meepo.sexygirl.Constants;
import org.meepo.sexygirl.R;
import org.xchu.sexy.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoqi.zhu on 13-12-16.
 */
public class CategoryActivity extends Activity {

    static final int COL_HEIGHT = 70;

    ListView lv;
    List<String> list;
    TextView tv;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category);
        lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(new CategoryAdapter(this));
//        lv.setBackgroundColor(0);
        Display display = this.getWindowManager().getDefaultDisplay();
        int dividerHeight = (display.getHeight() - 100 - COL_HEIGHT * list.size()) / (list.size() + 2);
        lv.setDividerHeight(dividerHeight);
        lv.setPadding(80, 50, 80, 50);

        tv = (TextView) findViewById(R.id.textView);
        tv.setText("当前积分：" + OffersManager.getPoints(CategoryActivity.this));

        OffersManager.setPointsChangeListener(new PointsChangeListener() {
                @Override
            public void onPointsChanged(int points) {
                tv.setText("当前积分：" + points);
            }
        });
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
            list.add("性感尤物");
            list.add("唯美清纯");
            list.add("美臀诱惑");
            list.add("日本美女");
            list.add("免费获取积分");
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
            holder.text.setTextSize(20);
            holder.text.setBackgroundColor(0x33ffffff);
            holder.text.setTextColor(0xeeff0048);
//            holder.icon.setImageBitmap(list.get(position));
            Display display = context.getWindowManager().getDefaultDisplay();
            ViewGroup.LayoutParams para = holder.text.getLayoutParams();
//            para.height = (display.getHeight() - 50) / list.size();
            para.height = COL_HEIGHT;
            para.width = display.getWidth() - 120;
//            holder.icon.setLayoutParams(para);
//            holder.icon.setScaleType(ImageView.ScaleType.FIT_XY);
            holder.text.setLayoutParams(para);
//            holder.text.setScaleType(ImageView.ScaleType.FIT_XY);

            holder.text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (NetworkUtils.isNetConnected(CategoryActivity.this)) {
                        startMainActivity(position);
                    } else {
                        new AlertDialog.Builder(CategoryActivity.this).setMessage("没有可以使用的网络，请打开您的WIFI或3G网络").setPositiveButton("Ok", null).show();
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
            if(position == list.size() - 1) {
                OffersManager.showOffers(CategoryActivity.this);
            } else {
                int points = OffersManager.getPoints(CategoryActivity.this);
                if(position == 4 && points < 50) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CategoryActivity.this, "需要50积分解锁，您的积分不足，请点击“获取积分”按钮获取积分。", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                if(position == 5 && points < 100) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CategoryActivity.this, "需要100积分解锁，您的积分不足，请点击“获取积分”按钮获取积分。", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra(Constants.Extra.IMAGE_POSITION, position);
                context.startActivity(intent);
            }
        }
    }


}

