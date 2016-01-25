package com.kymjs.rxvolley.interf;

import java.util.Map;

/**
 * Created by kymjs on 1/25/16.
 */
public interface IConvertAdapter<T> {
    T convertTo(Map<String, String> headers, byte[] t);
}
