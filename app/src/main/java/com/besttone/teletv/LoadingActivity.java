package com.besttone.teletv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoadingActivity extends AppCompatActivity {

    Button btnSkip;
    ImageView ivLoading;

    OkHttpClient okHttpClient;

    Gson gson;

    private final String mainUrl = "http://test.sogaha.cn/ssp/ad/get?adh=0&adw=0&androidid=3d6aa4d7ac6ae508&androididMd5=F344737084B41068086B9BF2B7567FC3&apiVersion=10&appId=1518495420441526274&appname=%E7%99%BE%E5%BA%A6%E8%BE%93%E5%85%A5%E6%B3%95&appversion=51&boot_mark=1030613f-c0fa-486a-b475-16c2dcda6f63&bundleId=com.baidu.input&caid&carrier=46011&devicetype=JAD-AL00&h=2578&idfa&idfaMd5&idfv&imei&imeiMd5&imsi&ip=171.213.5.35&isdeeplink=true&lat&lng=0&mac&modelCode=JAD-AL00&nw=20&oaid=55a925dc-3bad-48ba-85fb-3c6b928f1834&oaidMd5&orientation=0&phoneName&pid=1714971263978983426&ppi=160&ptype=1&romVersion&s=android&screendensity=120&supportEncrypt=1&sv=12&timeZone&traceid=0b02da3c2ea955581&ua=Mozilla%2F5.0+%28Linux%3B+Android+12%3B+JAD-AL00+Build%2FHUAWEIJAD-AL00%3B+wv%29+AppleWebKit%2F537.36+%28KHTML%2C+like+Gecko%29+Version%2F4.0+Chrome%2F99.0.4844.88+Mobile+Safari%2F537.36&update_mark=1645254114.527999998&vendor=HUAWEI&verCodeOfAG&verCodeOfHms&w=1228";

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    update(msg);
                    break;
                case 1:
                    goWeb(getString(R.string.url), null);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + msg.what);
            }
        }
    };

    private void update(Message message) {
        MainObject object = (MainObject) message.obj;
        if (object != null) {
            Glide.with(this).load(object.pic).into(ivLoading);
            ivLoading.setOnClickListener(v -> {
                removeHandler();
                goWeb(object.link, object);
            });
        }
    }



    private void sendRequest(String url) {
        Request request = new Request.Builder().url(url)
                .get().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;
                System.out.println("unfoldMonitorLink: " + response.body().toString());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        btnSkip = findViewById(R.id.btnSkip);
        ivLoading = findViewById(R.id.ivLoading);
        btnSkip.setOnClickListener(v -> {
            removeHandler();
            goWeb(getString(R.string.url), null);
        });

        Glide.with(this).load(getString(R.string.loading_img_url)).into(ivLoading);
        ivLoading.setOnClickListener(v -> {
            removeHandler();
            goWeb(getString(R.string.loading_url), null);
        });


//        okHttpClient = new OkHttpClient();
//        gson = new Gson();
//        handler.post(() -> {
//            //获取开屏页数据信息
//            Request request = new Request.Builder().url(mainUrl)
//                    .get().build();
//            okHttpClient.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                    if (!response.isSuccessful())
//                        throw new IOException("Unexpected code " + response);
//                    assert response.body() != null;
//                    String result = response.body().string();
//                    MainObject mainObject = gson.fromJson(result, new TypeToken<MainObject>() {
//                    }.getType());
//                    //send request unfoldMonitorLink
//                    for (String linkUrl : mainObject.unfoldMonitorLink
//                    ) {
//                        sendRequest(linkUrl);
//                    }
//                    //send message and update ui
//                    Message message = new Message();
//                    message.obj = mainObject;
//                    message.what = 0;
//                    handler.sendMessage(message);
//                }
//
//                @Override
//                public void onFailure(@NonNull Call call, @NonNull IOException e) {
//
//                }
//            });
//
//        });
        handler.sendEmptyMessageDelayed(1, 3000);
    }

    private void goWeb(String url, MainObject obj) {
        Intent intent = new Intent();
        intent.setClass(LoadingActivity.this, WebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putSerializable("object", obj);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void removeHandler() {
        if (handler != null) {
            handler.removeMessages(1);
            handler = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeHandler();
    }
}