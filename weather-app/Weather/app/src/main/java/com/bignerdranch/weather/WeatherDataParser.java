package com.bignerdranch.weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

public class WeatherDataParser {

    public static String tempConverter(String timezone, int temp){
        if(timezone.contains("America")){
            int convertedTemp = (int) ((temp - 273.15) * 1.8 + 32);
            return convertedTemp + "\u2109";
        }else{
            int convertedTemp = (int) (temp - 237.15);
            return convertedTemp + "\u2103";

        }

    }

    public static String timeConverter(String timezone, int unixTime){
        Date date = new java.util.Date(unixTime * 1000L);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone(timezone));
        return sdf.format(date);
    }

    public static String parseCurrentWeatherData(JSONObject data) throws JSONException {

        String timezone = data.getString("timezone");

        int tempK = data.getJSONObject("current")
            .getInt("temp");

        String convertedTemp = tempConverter(timezone, tempK);

        String description = data.getJSONObject("current")
            .getJSONArray("weather")
            .getJSONObject(0)
            .getString("description");

        int feelsLike = data.getJSONObject("current")
            .getInt("feels_like");

        String convertedFeelsLike = tempConverter(timezone, feelsLike);

        int humidity = data.getJSONObject("current")
            .getInt("humidity");

        int windSpeed = data.getJSONObject("current")
            .getInt("wind_speed");

        int sunrise = data.getJSONObject("current")
            .getInt("sunrise");

        int sunset = data.getJSONObject("current")
            .getInt("sunset");

        String formattedSunrise = timeConverter(timezone, sunrise);
        String formattedSunset = timeConverter(timezone, sunset);

        return "\nTimezone: "+ timezone + "\nTemperature: " + convertedTemp + "\nFeels Like: " + convertedFeelsLike +
            "\nForecast: " + description + "\nHumidity: " + humidity + "%" +"\nWind Speed: " + windSpeed + "mph" +
            "\nSunrise: " + formattedSunrise + "\nSunset: " + formattedSunset;

    }

    public static String parseDailyWeatherData(JSONObject data) throws JSONException {
        String timezone = data.getString("timezone");

        String finalOutput = "Timezone: " + timezone + "\n";

        JSONArray daily = data.getJSONArray("daily");
        for(int i = 0; i < daily.length(); i++) {

            int minTempK = daily.getJSONObject(i)
                .getJSONObject("temp").getInt("min");

            int maxTempK = daily.getJSONObject(i)
                .getJSONObject("temp").getInt("max");

            String convertedMinTemp = tempConverter(timezone, minTempK);
            String convertedMaxTemp = tempConverter(timezone, maxTempK);

            String description = daily.getJSONObject(i)
                .getJSONArray("weather")
                .getJSONObject(0)
                .getString("description");

            int feelsLikeDay = daily.getJSONObject(i)
                .getJSONObject("feels_like").getInt("day");

            int feelsLikeNight = daily.getJSONObject(i)
                .getJSONObject("feels_like").getInt("night");

            String convertedFeelsLikeDay = tempConverter(timezone, feelsLikeDay);
            String convertedFeelsLikeNight = tempConverter(timezone, feelsLikeNight);

            int humidity = daily.getJSONObject(i)
                .getInt("humidity");

            int windSpeed = daily.getJSONObject(i)
                .getInt("wind_speed");

            int sunrise = daily.getJSONObject(i)
                .getInt("sunrise");

            int sunset = daily.getJSONObject(i)
                .getInt("sunset");

            String formattedSunrise = timeConverter(timezone, sunrise);
            String formattedSunset = timeConverter(timezone, sunset);

            Date date = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, i);
            SimpleDateFormat sdf = new SimpleDateFormat("E MM.dd.yyyy");
            String currentDayOfWeek = sdf.format(c.getTime());

            finalOutput = finalOutput + "\n" + currentDayOfWeek + "\nLow Daily Temp: " + convertedMinTemp + "\nHigh Daily Temp: " + convertedMaxTemp +
                "\nDay Feels Like: " + convertedFeelsLikeDay + "\nNight Feels Like: " + convertedFeelsLikeNight +
                "\nForecast: " + description + "\nHumidity: " + humidity + "%" + "\nWind Speed: " + windSpeed + "mph" +
                "\nSunrise: " + formattedSunrise + "\nSunset: " + formattedSunset + "\n";
        }
        return finalOutput;
    }

    public static String parseHourlyWeatherData(JSONObject data) throws JSONException {

        String timezone = data.getString("timezone");

        String finalOutput = "Timezone: " + timezone + "\n";

        JSONArray hourly = data.getJSONArray("hourly");
        for(int i = 0; i < hourly.length(); i++) {

            int tempK = hourly.getJSONObject(i)
                .getInt("temp");

            String convertedTemp = tempConverter(timezone, tempK);

            String description = hourly.getJSONObject(i)
                .getJSONArray("weather")
                .getJSONObject(0)
                .getString("description");

            int feelsLike = hourly.getJSONObject(i)
                .getInt("feels_like");

            String convertedFeelsLike = tempConverter(timezone, feelsLike);

            int humidity = hourly.getJSONObject(i)
                .getInt("humidity");

            int windSpeed = hourly.getJSONObject(i)
                .getInt("wind_speed");

            int dt = hourly.getJSONObject(i)
                .getInt("dt");

            String currentHour = timeConverter(timezone, dt);

            finalOutput = finalOutput + "\n" + currentHour + "\nTemperature: " + convertedTemp + "\nFeels Like: " + convertedFeelsLike +
                "\nForecast: " + description + "\nHumidity: " + humidity + "%" + "\nWind Speed: " + windSpeed + " mph\n";
        }
        return finalOutput;
    }

    public static String parseAlertsWeatherData(JSONObject data) throws JSONException {
        String timezone = data.getString("timezone");

        String finalOutput = "Timezone: " + timezone + "\n";

        JSONArray alerts = data.getJSONArray("alerts");
        for(int i = 0; i < alerts.length(); i++) {

            int start = alerts.getJSONObject(i)
                .getInt("start");

            int end = alerts.getJSONObject(i)
                .getInt("end");

            String startConverted = timeConverter(timezone, start);
            String endConverted = timeConverter(timezone, end);

            String description = alerts.getJSONObject(i)
                .getString("description");

            String senderName = alerts.getJSONObject(i)
                .getString("sender_name");

            String event = alerts.getJSONObject(i)
                .getString("event");

        finalOutput = finalOutput + "\nAlert Start: " + startConverted + "\nAlert End: " + endConverted + "\nAdvisory: " + event +
            "\nAdvisory Information: " + description + "\nSource: " + senderName;
        }
        return finalOutput;
    }

    public static String runParser(String weatherData) throws JSONException {
        JSONObject data = new JSONObject(weatherData);

        if (data.has("current")){
            return parseCurrentWeatherData(data);
        } else if(data.has("daily")){
            return parseDailyWeatherData(data);
        }else if(data.has("hourly")){
            return parseHourlyWeatherData(data);
        }else if(data.has("alerts")){
            return parseAlertsWeatherData(data);
        }else{
            return "No information found.";
        }
    }

}
