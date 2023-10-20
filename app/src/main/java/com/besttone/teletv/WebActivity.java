package com.besttone.teletv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WebActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    AgentWeb mAgentWeb;

    OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        linearLayout = findViewById(R.id.ll_parent);
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            String url = bundle.getString("url");
            mAgentWeb = AgentWeb.with(this)
                    .setAgentWebParent(linearLayout, new LinearLayout.LayoutParams(-1, -1))
                    .useDefaultIndicator()
                    .createAgentWeb()
                    .ready()
                    .go(url);
            MainObject mainObject = null;
            mainObject = (MainObject) bundle.getSerializable("object");
            if (mainObject != null) {
                okHttpClient = new OkHttpClient();
                for (String link : mainObject.clickMonitorLink
                ) {
                    sendRequest(link);
                }
            }
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
                System.out.println("clickMonitorLink: " + response.body().toString());
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}