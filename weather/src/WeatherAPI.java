import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONObject;

public class WeatherAPI {

    private static final String API_KEY = "API";

    public static String getWeather(String city) {
        try {
            String urlStr = "https://api.openweathermap.org/data/2.5/weather?q=" + city +
                    "&appid=" + API_KEY + "&units=metric&lang=en";

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);
            in.close();

            JSONObject json = new JSONObject(response.toString());

            double temp = json.getJSONObject("main").getDouble("temp");
            String description = capitalizeFirstLetter(json.getJSONArray("weather")
                    .getJSONObject(0)
                    .getString("description"));


            String datetime = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            DatabaseManager.insertWeather(city, temp, description, datetime);


            return String.format("Temperature: %.1f°C, %s", temp, description);

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: Failed to fetch weather data. Check the city name or your internet connection.";
        }
    }

    private static String capitalizeFirstLetter(String text) {
        if (text == null || text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}

