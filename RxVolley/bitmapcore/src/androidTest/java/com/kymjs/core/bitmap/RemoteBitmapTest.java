package com.kymjs.core.bitmap;

import android.os.Looper;
import android.test.AndroidTestCase;
import android.widget.ImageView;

import com.kymjs.core.bitmap.client.BitmapCore;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.toolbox.Loger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by kymjs on 1/26/16.
 */
public class RemoteBitmapTest extends AndroidTestCase {

    HttpCallback callback;
    ImageView imageView;

    @Before
    public void setUp() throws Exception {
        callback = new HttpCallback() {
            @Override
            public void onPreStart() {
                Loger.debug("=====onPreStart");
                assertTrue(Thread.currentThread() == Looper.getMainLooper().getThread());
            }

            @Override
            public void onPreHttp() {
                super.onPreHttp();
                Loger.debug("=====onPreHttp");
                assertTrue(Thread.currentThread() == Looper.getMainLooper().getThread());
            }

            @Override
            public void onSuccessInAsync(byte[] t) {
                super.onSuccessInAsync(t);
                assertNotNull(t);
                Loger.debug("=====onSuccessInAsync" + new String(t));
                assertFalse(Thread.currentThread() == Looper.getMainLooper().getThread());
            }

            @Override
            public <Bitmap> void onSuccess(Bitmap t) {
                super.onSuccess(t);
                Loger.debug("=====onSuccess" + t);
                
                assertTrue(Thread.currentThread() == Looper.getMainLooper().getThread());
                assertEquals(imageView.getTag(), URL);
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                assertTrue(Thread.currentThread() == Looper.getMainLooper().getThread());
                Loger.debug("=====onFailure" + strMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                assertTrue(Thread.currentThread() == Looper.getMainLooper().getThread());
                Loger.debug("=====onFinish");
            }
        };

        imageView = new ImageView(getContext());
    }

    public static final String URL = "http://kymjs.com/image/logo.jpg";

    @Test
    public void testBitmapLoad() {
        new BitmapCore.Builder().url(URL)
                .view(imageView)
                .callback(callback)
                .doTask();
    }

    @After
    public void down() {
    }
}
