package com.example.HealthMonitor;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    private Button group_details_button, component_details_button;
    private TextView group_details, component_details;
    private Boolean isGroupDetailsExpand = false, isComponentDetailsExpand = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        group_details = findViewById(R.id.group_details);
        group_details_button = findViewById(R.id.group_details_button);
        component_details = findViewById(R.id.component_details);
        component_details_button = findViewById(R.id.component_details_button);

        group_details_button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if(isGroupDetailsExpand){
                    group_details.setVisibility(View.GONE);
                    group_details_button.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                    isGroupDetailsExpand = false;
                }
                else{
                    group_details.setVisibility(View.VISIBLE);
                    group_details_button.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
                    isGroupDetailsExpand = true;
                }
            }
        });

        component_details_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isComponentDetailsExpand){
                    component_details.setVisibility(View.GONE);
                    component_details_button.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                    isComponentDetailsExpand = false;
                }
                else{
                    component_details.setVisibility(View.VISIBLE);
                    component_details_button.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
                    isComponentDetailsExpand = true;
                }
            }
        });
    }

}