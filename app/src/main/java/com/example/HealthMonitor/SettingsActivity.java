package com.example.HealthMonitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.thekhaeng.pushdownanim.PushDownAnim;

public class SettingsActivity extends AppCompatActivity {
    private int pref_refreshrate;
    private EditText refreshRate;
    private Button save_rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        refreshRate = findViewById(R.id.editTextNumber);
        save_rate = findViewById(R.id.save_rate);

        SharedPreferences share= getSharedPreferences("PatientPref", MODE_PRIVATE);
        pref_refreshrate = share.getInt("refreshrate",15);

        PushDownAnim.setPushDownAnimTo(save_rate)
                .setScale(PushDownAnim.MODE_STATIC_DP, 12)
                .setDurationPush(100)
                .setDurationRelease(225);

        refreshRate.setText(""+pref_refreshrate);

        save_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref_refreshrate = Integer.parseInt(refreshRate.getText().toString());
                //pref_refreshrate = pref_refreshrate*1000;
            }
        });

    }

    @Override
    protected void onPause (){
        super.onPause();
        SharedPreferences share = getSharedPreferences("PatientPref",MODE_PRIVATE);
        SharedPreferences.Editor edit = share.edit();
        edit.putInt("refreshrate", pref_refreshrate);
        edit.apply();
    }

}