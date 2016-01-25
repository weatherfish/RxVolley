package com.kymjs.rxvolley;

import android.test.AndroidTestCase;

import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.RequestQueue;
import com.kymjs.rxvolley.rx.Result;
import com.kymjs.rxvolley.toolbox.Loger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author kymjs (http://www.kymjs.com/) on 1/5/16.
 */
public class GetRequestTest extends AndroidTestCase {

    HttpCallback callback;

    @Before
    public void setUp() throws Exception {
        RxVolley.setRequestQueue(RequestQueue.newRequestQueue(getContext().getCacheDir()));

        callback = new HttpCallback() {
            @Override
            public void onPreStart() {
                Loger.debug("=====onPreStart");
            }

            @Override
            public void onPreHttp() {
                super.onPreHttp();
                Loger.debug("=====onPreHttp");
            }

            @Override
            public void onSuccessInAsync(byte[] t) {
                super.onSuccessInAsync(t);
                Loger.debug("=====onSuccessInAsync" + new String(t));
            }

            @Override
            public <String> void onSuccess(String t) {
                Loger.debug("=====onSuccess" + t);
            }

            @Override
            public void onSuccess(Map<String, String> headers, byte[] t) {
                super.onSuccess(headers, t);
                Loger.debug("=====onSuccessWithHeader" + headers.size() + new String(t));
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                Loger.debug("=====onFailure" + strMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                Loger.debug("=====onFinish");
            }
        };
    }

    @After
    public void tearDown() throws Exception {
    }
//
//    @Test
//    public void testGetOnSuccess() throws Exception {
//        RxVolley.get("http://www.oschina.net/action/api/news_list", callback);
//    }
//
//    @Test
//    public void testGetOnFailure() throws Exception {
//        RxVolley.get("http://failure/url/", callback);
//    }
//
//    @Test
//    public void testGetWithParams() throws Exception {
//        HttpParams params = new HttpParams();
//        params.put("pageIndex", 1);
//        params.put("pageSize", 20);
//        RxVolley.get("http://www.oschina.net/action/api/news_list", params, callback);
//    }

    @Test
    public void testGetRxJava() throws Exception {
        new RxVolley.Builder().url("http://www.oschina.net/action/api/news_list").getResult()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Result, String>() {
                    @Override
                    public String call(Result result) {
                        return new String(result.data);
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Loger.debug("=====onSuccess" + s);
                    }
                });
    }   

}