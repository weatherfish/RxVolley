package com.kymjs.rxvolley;

import android.os.Looper;
import android.test.AndroidTestCase;

import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.ProgressListener;
import com.kymjs.rxvolley.toolbox.FileUtils;
import com.kymjs.rxvolley.toolbox.Loger;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

/**
 * Created by kymjs on 1/21/16.
 */
public class DownloadTest extends AndroidTestCase {

    HttpCallback callback;

    @Before
    public void setUp() throws Exception {
        callback = new HttpCallback() {
            @Override
            public void onPreStart() {
                Loger.debug("=====onPreStart");

//                测试类是运行在异步的,所以此处断言会异常
//                assertTrue(Thread.currentThread() == Looper.getMainLooper().getThread());
            }

            @Override
            public void onPreHttp() {
                super.onPreHttp();
                Loger.debug("=====onPreHttp");
                assertTrue(Thread.currentThread() == Looper.getMainLooper().getThread());
            }

            @Override
            public void onSuccessInAsync(byte[] t) {
                Loger.debug("=====onSuccessInAsync" + new String(t));

                assertNotNull(t);
                
                //onSuccessInAsync 一定是运行在异步
                assertFalse(Thread.currentThread() == Looper.getMainLooper().getThread());
            }

            @Override
            public <String> void onSuccess(String t) {
                Loger.debug("=====onSuccess" + t);
                assertTrue(Thread.currentThread() == Looper.getMainLooper().getThread());
            }

            @Override
            public void onSuccess(Map<String, String> headers, byte[] t) {
                assertNotNull(t);
                Loger.debug("=====onSuccessWithHeader" + headers.size() + new String(t));
                assertTrue(Thread.currentThread() == Looper.getMainLooper().getThread());
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                Loger.debug("=====onFailure" + strMsg);
                assertTrue(Thread.currentThread() == Looper.getMainLooper().getThread());
            }

            @Override
            public void onFinish() {
                super.onFinish();
                Loger.debug("=====onFinish");
                assertTrue(Thread.currentThread() == Looper.getMainLooper().getThread());
            }
        };
    }

    /**
     * 下载方法在单元测试中总是会被中断,在APP中测试就工作正常,猜测应该是测试线程不允许有长时间不结束的线程
     */
    @Test
    public void testDownload() throws Exception {
        RxVolley.download(FileUtils.getSDCardPath() + "/a.apk",
                "https://www.oschina.net/uploads/osc-android-app-2.4.apk",
                new ProgressListener() {
                    @Override
                    public void onProgress(long transferredBytes, long totalSize) {
                        Loger.debug(transferredBytes + "======" + totalSize);
                        assertTrue(Thread.currentThread() == Looper.getMainLooper().getThread());
                    }
                }, callback);
    }
}
