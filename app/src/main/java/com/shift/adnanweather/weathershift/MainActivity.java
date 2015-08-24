package com.shift.adnanweather.weathershift;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends MapsActivity implements LocationProvider.LocationCallback {

    public static final String TAG = MainActivity.class.getSimpleName();
    private CurrentWeather mCurrentWeather;

    private LocationProvider mLocationProvider;
    private GoogleMap mMap;

    protected Button newButton;

    double currentLatitude;
    double currentLongitude;
    String apiKey = "7cc99c4d2617357f95e5a9493ff18a06";


    @InjectView(R.id.temperatureLabel) TextView mTemperatureLabel;
    @InjectView(R.id.humiNum) TextView mHumiNum;
    @InjectView(R.id.preciNum) TextView mPreciNum;
    @InjectView(R.id.dayType) TextView mDayType;
    @InjectView(R.id.iconImageView) TextView mIconImageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        mLocationProvider = new LocationProvider(this, this);
        mTemperatureLabel.setShadowLayer(30,0,0, Color.RED);
        Typeface cf = Typeface.createFromAsset(getAssets(), "fonts/GoodMorningAfternoon.ttf");
        mTemperatureLabel.setTypeface(cf);
    }

    @Override
    public void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());
        Log.d(TAG, "This is handleNewLocation");
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        String forecastURL = "https://api.forecast.io/forecast/" + apiKey + "/" + currentLatitude + "," + currentLongitude;
        Log.d(TAG, forecastURL);



        if (isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forecastURL)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            Log.v(TAG, response.body().string());
                            if (response.isSuccessful()) {
                                mCurrentWeather = getCurrentDetails(jsonData);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        updateDisplay();
                                    }
                                });

                            }
                        } else {
                            alertUserAboutError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                    catch (JSONException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                }


            });
        } else {
            Toast.makeText(this, getString(R.string.network_unavailable_message), Toast.LENGTH_LONG).show();
        }
        Log.d(TAG, "Main UI code is running.");


    }

    private void updateDisplay() {
        mTemperatureLabel.setText(mCurrentWeather.getTemperature() + "");
        mHumiNum.setText(mCurrentWeather.getHumidity() + "");
        mPreciNum.setText(mCurrentWeather.getPrecipChance() + "%");
        mDayType.setText(mCurrentWeather.getSummary());


//        Drawable drawable = getResources().getDrawable(mCurrentWeather.getIconId());
//        mIconImageView.setImageDrawable(drawable);
    }

    private CurrentWeather getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        Log.i(TAG, "From JSON: " + timezone);

        JSONObject currently = forecast.getJSONObject("currently");

        CurrentWeather currentWeather = new CurrentWeather();

        currentWeather.setHumidity(currently.getDouble("humidity"));
        currentWeather.setPrecipChance(currently.getDouble("precipProbability"));
        currentWeather.setTemperature(currently.getDouble("temperature"));

        currentWeather.setTime(currently.getLong("time"));

        currentWeather.setIcon(currently.getString("icon"));
        currentWeather.setSummary(currently.getString("summary"));

        currentWeather.setTimezone("timezone");

        return  currentWeather;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }

        return isAvailable;
    }


        @Override
    protected void onResume() {
        super.onResume();
        mLocationProvider.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationProvider.disconnect();
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }


}
