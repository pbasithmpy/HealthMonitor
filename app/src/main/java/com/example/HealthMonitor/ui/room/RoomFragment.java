package com.example.HealthMonitor.ui.room;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.HealthMonitor.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static android.content.Context.MODE_PRIVATE;

public class RoomFragment extends Fragment {

    WebView webView1,webView2;
    private final Handler mainhandler = new Handler();
    int delay = 15000;
    private volatile boolean stopthread = false;
    private String widget1_id, widget2_id, site, channel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_room, container, false);

        webView1 = root.findViewById(R.id.webview1);
        webView2 = root.findViewById(R.id.webview2);

        webView1.setVerticalScrollBarEnabled(true);
        webView1.setHorizontalScrollBarEnabled(true);
        webView2.setVerticalScrollBarEnabled(true);
        webView2.setHorizontalScrollBarEnabled(true);

        FloatingActionButton map_btn = getActivity().findViewById(R.id.map_button);
        map_btn.setVisibility(View.VISIBLE);

        WebSettings webSettings1 = webView1.getSettings();
        webSettings1.setJavaScriptEnabled(true);
        WebSettings webSettings2 = webView2.getSettings();
        webSettings2.setJavaScriptEnabled(true);

        webView1.setWebViewClient(new WebViewClient());
        webView2.setWebViewClient(new WebViewClient());

        SharedPreferences share= getActivity().getSharedPreferences("PatientPref", MODE_PRIVATE);
        site = share.getString("site","");
        channel = share.getString("channel","");
        widget1_id = share.getString("widget1", "123456");
        widget2_id = share.getString("widget2", "654321");
        delay = share.getInt("refreshrate",15000);

        webView1.loadUrl("https://"+site+".com/channels/"+channel+"/widgets/"+widget1_id);
        webView2.loadUrl("https://"+site+".com/channels/"+channel+"/widgets/"+widget2_id);

        return root;
    }

    @Override
    public void onResume(){
        super.onResume();
        stopthread = false;
        ExampleRunnable runnable = new ExampleRunnable();
        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Override
    public void onDestroy (){
        super.onDestroy();
        stopthread = true;
    }

    class ExampleRunnable implements Runnable{
        @Override
        public void run(){
            while (!Thread.currentThread().isInterrupted()) {
                if (stopthread)
                    return;
                mainhandler.post(new Runnable() {
                    @Override
                    public void run() {
                        webView1.reload();
                        webView2.reload();
                    }
                });
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}