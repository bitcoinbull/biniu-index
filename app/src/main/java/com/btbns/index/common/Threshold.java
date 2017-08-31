package com.btbns.index.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sunnycomes on 8/25/17.
 */

public class Threshold {
    public static Map<String, Double> threshold = new HashMap<>();
    static {
        threshold.put("60", 0.5);
        threshold.put("180", 0.5);
        threshold.put("300", 0.5);
        threshold.put("600", 0.5);
        threshold.put("900", 0.5);
        threshold.put("1800", 0.5);
        threshold.put("3600", 0.5);
        threshold.put("7200", 0.5);
        threshold.put("14400", 0.5);
        threshold.put("21600", 0.5);
        threshold.put("43200", 0.5);
        threshold.put("86400", 0.5);
        threshold.put("259200", 0.5);
        threshold.put("604800", 0.5);
    }
}
