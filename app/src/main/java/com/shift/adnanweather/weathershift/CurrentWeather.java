package com.shift.adnanweather.weathershift;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Adnan on 1/25/2015.
 */
public class CurrentWeather {
    private String mIcon;
    private long mTime;
    private double mTemperature;
    private String mSummary;
    private double mPrecipChance;
    private double mHumidity;

    public String getTimezone() {
        return mTimezone;
    }

    public void setTimezone(String timezone) {
        mTimezone = timezone;
    }

    private String mTimezone;

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public int getTemperature() {
        return (int)Math.round(mTemperature);
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public int getPrecipChance() {
        double precipPercentage = mPrecipChance*100;
        return (int)Math.round(mPrecipChance);
    }

    public void setPrecipChance(double precipChance) {
        mPrecipChance = precipChance;
    }

    public double getHumidity() {
        return mHumidity;
    }

    public int getIconId() {

     int iconId = R.mipmap.clear_day;

     if(mIcon.equals("clear-day")) {
         iconId = R.mipmap.clear_day;
     }
     else if (mIcon.equals("clear-night")) {
         iconId = R.mipmap.clear_night;
     }
     else if (mIcon.equals("rain")) {
         iconId = R.mipmap.rain;
     }
     else if (mIcon.equals("snow")) {
         iconId = R.mipmap.snow;
     }
     else if (mIcon.equals("sleet")) {
         iconId = R.mipmap.sleet;
     }
     else if (mIcon.equals("wind")) {
         iconId = R.mipmap.wind;
     }
     else if (mIcon.equals("fog")) {
         iconId = R.mipmap.fog;
     }
     else if (mIcon.equals("cloudy")) {
         iconId = R.mipmap.cloudy;
     }
     else if (mIcon.equals("partly-cloudy-day")) {
         iconId = R.mipmap.partly_cloudy;
     }
     else if (mIcon.equals("partly-cloudy-night")) {
         iconId = R.mipmap.cloudy_night;
     }

        return iconId;
    }

    public void setHumidity(double humidity) {
        mHumidity = humidity;
    }

    public String getFormattedTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone(getTimezone()));
        Date dateTime = new Date(getTime() *1000);
        String timeString = formatter.format(dateTime);

        return timeString;
    }

}
