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

/**
 * Created by adilsoomro on 8/19/16.
 */
public class CoinFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.coin_layout, container, false);

        if (Cache.topDivs == null || Cache.topDivs.equals("")) return rootView;

        TableLayout tl = (TableLayout) rootView.findViewById(R.id.item_table);

        try {
            JSONObject obj = new JSONObject(Cache.bottomDivs);
            Iterator<String> keys = obj.keys();
            while (keys.hasNext()) {
                String key = keys.next();

                String market = key.split("-")[0];
                if (!market.contains("btc")) continue;

                String strType = key.split("-")[1];

                TableLayout tlx = (TableLayout) inflater.inflate(R.layout.item_layout, container, false);

                TextView level = (TextView) tlx.findViewById(R.id.level);
                TextView reliability = (TextView) tlx.findViewById(R.id.reliability);
                TextView time = (TextView) tlx.findViewById(R.id.div_time);

                level.setText(strType);
                reliability.setText(obj.getJSONObject(key).getDouble("reliability") + "");
                time.setText(obj.getJSONObject(key).getString("cur_div_time"));

                tl.addView(tlx);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootView;
    }
}
