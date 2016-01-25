package com.kymjs.rxvolley.http;

import com.kymjs.rxvolley.interf.IConvertAdapter;

import java.util.Map;

/**
 * Created by kymjs on 1/25/16.
 */
public class DefaultConvertAdapter implements IConvertAdapter<String> {
    @Override
    public String convertTo(Map<String, String> headers, byte[] t) {
        return new String(t);
    }
}
