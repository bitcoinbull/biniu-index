package com.btbns.index;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.btbns.index.common.Cache;
import com.btbns.index.fragments.EthBottomDivFragment;
import com.btbns.index.fragments.EthTopDivFragment;
import com.btbns.index.meta.CoinName;
import com.btbns.index.meta.SignalType;
import com.btbns.index.fragments.BtcBottomDivFragment;
import com.btbns.index.fragments.BtcTopDivFragment;
import com.btbns.index.fragments.LtcBottomDivFragment;
import com.btbns.index.fragments.LtcTopDivFragment;
import com.btbns.index.fragments.ViewPagerAdapter;
import com.btbns.index.meta.Titles;
import com.btbns.index.utils.Util;

import java.util.Timer;
import java.util.TimerTask;


/**
 * @author Adil Soomro
 */
public class MainActivity extends AppCompatActivity {
    /**
     * Called when the activity is first created.
     */

    public final Context cxt = this;

    String[] titles = {
            Titles.BTC_SHORT,
            Titles.BTC_LONG,

            Titles.ETH_SHORT,
            Titles.ETH_LONG,

            Titles.LTC_SHORT,
            Titles.LTC_LONG
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Timer timer = new Timer();
        TimerTask updateTask = new TimerTask() {
            @Override
            public void run() {
                executeUpdate();
            }
        };

        TimerTask scrollTask = new TimerTask() {
            @Override
            public void run() {
                scrollTab();
            }
        };

        timer.schedule(updateTask, 1, 240000);
//        timer.schedule(scrollTask, 10000, 10000);
    }

    public void executeUpdate() {
        LongOperation lo = new LongOperation();
        lo.execute();
    }

    static int curIdx = 0;

    public void scrollTab() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        if (tabLayout == null) return;

        tabLayout.getTabAt((curIdx + 1) % titles.length).select();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        String json = "";

        Fragment btcTopDiv = new BtcTopDivFragment();
        Fragment btcBottomDiv = new BtcBottomDivFragment();

        Fragment ethTopDiv = new EthTopDivFragment();
        Fragment ethBottomDiv = new EthBottomDivFragment();

        Fragment ltcTopDiv = new LtcTopDivFragment();
        Fragment ltcBottomDiv = new LtcBottomDivFragment();

        adapter.insertNewFragment(btcTopDiv, CoinName.BTC, SignalType.TOP_DIV, json);
        adapter.insertNewFragment(btcBottomDiv, CoinName.BTC, SignalType.BOTTOM_DIV, json);

        adapter.insertNewFragment(ethTopDiv, CoinName.ETH, SignalType.TOP_DIV, json);
        adapter.insertNewFragment(ethBottomDiv, CoinName.ETH, SignalType.BOTTOM_DIV, json);

        adapter.insertNewFragment(ltcTopDiv, CoinName.LTC, SignalType.TOP_DIV, json);
        adapter.insertNewFragment(ltcBottomDiv, CoinName.LTC, SignalType.BOTTOM_DIV, json);

        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.main_activity, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private class LongOperation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String topDivs = Util.getUrlContent("http://api.btbns.com:8888/v1/top_divs?type=long");
            String bottomDivs = Util.getUrlContent("http://api.btbns.com:8888/v1/bottom_divs?type=long");

            Cache.topDivs = topDivs;
            Cache.bottomDivs = bottomDivs;

            Log.e("biniu.topDivs", topDivs);
            Log.e("biniu.bottomDivs", bottomDivs);

            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            ViewPager viewPager = (ViewPager) findViewById(R.id.main_tab_content);
            setupViewPager(viewPager);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
            tabLayout.setupWithViewPager(viewPager);

            double max = -1;
            int idx = -1;
            for (int i = 0; i < titles.length; i++) {
                tabLayout.getTabAt(i).setText(titles[i]);

                if (idx < 0 && Cache.status.get(titles[i]) != null && Cache.status.get(titles[i]) > max) {
                    idx = i;
                    max = Cache.status.get(titles[i]);
                }
            }

            if (idx < 0) {
                curIdx = 0;
                tabLayout.getTabAt(0).select();
            } else {
                curIdx = idx;
                tabLayout.getTabAt(idx).select();
            }

//            Toast.makeText(cxt, "更新时间:" + new Date().toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}