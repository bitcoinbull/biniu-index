package com.btbns.index.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import com.btbns.index.common.Cache;
import com.btbns.index.R;
import com.btbns.index.common.Threshold;
import com.btbns.index.meta.Titles;
import com.btbns.index.utils.Util;

/**
 * Created by adilsoomro on 8/19/16.
 */
public class LtcBottomDivFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.coin_layout, container, false);

        if (Cache.bottomDivs == null || Cache.bottomDivs.equals("") || Cache.bottomDivs.equals("{}")) {
            Cache.status.put(Titles.LTC_LONG, 0.0);

            return rootView;
        }

        TableLayout tl = (TableLayout) rootView.findViewById(R.id.item_table);

        double max = -1;

        try {
            JSONObject obj = new JSONObject(Cache.bottomDivs);
            Iterator<String> keys = obj.keys();

            TableLayout tlx = (TableLayout) inflater.inflate(R.layout.summary_layout, container, false);
//            tl.addView(tlx);

            TextView summary = (TextView) tlx.findViewById(R.id.summary);
            summary.setText("更新时间:" + obj.getString("timestamp") + ", 每4分钟刷新一次。");

            while (keys.hasNext()) {
                String key = keys.next();

                String market = key.split("-")[0];
                if (!market.contains("ltc")) continue;

                tlx = (TableLayout) inflater.inflate(R.layout.item_layout, container, false);

                TextView level = (TextView) tlx.findViewById(R.id.level);
                TextView reliability = (TextView) tlx.findViewById(R.id.reliability);
                TextView time = (TextView) tlx.findViewById(R.id.div_time);

                String mType = Util.getMarketType(market);
                String kType = Util.getKlineType(key.split("-")[1]);

                double score = obj.getJSONObject(key).getDouble("reliability");
                if (score < Threshold.threshold.get(key.split("-")[1])) continue;

                if (score > max) max = score;

                level.setText(mType + "/" + kType);
                reliability.setText(score + "");
                time.setText(obj.getJSONObject(key).getString("cur_low_time"));

                tl.addView(tlx);
            }

        } catch (JSONException e) {
            Cache.status.put(Titles.LTC_LONG, 0.0);

            e.printStackTrace();
        }

        Cache.status.put(Titles.LTC_LONG, max);

        return rootView;
    }
}
