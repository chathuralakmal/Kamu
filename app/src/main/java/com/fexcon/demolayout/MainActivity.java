package com.fexcon.demolayout;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;
import com.ms.square.android.glassview.GlassView;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private ListView mListView;
    private GlassView myGlassView;
    private int color;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView myTextView1 = (TextView) findViewById(R.id.textView1);
        Typeface typeface1 = Typeface.createFromAsset(getAssets(), "fonts/Sansation-Regular.ttf");
        myTextView1.setTypeface(typeface1);

        TextView myTextView = (TextView) findViewById(R.id.textCity);
        Typeface typeface=Typeface.createFromAsset(getAssets(), "fonts/CaviarDreams.ttf");
        myTextView.setTypeface(typeface);

        final View myView = (View) findViewById(R.id.activity_main);


        final Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        final Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_in);


        myView.setBackgroundResource(R.drawable.image2);

        LocationManager locationManager;
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();
        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);
        locationManager.getLastKnownLocation(provider);
        // Getting Current Location
        location = locationManager.getLastKnownLocation(provider);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                2000, 1, this);


        if (location != null) {
            Log.e("Kamu","Location"+location.getLongitude());
        }
//        ProgressWheel wheel = new ProgressWheel(this);
//        wheel.setBarColor(Color.BLUE);
//        wheel.spin();

//        SimpleArcDialog mDialog = new SimpleArcDialog(this);
//        mDialog.setConfiguration(new ArcConfiguration(this));
//        mDialog.showWindow(false);
//        mDialog.show();

        final  RelativeLayout locationPanel = (RelativeLayout) findViewById(R.id.locationDetails);
        myGlassView = (GlassView)findViewById(R.id.glass_view);


        final Animation bottomUp = AnimationUtils.loadAnimation(this,
                R.anim.bottom_up);




        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        // Need to runOnUiThread to avoid getting Error.
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                locationPanel.startAnimation(bottomUp);

                                bottomUp.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        // locationPanel.startAnimation(fadeOut);
                                        locationPanel.setVisibility(View.GONE);
                                        loadListView();
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });

                            }
                        });

                    }
                },
                5000
        );

//        ArcConfiguration configuration = new ArcConfiguration(this);
//        configuration.setLoaderStyle(SimpleArcLoader.STYLE.COMPLETE_ARC);
        //configuration.setText("Please wait..");
// Using this configuration with Dialg
//        mDialog.setConfiguration(configuration);
//        mDialog.show();
// Using this configuration with ArcLoader
  //      mSimpleArcLoader.refreshArcLoaderDrawable(configuration);





        //final RelativeLayout listView = (RelativeLayout) findViewById(R.id.listViewLayout);

       // hiddenPanel.setVisibility(View.VISIBLE);

    }



    public void startAnimation(){

    }

    public void loadListView(){

        final ArrayList<Recipe> recipeList = Recipe.getRecipesFromFile("recipes.json", this);
        // Create adapter
        RecipeAdapter adapter = new RecipeAdapter(this, recipeList);

        // Create list view
        mListView = (ListView) findViewById(R.id.recipe_list_view);
        mListView.setAdapter(adapter);


        ArgbEvaluator evaluator = new ArgbEvaluator();
        ValueAnimator animator = new ValueAnimator();
        animator.setIntValues(Color.parseColor("#22000000"), Color.parseColor("#ffffffff"));
        animator.setEvaluator(evaluator);
        animator.setDuration(1000);


        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                color = (int) animation.getAnimatedValue();
                myGlassView.setBackgroundColor(color);
                //postInvalidate(); if you are animating a canvas
                //View.setBackgroundColor(color); another exampleof where to use
            }
        });
        animator.start();

        //myGlassView.setBlurRadius(10);
        //myGlassView.setBackgroundColor(color);
    }


    @Override
    public void onLocationChanged(Location location) {
        int lat = (int) (location.getLatitude());
        int lng = (int) (location.getLongitude());

        Log.e("Kamu","Lat & Long"+lat);
    }

    @Override
    public void onProviderDisabled(String provider) {

        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        Toast.makeText(getBaseContext(), "Gps is turned off!! ",
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderEnabled(String provider) {

        Toast.makeText(getBaseContext(), "Gps is turned on!! ",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

        // TODO Auto-generated method stub

    }
}
