package com.paiya.alkaff.solattime;

import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.paiya.alkaff.solattime.model.DateData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DateConverter extends AppCompatActivity
{
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Button btnHijri, btnGreg;
    TextView txtOut, txtDay, txtMth, txtYear;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_converter);

        runNavigationDrawer();
        btnHijri = findViewById(R.id.btnToHijri);
        btnGreg = findViewById(R.id.btnToGreg);

        btnHijri.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                btnHijri = findViewById(R.id.btnToHijri);
                btnGreg = findViewById(R.id.btnToGreg);
                txtDay = findViewById(R.id.txtDay);
                txtMth = findViewById(R.id.txtMth);
                txtYear = findViewById(R.id.txtYear);

                int day = Integer.parseInt(txtDay.getText().toString());
                int mth = Integer.parseInt(txtMth.getText().toString());
                int year = Integer.parseInt(txtYear.getText().toString());

                if (day == 0 || mth == 0 || year == 0)
                {
                    Toast.makeText(getApplicationContext(),"Invalid Format", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    convertToHijri(day, mth, year);
                }
            }
        });

        btnGreg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                btnHijri = findViewById(R.id.btnToHijri);
                btnGreg = findViewById(R.id.btnToGreg);
                txtDay = findViewById(R.id.txtDay);
                txtMth = findViewById(R.id.txtMth);
                txtYear = findViewById(R.id.txtYear);

                int day = Integer.parseInt(txtDay.getText().toString());
                int mth = Integer.parseInt(txtMth.getText().toString());
                int year = Integer.parseInt(txtYear.getText().toString());

                if (day == 0 || mth == 0 || year == 0)
                {
                    Toast.makeText(getApplicationContext(),"Invalid Format", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    convertToGreg(day, mth, year);
                }
            }
        });
    }

    void runNavigationDrawer()
    {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.viewNavigation);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                item.setChecked(true);

                String selection = item.getTitle().toString();

                //Prayer Times
                if (selection.contains("Prayer Times"))
                {
                    Intent intent = new Intent(DateConverter.this, SolatTime.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(getApplicationContext(), "Prayer Time", Toast.LENGTH_SHORT).show();
                }

                //Calendar Converter
                else if (selection.contains("Convert Gregorian/Hijri"))
                {
                    Toast.makeText(getApplicationContext(), "You are already in Convert Gregorian/Hijri Page", Toast.LENGTH_SHORT).show();
                }

                //Set Location
                else if (selection.contains("Set Location"))
                {
                    Toast.makeText(getApplicationContext(), "Not Applicable", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Error: Invalid Selection", Toast.LENGTH_SHORT).show();
                }

                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    void convertToHijri(int day, int mth, int yr)
    {
        txtOut = findViewById(R.id.txtOutput);

        //URL Format
        //http://www.islamicfinder.us/index.php/api/calendar?day=3&month=10&year=2018&convert_to=0
        String baseURL = "http://www.islamicfinder.us/index.php/api/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DateApi dateApi = retrofit.create(DateApi.class);
        dateApi.getDate(day, mth, yr, 0).enqueue(new Callback<DateData>()
        {
            @Override
            public void onResponse(Call<DateData> call, Response<DateData> response)
            {
                DateData dateData = response.body();
                String[] str = dateData.getData().split("-");
                txtOut.setText("Output:\n\n" + str[2] + "/" + str[1] + "/" + str[0] + "H");
            }

            @Override
            public void onFailure(Call<DateData> call, Throwable t)
            {
                Toast.makeText(getApplicationContext(), "Failed to Convert", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void convertToGreg(int day, int mth, int yr)
    {
        txtOut = findViewById(R.id.txtOutput);

        //URL Format
        //http://www.islamicfinder.us/index.php/api/calendar?day=3&month=10&year=2018&convert_to=0
        String baseURL = "http://www.islamicfinder.us/index.php/api/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DateApi dateApi = retrofit.create(DateApi.class);
        dateApi.getDate(day, mth, yr, 1).enqueue(new Callback<DateData>()
        {
            @Override
            public void onResponse(Call<DateData> call, Response<DateData> response)
            {
                DateData dateData = response.body();
                String[] str = dateData.getData().split("-");
                txtOut.setText("Output:\n\n" + str[2] + "/" + str[1] + "/" + str[0]);
            }

            @Override
            public void onFailure(Call<DateData> call, Throwable t)
            {
                Toast.makeText(getApplicationContext(), "Failed to Convert", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
