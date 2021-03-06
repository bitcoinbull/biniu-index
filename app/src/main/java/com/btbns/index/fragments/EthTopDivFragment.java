package com.btbns.index.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.btbns.index.R;
import com.btbns.index.common.Cache;
import com.btbns.index.common.Threshold;
import com.btbns.index.meta.Titles;
import com.btbns.index.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by adilsoomro on 8/19/16.
 */
public class EthTopDivFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.coin_layout, container, false);

        if (Cache.topDivs == null || Cache.topDivs.equals("") || Cache.topDivs.equals("{}")) {
            Cache.status.put(Titles.ETH_SHORT, 0.0);

            return rootView;
        }

        TableLayout tl = (TableLayout) rootView.findViewById(R.id.item_table);

        double max = -1;
        try {
            JSONObject obj = new JSONObject(Cache.topDivs);
            Iterator<String> keys = obj.keys();

            TableLayout tlx = (TableLayout) inflater.inflate(R.layout.summary_layout, container, false);
//            tl.addView(tlx);

            TextView summary = (TextView) tlx.findViewById(R.id.summary);
            summary.setText("更新时间:" + obj.getString("timestamp") + ", 每4分钟刷新一次。");

            while (keys.hasNext()) {
                String key = keys.next();

                String market = key.split("-")[0];
                if (!market.contains("eth")) continue;

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
                time.setText(obj.getJSONObject(key).getString("cur_high_time"));

                tl.addView(tlx);
            }

        } catch (JSONException e) {
            Cache.status.put(Titles.ETH_SHORT, 0.0);

            e.printStackTrace();
        }

        Cache.status.put(Titles.ETH_SHORT, max);

        return rootView;
    }
}
