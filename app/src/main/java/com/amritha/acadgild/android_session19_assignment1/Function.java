package com.amritha.acadgild.android_session19_assignment1;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Amritha on 4/5/18.
 */
public class Function {

    // URL to get contacts JSON
    private static String OPEN_WEATHER_MAP_URL = "http://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b1b15e88fa797225412429c1c50c122a1";

    //API KEY for weather update

    private static final String OPEN_WEATHER_MAP_API = "41246defe95529686d7e3eb3a260979d";

    public interface AsyncResponse {

        void processFinish(String output1, String output2, String output3, String output4,
                           String output5, String output6, String output8);
    }

    /**
     * Async task class to get json by making HTTP call
     */

    public static class placeIdTask extends AsyncTask<String, Void, JSONObject> {

        public AsyncResponse delegate = null;//Call back interface

        public placeIdTask(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interface through constructor
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            JSONObject jsonWeather = null;

            //getting Json data
            try {
                jsonWeather = getWeatherJson(strings[0], strings[1]);
            } catch (Exception e) {
                Log.d("Error", "Cannot process JSON results", e);
            }

            return jsonWeather;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            if (jsonObject != null) {

                try {
                    //initializing weather Objects
                    JSONObject details = jsonObject.getJSONArray("weather").getJSONObject(0);

                    JSONObject main = jsonObject.getJSONObject("main");

                    DateFormat df = DateFormat.getDateTimeInstance();

                    String city = jsonObject.getString("name").toUpperCase(Locale.US) +
                            ", " + jsonObject.getJSONObject("sys").getString("country");


                    String description = details.getString("description").toUpperCase(Locale.US);
                    String temperature = String.format("%.2f", main.getDouble("temp")) + "°";
                    String humidity = main.getString("humidity") + "%";
                    String pressure = main.getString("pressure") + " hPa";

                    String updatedOn = df.format(new Date(jsonObject.getLong("dt") * 1000));

                    delegate.processFinish(city, description, temperature, humidity, pressure, updatedOn, ""
                            + jsonObject.getJSONObject("sys").getLong("sunrise") * 1000);
                    //passing the data via interface

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            super.onPostExecute(jsonObject);
        }

    }

//getting the data in Json

    public static JSONObject getWeatherJson(String lat, String lon) {

        try {

            URL url = new URL(String.format(OPEN_WEATHER_MAP_URL, lat, lon));
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.addRequestProperty("x-api-key", OPEN_WEATHER_MAP_API);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(httpURLConnection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp = "";
            while ((tmp = reader.readLine()) != null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not
            // successful
            if (data.getInt("cod") != 200) {
                return null;
            }

            return data;

        } catch (Exception e) {
            return null;
        }
    }

}
