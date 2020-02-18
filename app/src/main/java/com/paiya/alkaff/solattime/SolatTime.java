package com.paiya.alkaff.solattime;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.paiya.alkaff.solattime.model.WaktuData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class SolatTime extends AppCompatActivity
{
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView dispDate;
    ListView listView;
    Button btnRefresh;
    //Button btnGPS;
    ArrayList<String> WFormat;
    float latitude = 0;
    float longitude = 0;
    //LocationManager locationManager;
    //LocationListener listener;
    FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solat_time);

        dispDate();
        dispWaktuData();
        runNavigationDrawer();

        btnRefresh = findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dispWaktuData();
            }
        });
    }

    void dispDate()
    {
        dispDate = findViewById(R.id.txtDate);

        Date today = new Date();
        SimpleDateFormat d = new SimpleDateFormat("EEEE, dd MMMM yyyy, hh:mm a");
        String todayDate = d.format(today);

        dispDate.setText(todayDate);
    }

    void dispWaktuData()
    {
        //URL Format:
        //http://www.islamicfinder.us/index.php/api/prayer_times?latitude=12.8425&longitude=80.0639&timezone=Asia/Kolkata
        String baseURL = "http://www.islamicfinder.us/index.php/api/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final WaktuApi waktuApi = retrofit.create(WaktuApi.class);
        Call<WaktuData> call = waktuApi.getWaktu(latitude, longitude, "Asia/Kolkata");

        call.enqueue(new Callback<WaktuData>()
        {
            @Override
            public void onResponse(Call<WaktuData> call, Response<WaktuData> response)
            {
                String[] Waktu = {"Subuh", "Sunrise", "Zohr", "Asar", "Maghrib", "Isha"};
                String[] WTime = new String[6];

                listView = findViewById(R.id.lvPrayerTime);
                WFormat = new ArrayList<>();

                //WaktuData waktuData = response.body().getResults().;
                WaktuData waktuData = response.body();

                if (waktuData != null)
                {
                    WTime[0] = waktuData.results.t1.replaceAll("%", "");
                    WTime[1] = waktuData.results.t2.replaceAll("%", "");
                    WTime[2] = waktuData.results.t3.replaceAll("%", "");
                    WTime[3] = waktuData.results.t4.replaceAll("%", "");
                    WTime[4] = waktuData.results.t5.replaceAll("%", "");
                    WTime[5] = waktuData.results.t6.replaceAll("%", "");
                }

                WFormat.add("\t\t" + Waktu[0] + "\t\t" + WTime[0]);

                for (int i = 1; i < 6; i++)
                {
                    WFormat.add("\t\t" + Waktu[i] + ":\t\t" + WTime[i]);
                }

                ArrayAdapter WAdapter;
                WAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, WFormat);
                listView.setAdapter(WAdapter);

                Toast.makeText(getApplicationContext(), "Downloaded Prayer Times!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<WaktuData> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed to Download Prayer Times", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void runNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.viewNavigation);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);

                String selection = item.getTitle().toString();

                //Prayer Times
                if (selection.contains("Prayer Times")) {
                    Toast.makeText(getApplicationContext(), "You are already in Prayer Times Page", Toast.LENGTH_SHORT).show();
                }

                //Calendar Converter
                else if (selection.contains("Convert Gregorian/Hijri")) {
                    Intent intent = new Intent(SolatTime.this, DateConverter.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(getApplicationContext(), "Convert Gregorian/Hijri", Toast.LENGTH_SHORT).show();
                }

                //Set Location
                else if (selection.contains("Set Location")) {
                    setLocation();
                    //Toast.makeText(getApplicationContext(), "T3", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Error: Invalid Selection", Toast.LENGTH_SHORT).show();
                }

                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    void setLocation() {
        client = LocationServices.getFusedLocationProviderClient(SolatTime.this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText setLatitude = new EditText(this);
        setLatitude.setHint("Latitude");
        setLatitude.setSingleLine(true);

        final EditText setLongitude = new EditText(this);
        setLongitude.setHint("Longitude");
        setLongitude.setSingleLine(true);

        // Retrieve GPS Coordinates
        final Button btnGPS;
        btnGPS = new Button(this);
        btnGPS.setText("Get Coordinates");
        btnGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();

                if (ActivityCompat.checkSelfPermission(SolatTime.this, Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED)
                {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                client.getLastLocation().addOnSuccessListener(SolatTime.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            latitude = (float) location.getLatitude();
                            longitude = (float) location.getLongitude();
                            setLatitude.setText(String.valueOf(latitude));
                            setLongitude.setText(String.valueOf(longitude));
                            Toast.makeText(getApplicationContext(), "GPS: " + latitude + ", " + longitude, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            private void requestPermission()
            {
                ActivityCompat.requestPermissions(SolatTime.this, new String[]{ACCESS_FINE_LOCATION},10);
            }
        });

        // Set Coordinates Manually
        final Button btnSet = new Button(this);
        btnSet.setText("Set");
        btnSet.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                longitude = Float.parseFloat(setLongitude.getText().toString());
                latitude = Float.parseFloat(setLatitude.getText().toString());

                Toast.makeText(getApplicationContext(), "Coordinates Set: " + latitude + ", " + longitude, Toast.LENGTH_LONG).show();
            }
        });

        layout.addView(setLatitude);
        layout.addView(setLongitude);
        layout.addView(btnGPS);
        layout.addView(btnSet);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Location").setView(layout);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
