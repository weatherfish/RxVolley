package com.kymjs.rxvolley.demo;

import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;

import com.kymjs.okhttp.OkHttpStack;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.ProgressListener;
import com.kymjs.rxvolley.http.RequestQueue;
import com.kymjs.rxvolley.rx.Result;
import com.kymjs.rxvolley.toolbox.FileUtils;
import com.kymjs.rxvolley.toolbox.Loger;
import com.squareup.okhttp.OkHttpClient;

import java.util.Map;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private Subscription subscription;

    HttpCallback callback = new HttpCallback() {
        @Override
        public void onPreStart() {
            Loger.debug("=====onPreStart");
        }

        @Override
        public void onPreHttp() {
            super.onPreHttp();
            Loger.debug("=====onPreHttp");
            Loger.debug("=====当前线程是主线程" + (Thread.currentThread() == Looper.getMainLooper
                    ().getThread()));
        }

        @Override
        public void onSuccessInAsync(byte[] t) {
            super.onSuccessInAsync(t);
            Loger.debug("=====onSuccessInAsync" + new String(t));
            Loger.debug("=====当前线程是主线程" + (Thread.currentThread() == Looper.getMainLooper
                    ().getThread()));
        }

        @Override
        public <String> void onSuccess(String t) {
            super.onSuccess(t);
            Loger.debug("=====onSuccess" + t);
            Loger.debug("=====当前线程是主线程" + (Thread.currentThread() == Looper.getMainLooper
                    ().getThread()));
        }

        @Override
        public void onSuccess(Map<String, String> headers, byte[] t) {
            super.onSuccess(headers, t);
            Loger.debug("=====当前线程是主线程" + (Thread.currentThread() == Looper.getMainLooper
                    ().getThread()));
            Loger.debug("=====onSuccessWithHeader" + headers.size() + new String(t));
        }

        @Override
        public void onFailure(int errorNo, String strMsg) {
            super.onFailure(errorNo, strMsg);
            Loger.debug("=====当前线程是主线程" + (Thread.currentThread() == Looper.getMainLooper
                    ().getThread()));
            Loger.debug("=====onFailure" + strMsg);
        }

        @Override
        public void onFinish() {
            super.onFinish();
            Loger.debug("=====当前线程是主线程" + (Thread.currentThread() == Looper.getMainLooper
                    ().getThread()));
            Loger.debug("=====onFinish");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RxVolley.setRequestQueue(RequestQueue.newRequestQueue(RxVolley.CACHE_FOLDER,
                new OkHttpStack(new OkHttpClient())));

        testGetRxJava();
    }

    private void testGetRxJava() {
        new RxVolley.Builder()
                .url("http://www.oschina.net/action/api/news_list").getResult()
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

    /**
     * 下载
     */
    private void download() {
        RxVolley.download(FileUtils.getSDCardPath() + "/a.apk",
                "https://www.oschina.net/uploads/osc-android-app-2.4.apk",
                new ProgressListener() {
                    @Override
                    public void onProgress(long transferredBytes, long totalSize) {
                        Loger.debug(transferredBytes + "======" + totalSize);
                        Loger.debug("=====当前线程是主线程" + (Thread.currentThread() == Looper
                                .getMainLooper().getThread()));
                    }
                }, callback);
    }
}
