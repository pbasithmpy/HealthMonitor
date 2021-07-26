package com.example.HealthMonitor.ui.patient;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.HealthMonitor.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import static android.content.Context.MODE_PRIVATE;

public class PatientFragment extends Fragment {

    private String body_temp ="37", oxygen_percent ="95", pulse_rate="85";
    private TextView  temp,bpm, oxygen,test,text_patient, temp_normal, pulse_normal, oxygen_normal;
    private ImageView temp_image, pulse_image, oxygen_image;
    private View tempOutline,pulseOutline,oxygenOutline;
    //private RequestQueue mQueue;
    private final Handler mainhandler = new Handler();
    private int delay = 15000;
    private volatile boolean stopthread = false, isPulseNotNormal = false, isTempNotNormal =false, isOxygenNotNormal =false;
    private ConstraintLayout temp_layout,pulse_layout,oxygen_layout;
    private String site_name, channelID, apikey, connection_error;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_patient, container, false);

        text_patient = root.findViewById(R.id.text_patient);
        bpm = root.findViewById(R.id.bpm);
        oxygen= root.findViewById(R.id.oxygen);
        temp = root.findViewById(R.id.temp);
        test = root.findViewById(R.id.test);
        temp_normal = root.findViewById(R.id.temp_normal);
        pulse_normal = root.findViewById(R.id.pulse_normal);
        oxygen_normal = root.findViewById(R.id.oxygen_normal);
        temp_image = root.findViewById(R.id.imageView1);
        pulse_image = root.findViewById(R.id.imageView2);
        oxygen_image = root.findViewById(R.id.imageView3);
        tempOutline = root.findViewById(R.id.temp_outline);
        pulseOutline = root.findViewById(R.id.pulse_outline);
        oxygenOutline = root.findViewById(R.id.oxygen_outline);
        temp_layout = root.findViewById(R.id.temp_layout);
        pulse_layout = root.findViewById(R.id.pulse_layout);
        oxygen_layout = root.findViewById(R.id.oxygen_layout);
        //mQueue = Volley.newRequestQueue(this.getContext());

        FloatingActionButton map_btn = getActivity().findViewById(R.id.map_button);
        map_btn.setVisibility(View.VISIBLE);

        SharedPreferences share= getActivity().getSharedPreferences("PatientPref", MODE_PRIVATE);
        site_name = share.getString("site","");
        channelID= share.getString("channel","");
        apikey = share.getString("apikey","");
        delay = share.getInt("refreshrate", 15000);

        return root;
    }



    @Override
    public void onResume(){
        super.onResume();
        stopthread = false;
        ExampleRunnable runnable = new ExampleRunnable();
        Thread thread = new Thread(runnable);
        thread.start();


        temp_layout.setOnLongClickListener(new View.OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onLongClick(View view) {
                temp_layout.setElevation(20);

                return false;
            }
        });

        pulse_layout.setOnLongClickListener(new View.OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onLongClick(View view) {
                pulse_layout.setElevation(20);

                return false;
            }
        });

        oxygen_layout.setOnLongClickListener(new View.OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onLongClick(View view) {
                oxygen_layout.setElevation(20);
                return false;
            }
        });
    }


    @Override
    public void onDestroy (){
        super.onDestroy();
        stopthread = true;
    }

    class ExampleRunnable implements Runnable{
        int i = 0;
        @Override
        public void run(){
            while (!Thread.currentThread().isInterrupted()) {
                if (stopthread)
                    return;
                JsonGET();
                mainhandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (connection_error !=null){
                            text_patient.setText(connection_error);
                            text_patient.setBackgroundResource(R.drawable.round_light_background);
                        }
                        else {
                            changeTemp();
                            changeBpm();
                            changeOxygenLevel();
                            if (isTempNotNormal || isPulseNotNormal || isOxygenNotNormal) {
                                text_patient.setText("Patient is Not Normal");
                                text_patient.setBackgroundResource(R.drawable.round_red);
                            } else {
                                text_patient.setText("Patient is Normal");
                                text_patient.setBackgroundResource(R.drawable.round_green);
                            }
                        }
                        test.setText("success"+" "+i);
                        i++;
                        //stopthread = true;
                    }
                });
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    test.setText(e.toString());
                    e.printStackTrace();
                }
            }
        }
    }

    void JsonGET () {
        try {

            URL url = new URL("https://api."+site_name+".com/channels/"+channelID+"/feeds.json?results=1");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();//here is the problem
            //test.setText("test Here");

            //Getting the response code
            int responsecode = conn.getResponseCode();

            if (responsecode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            } else {

                String inline = "";
                Scanner scanner = new Scanner(url.openStream());

                //Write all the JSON data into a string using a scanner
                while (scanner.hasNext()) {
                    inline += scanner.nextLine();
                }

                //Close the scanner
                scanner.close();

                JSONObject jsonObject = new JSONObject(inline);
                JSONArray jsonArray = jsonObject.getJSONArray("feeds");


                body_temp = jsonArray.getJSONObject(0).getString("field1");
                pulse_rate = jsonArray.getJSONObject(0).getString("field2");
                oxygen_percent = jsonArray.getJSONObject(0).getString("field3");


            }

        } catch (Exception e) {
           // test.setText(e.toString());
            connection_error=e.toString();
            e.printStackTrace();
        }
    }


    void changeTemp () {
        temp.setText(body_temp);
        int bar = Integer.parseInt(body_temp);
        if (bar > 37) {
            temp.setTextColor(this.getResources().getColor(R.color.red_200));
            //tempOutline.setBackgroundColor(this.getResources().getColor(R.color.red_200));
            //temp_image.setBackgroundColor(this.getResources().getColor(R.color.red_200));
            tempOutline.setBackgroundResource(R.drawable.solid_circle_red);
            temp_image.setImageResource(R.drawable.ic_baseline_heart_24);
            temp_normal.setText("Not normal");
            temp_normal.setTextColor(getResources().getColor(R.color.red_200));
            isTempNotNormal =true;
        }
        else {
            temp.setTextColor(this.getResources().getColor(R.color.green));
            //tempOutline.setBackgroundColor(this.getResources().getColor(R.color.green));
            //temp_image.setBackgroundColor(this.getResources().getColor(R.color.green));
            tempOutline.setBackgroundResource(R.drawable.solid_circle_green);
            temp_image.setImageResource(R.drawable.ic_heart_green);
            isTempNotNormal= false;
            temp_normal.setTextColor(getResources().getColor(R.color.green));
            temp_normal.setText("Normal");
        }
    }

    void changeBpm () {
        bpm.setText(pulse_rate);
        int bar = Integer.parseInt(pulse_rate);
        if ((bar >= 75) && (bar <= 100)) {
            bpm.setTextColor(this.getResources().getColor(R.color.green));
            //pulseOutline.setBackgroundColor(this.getResources().getColor(R.color.green));
            //pulse_image.setBackgroundColor(this.getResources().getColor(R.color.green));
            pulseOutline.setBackgroundResource(R.drawable.solid_circle_green);
            pulse_image.setImageResource(R.drawable.ic_heart_green);
            isPulseNotNormal= false;
            pulse_normal.setText("Normal");
            pulse_normal.setTextColor(getResources().getColor(R.color.green));
        }
        else {
            bpm.setTextColor(this.getResources().getColor(R.color.red_200));
            //pulseOutline.setBackgroundColor(this.getResources().getColor(R.color.red_200));
            //pulse_image.setBackgroundColor(this.getResources().getColor(R.color.red_200));
            pulseOutline.setBackgroundResource(R.drawable.solid_circle_red);
            pulse_image.setImageResource(R.drawable.ic_baseline_heart_24);
            isPulseNotNormal =true;
            pulse_normal.setText("Not normal");
            pulse_normal.setTextColor(getResources().getColor(R.color.red_200));
        }
    }

    void changeOxygenLevel () {
        oxygen.setText(oxygen_percent);
        int bar = Integer.parseInt(oxygen_percent);
        if (bar < 95) {
            oxygen.setTextColor(this.getResources().getColor(R.color.red_200));
            //oxygenOutline.setBackgroundColor(this.getResources().getColor(R.color.red_200));
            //oxygen_image.setBackgroundColor(this.getResources().getColor(R.color.red_200));
            oxygenOutline.setBackgroundResource(R.drawable.solid_circle_red);
            oxygen_image.setImageResource(R.drawable.ic_baseline_heart_24);
            isOxygenNotNormal =true;
            oxygen_normal.setTextColor(getResources().getColor(R.color.red_200));
            oxygen_normal.setText("Not normal");
        }
        else {
            oxygen.setTextColor(this.getResources().getColor(R.color.green));
            //oxygenOutline.setBackgroundColor(this.getResources().getColor(R.color.green));
            //oxygen_image.setBackgroundColor(this.getResources().getColor(R.color.green));
            oxygenOutline.setBackgroundResource(R.drawable.solid_circle_green);
            oxygen_image.setImageResource(R.drawable.ic_heart_green);
            isOxygenNotNormal=false;
            oxygen_normal.setText("Normal");
            oxygen_normal.setTextColor(getResources().getColor(R.color.green));
        }
    }

}