package com.bignerdranch.weather;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class WeatherAPIs {
    private static final String mOpenWeatherAPIKey = "d05bc56afbcec065e77a87d77dac7999";
    private static final String mMapQuestAPIKey = "ZgyY6PG7ibHEuUXAwNWMUwqPwG7psY7p";
    public static String mWeatherData;

    private static OkHttpClient client = new OkHttpClient();

    private static String runAPIClient(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }

    }

    private static String[] mapQuestService(String address) {

        String lat = "";
        String lng = "";

        address.replace(" ", "+");
        String baseURLString = "http://www.mapquestapi.com";
        String path = "/geocoding/v1/address?key=" + mMapQuestAPIKey + "&location=" + address;

        try {
            String responseBody = WeatherAPIs.runAPIClient(baseURLString + path);
            JSONObject locationData = new JSONObject(responseBody);

            JSONObject results = locationData.getJSONArray("results").getJSONObject(0);
            JSONObject locations = results.getJSONArray("locations").getJSONObject(0);

            int latValue = locations.getJSONObject("latLng").getInt("lat");
            lat = Integer.toString(latValue);

            int lngValue = locations.getJSONObject("latLng").getInt("lng");
            lng = Integer.toString(lngValue);

        } catch (IOException | JSONException e) {
            System.out.println("Mapquest service failed.");
            e.printStackTrace();
        }
        return (new String[] { lat, lng });

    }

    private static void openWeatherService(String lat, String lng, String userRequest) {

        List<String> excludeFilter = new LinkedList<String>(
            Arrays.asList("current", "minutely", "hourly", "daily", "alerts"));

        excludeFilter.remove(userRequest);

        String excludeParameters = TextUtils.join(",", excludeFilter);
        String baseURLString = "https:api.openweathermap.org/";
        String path = "data/2.5/onecall?lat=" + lat + "&lon=" + lng + "&exclude=" + excludeParameters + "&appid="
            + mOpenWeatherAPIKey;

        try {
            mWeatherData = WeatherAPIs.runAPIClient(baseURLString + path);
            JSONObject weatherData = new JSONObject(mWeatherData);
            System.out.println(weatherData.toString(4));
        } catch (IOException | JSONException e) {
            System.out.println("Open Weather service failed.");
            e.printStackTrace();
        }

    }

    public static void run(String userRequest, String address) {
        String[] latlng = WeatherAPIs.mapQuestService(address);
        WeatherAPIs.openWeatherService(latlng[0], latlng[1], userRequest);
    }

}

