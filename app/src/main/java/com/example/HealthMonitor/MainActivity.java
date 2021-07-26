package com.example.HealthMonitor;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.thekhaeng.pushdownanim.PushDownAnim;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navView;
    private FloatingActionButton map_btn;
    private ImageButton logo,settings,about;
    private TextView title;
    private boolean logo_color = true;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        navView = findViewById(R.id.nav_view);
        map_btn = findViewById(R.id.map_button);
        logo = findViewById(R.id.logo);
        title = findViewById(R.id.apptitle);
        settings = findViewById(R.id.settings);
        about = findViewById(R.id.about);

        PushDownAnim.setPushDownAnimTo(map_btn, logo, settings, about, title)
        .setScale(PushDownAnim.MODE_STATIC_DP, 10)
        .setDurationPush(150)
        .setDurationRelease(350);



        /* // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build(); */

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


        settings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                //getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
                getWindow().setExitTransition(new Fade());
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
            }
        });


        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().setExitTransition(new Fade());
                ActivityOptions option = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this);
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent,option.toBundle());
            }
        });



        map_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                Intent intent = new Intent (Intent.ACTION_VIEW);
                // ,Uri.parse("google.navigation:q=67.405658,88.010996"));
                //intent.setPackage("com.google.android.apps.maps");
                intent.setData(Uri.parse("geo:0,0?q=67.405658,88.010996(Patient home)"));
                if (intent.resolveActivity(getPackageManager()) != null){
                    startActivity(intent);
                }
            }
        });


        //codes for toast
        logo.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick (View view){
            if (logo_color) {
                logo.setBackground(getDrawable(R.drawable.round_green));
                logo_color = false;
            }
            else {
                logo.setBackground(getDrawable(R.drawable.round_red));
                logo_color = true;
            }
        }
        });


        title.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick (View view){
                Toast toast = Toast.makeText(getApplicationContext(),"Health Monitor", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
                return true;
            }
        });

    }

}