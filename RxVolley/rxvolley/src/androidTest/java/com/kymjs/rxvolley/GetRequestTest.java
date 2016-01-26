package com.kymjs.rxvolley;

import android.os.Looper;
import android.test.AndroidTestCase;

import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.RequestQueue;
import com.kymjs.rxvolley.interf.IConvertAdapter;
import com.kymjs.rxvolley.toolbox.Loger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

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
                // 测试类是运行在异步的,所以此处断言会异常
                // assertTrue(Thread.currentThread() == Looper.getMainLooper().getThread());
            }

            @Override
            public void onPreHttp() {
                Loger.debug("=====onPreHttp");
                assertTrue(Thread.currentThread() == Looper.getMainLooper().getThread());
            }

            @Override
            public void onSuccessInAsync(byte[] t) {
                assertNotNull(t);
                Loger.debug("=====onSuccessInAsync" + new String(t));
                //onSuccessInAsync 一定是运行在异步
                assertFalse(Thread.currentThread() == Looper.getMainLooper().getThread());
            }

            @Override
            public <String> void onSuccess(String t) {
                Loger.debug("=====onSuccess" + t);
                assertNotNull(t);
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

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetWithBuild() throws Exception {
        final String STR = "转换后的字符串";
        new RxVolley.Builder().url("http://www.oschina.net/action/api/news_list")
                .convertAdapter(new IConvertAdapter<String>() {
                    @Override
                    public String convertTo(Map<String, String> headers, byte[] t) {
                        assertNotNull(t);
                        return STR;
                    }
                })
                .callback(new HttpCallback() {
                    @Override
                    public <String> void onSuccess(String t) {
                        assertEquals(STR, t);
                    }
                })
                .doTask();
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
//
//    /**
//     * 也是跟download一样的问题,在测试中就是不返回
//     */
//    @Test
//    public void testGetRxJava() throws Exception {
//        new RxVolley.Builder().callback(callback)
//                .url("http://www.oschina.net/action/api/news_list").getResult()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .map(new Func1<Result, String>() {
//                    @Override
//                    public String call(Result result) {
//                        return new String(result.data);
//                    }
//                })
//                .subscribe(new Action1<String>() {
//                    @Override
//                    public void call(String s) {
//                        Loger.debug("=====onSuccess" + s);
//                        assertTrue(Thread.currentThread() == Looper.getMainLooper().getThread());
//                    }
//                });
//    }

}