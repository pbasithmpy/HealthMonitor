package com.example.HealthMonitor.ui.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.HealthMonitor.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.thekhaeng.pushdownanim.PushDownAnim;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {
    private ImageView imageView ;
    private TextView textView;
    private TextView name_view,gender_view, age_view, location_view, phone_view, site_view, channel_view, apikey_view, widget1_view, widget2_view, refresh_delay;
    private EditText name_edit,gender_edit, age_edit,location_edit, phone_edit,site_edit, channel_edit, apikey_edit, widget1_edit, widget2_edit;
    private Button save,map, dial;
    private FloatingActionButton edit;
    private LinearLayout edit_layout, view_layout;
    private String pref_name, pref_age, pref_gender, pref_location, pref_phone, pref_site, pref_channel, pref_apikey, pref_widget1, pref_widget2;
    private boolean edit_flag = false;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        textView = root.findViewById(R.id.text0);

        view_layout = root.findViewById(R.id.view_layout);
        name_view = root.findViewById(R.id.name_view);
        age_view =root.findViewById(R.id.Age_view);
        gender_view = root.findViewById(R.id.gender_view);
        imageView = root.findViewById(R.id.imageView);
        location_view = root.findViewById(R.id.location_view);
        phone_view = root.findViewById(R.id.phone_view);
        site_view = root.findViewById(R.id.site_name);
        channel_view = root.findViewById(R.id.channel_id);
        apikey_view = root.findViewById(R.id.api_key);
        widget1_view = root.findViewById(R.id.widget1_ID);
        widget2_view = root.findViewById(R.id.widget2_ID);
        refresh_delay = root.findViewById(R.id.refresh_delay_value);

        edit_layout = root.findViewById(R.id.edit_layout);
        name_edit = root.findViewById(R.id.name_edit);
        gender_edit = root.findViewById(R.id.gender_edit);
        age_edit = root.findViewById(R.id.age_edit);
        location_edit = root.findViewById(R.id.location_edit);
        phone_edit = root.findViewById(R.id.phone_edit);
        site_edit = root.findViewById(R.id.website_edit);
        channel_edit = root.findViewById(R.id.channel_edit);
        apikey_edit = root.findViewById(R.id.api_edit);
        widget1_edit = root.findViewById(R.id.widget1_edit);
        widget2_edit = root.findViewById(R.id.widget2_edit);

        edit = root.findViewById(R.id.button_edit);
        save = root.findViewById(R.id.button_save);
        map = root.findViewById(R.id.map);
        dial = root.findViewById(R.id.dial);

        FloatingActionButton map_btn = getActivity().findViewById(R.id.map_button);
        map_btn.setVisibility(View.INVISIBLE);

        PushDownAnim.setPushDownAnimTo(map,dial,edit,save)
                .setScale(PushDownAnim.MODE_STATIC_DP, 12)
                .setDurationPush(100)
                .setDurationRelease(225);


        SharedPreferences share= getActivity().getSharedPreferences("PatientPref", MODE_PRIVATE);
        pref_name = share.getString("name", "");
        pref_age = share.getString("age", "");
        pref_gender = share.getString("gender","");
        pref_location = share.getString("location","");
        pref_phone = share.getString("phone","");
        pref_site = share.getString("site","");
        pref_channel = share.getString("channel","");
        pref_apikey = share.getString("apikey","");
        pref_widget1 = share.getString("widget1","");
        pref_widget2 = share.getString("widget2","");

        name_view.setText(pref_name);
        age_view.setText(pref_age);
        gender_view.setText(pref_gender);
        location_view.setText(pref_location);
        phone_view.setText(pref_phone);
        site_view.setText(pref_site);
        channel_view.setText(pref_channel);
        apikey_view.setText(pref_apikey);
        widget1_view.setText(pref_widget1);
        widget2_view.setText(pref_widget2);
        refresh_delay.setText(""+share.getInt("refreshrate", 15));


        return root;
    }


    @Override
    public void onResume (){
        super.onResume();


        dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+pref_phone));
                startActivity(intent);
            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (Intent.ACTION_VIEW);
                intent.setData(Uri.parse("geo:0,0?q="+pref_location));
                if (intent.resolveActivity(getActivity().getPackageManager()) != null){
                    startActivity(intent);
                }

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.setVisibility(View.INVISIBLE);
                view_layout.setVisibility(View.INVISIBLE);
                edit_layout.setVisibility(View.VISIBLE);

                edit_flag = true;

                name_edit.setText(pref_name);
                age_edit.setText(pref_age);
                gender_edit.setText(pref_gender);
                location_edit.setText(pref_location);
                phone_edit.setText(pref_phone);
                site_edit.setText(pref_site);
                channel_edit.setText(pref_channel);
                apikey_edit.setText(pref_apikey);
                widget1_edit.setText(pref_widget1);
                widget2_edit.setText(pref_widget2);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref_name = name_edit.getText().toString();
                pref_age = age_edit.getText().toString();
                pref_gender = gender_edit.getText().toString();
                pref_location = location_edit.getText().toString();
                pref_phone = phone_edit.getText().toString();
                pref_site = site_edit.getText().toString();
                pref_channel = channel_edit.getText().toString();
                pref_apikey = apikey_edit.getText().toString();
                pref_widget1 = widget1_edit.getText().toString();
                pref_widget2 = widget2_edit.getText().toString();

                edit.setVisibility(View.VISIBLE);
                view_layout.setVisibility(View.VISIBLE);
                edit_layout.setVisibility(View.INVISIBLE);
                name_view.setText(pref_name);
                age_view.setText(pref_age);
                gender_view.setText(pref_gender);
                location_view.setText(pref_location);
                phone_view.setText(pref_phone);
                site_view.setText(pref_site);
                channel_view.setText(pref_channel);
                apikey_view.setText(pref_apikey);
                widget1_view.setText(pref_widget1);
                widget2_view.setText(pref_widget2);
                //name_edit.getText().clear();
                //age_edit.getText().clear();
            }
        });
    }

    @Override
    public void onDestroy (){
        super.onDestroy();
        SharedPreferences sharedPref = getActivity().getSharedPreferences("PatientPref", MODE_PRIVATE);
        SharedPreferences.Editor patientEdit = sharedPref.edit();
        if (edit_flag) {
            patientEdit.putString("name", pref_name);
            patientEdit.putString("age", pref_age);
            patientEdit.putString("gender", pref_gender);
            patientEdit.putString("location", pref_location);
            patientEdit.putString("phone", pref_phone);
            patientEdit.putString("site", pref_site);
            patientEdit.putString("channel", pref_channel);
            patientEdit.putString("apikey", pref_apikey);
            patientEdit.putString("widget1", pref_widget1);
            patientEdit.putString("widget2", pref_widget2);
            patientEdit.apply();
        }
    }

}