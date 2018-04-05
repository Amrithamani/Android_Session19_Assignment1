package com.amritha.acadgild.android_session19_assignment1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //Initializing TextView

    TextView cityField, currentTemperatureField, humidity_field, pressure_field, updatedField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();//hiding Action Bar

        setContentView(R.layout.activity_main);

        //Finding views by id for TextViews

        cityField = findViewById(R.id.input_text_city);
        updatedField = findViewById(R.id.input_text_date_time);
        currentTemperatureField = findViewById(R.id.current_temperature);
        humidity_field = findViewById(R.id.input_text_humidity);
        pressure_field = findViewById(R.id.input_text_pressure);

        //calling interface via class Function passing all the strings obtained from json
        Function.placeIdTask asyncTask = new Function.placeIdTask(new Function.AsyncResponse() {
            public void processFinish(String weather_city, String weather_description, String weather_temperature,
                                      String weather_humidity, String weather_pressure, String weather_updatedOn, String sun_rise) {

                //setting Text for all the Fields

                cityField.setText(weather_city);
                updatedField.setText(weather_updatedOn);
                currentTemperatureField.setText(weather_temperature);
                humidity_field.setText("Humidity: " + weather_humidity);
                pressure_field.setText("Pressure: " + weather_pressure);

            }
        });
        asyncTask.execute("25.180000", "89.530000"); //  asyncTask.execute("Latitude", "Longitude")

    }

}