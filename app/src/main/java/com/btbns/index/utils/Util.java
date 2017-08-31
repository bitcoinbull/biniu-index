package com.btbns.index.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sunnycomes on 8/24/17.
 */

public class Util {
    public static String getUrlContent(String sUrl) {

        HttpURLConnection connection = null;
        try {
            URL url = new URL(sUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();

            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            String content = "";
            while ((line = rd.readLine()) != null) {
                content += line + "\n";
            }

            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String getMarketType(String market) {
        if (market.contains("future") && market.contains("next")) return "次周合约";
        if (market.contains("future") && market.contains("quarter")) return "季合约";
        if (market.contains("future")) return "当周合约";

        return "现货";
    }

    public static String getKlineType(String type) {
        if (type.equals("60")) return "1分钟";
        if (type.equals("180")) return "3分钟";
        if (type.equals("300")) return "5分钟";
        if (type.equals("600")) return "10分钟";
        if (type.equals("900")) return "15分钟";
        if (type.equals("1800")) return "30分钟";
        if (type.equals("3600")) return "1小时";
        if (type.equals("7200")) return "2小时";
        if (type.equals("14400")) return "4小时";
        if (type.equals("21600")) return "6小时";
        if (type.equals("43200")) return "12小时";
        if (type.equals("86400")) return "1天";
        if (type.equals("259200")) return "3天";
        if (type.equals("604800")) return "1周";

        return "未知";
    }
}
